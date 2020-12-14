package library.jpa.service;

import io.jsonwebtoken.Jwts;
import library.exception.exception.ApiRequestException;
import library.exception.exception.EmptyException;
import library.exception.exception.ExistNullOrBorrowedBookException;
import library.exception.exception.NullException;
import library.jpa.DAO.ListDao;
import library.jpa.DAO.PayBookDAO;
import library.jpa.entity.Book;
import library.jpa.entity.Card;
import library.jpa.entity.HeadBook;
import library.jpa.entity.Session;
import library.jpa.enum_.StatusCard;
import library.jpa.enum_.StatusSession;
import library.jpa.repository.CardRepository;
import library.security.configuration.jwt_config.JwtPropertiesConfiguration;
import library.userdetailservice.model.Account;
import library.userdetailservice.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static library.jpa.enum_.StatusBook.*;
import static library.jpa.enum_.StatusSession.*;

@Service
@Transactional
public class CardService {
    private final CardRepository repositoryCard;
    private final BookService bookService;
    private final HeadBookService headBookService;
    private final SessionService sessionService;
    private final AccountRepository accountRepository;
    private final JwtPropertiesConfiguration jwtPropertiesConfiguration;
    private final SecretKey secretKey;

    @Autowired
    public CardService(CardRepository repositoryCard,
                       BookService bookService,
                       HeadBookService headBookService, SessionService sessionService,
                       AccountRepository accountRepository,
                       JwtPropertiesConfiguration jwtPropertiesConfiguration,
                       SecretKey secretKey) {
        this.repositoryCard = repositoryCard;
        this.bookService = bookService;
        this.headBookService = headBookService;
        this.sessionService = sessionService;
        this.accountRepository = accountRepository;
        this.jwtPropertiesConfiguration = jwtPropertiesConfiguration;
        this.secretKey = secretKey;
    }

    public Card getByID(Long id) {
        if (!isExist(id))
            throw new ApiRequestException("This ID '" + id + "' is not exist ! check id");
        return repositoryCard.getOne(id);
    }

    public List<Card> getAll() {
        return repositoryCard.findAll();
    }

    public Account getAccount(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization").replace(jwtPropertiesConfiguration.getTokenPrefix(), "");

        String username = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token).getBody().getSubject();

        return accountRepository.findByUsername(username);
    }

    // *** good ***
    public void orderBooks(ListDao borrowBookDAO, HttpServletRequest httpServletRequest) {
        // get card ID in token from http request
        Account account = getAccount(httpServletRequest);

        // exception handle
        if (borrowBookDAO.getListId() == null) {
            throw new NullException("List book Id is null. Enter 'listId' of book");
        }
        if (borrowBookDAO.getListId().size() == 0) {
            throw new EmptyException("List book is empty. Enter book ID to borrow book");
        }

        // 3 list to filter list ID book input
        List<Long> listBorrowedId = new ArrayList<>();
        List<Long> listOrdered = new ArrayList<>();
        List<Long> listNullID = new ArrayList<>();
        List<Long> listIdBook = new ArrayList<>();

        // for set status book
        for (Long id : borrowBookDAO.getListId()) {
            // ID book is not exist add to null list
            if (!bookService.isExist(id)) {
                listNullID.add(id);
            }
            // ID book is borrowed add to borrowed list
            else if (bookService.getById(id).getStatus().equals(BORROWED.getStatus())) {
                listBorrowedId.add(id);
            } else if (bookService.getById(id).getStatus().equals(ORDERED.getStatus())) {
                listOrdered.add(id);
            }
            // else add to list book
            else {
                listIdBook.add(id);
            }
        }

        // return library if has book is borrowed or null and user want return when has any book cant borrowed
        if (!listBorrowedId.isEmpty() || !listNullID.isEmpty() || !listOrdered.isEmpty()) {
            StringBuilder listIDNull = new StringBuilder("Null ID: ");
            for (Long id : listNullID) {
                listIDNull.append(" '").append(id.toString()).append("', ");
            }
            StringBuilder listIDBorrowed = new StringBuilder("Borrowed ID: ");
            for (Long id : listBorrowedId) {
                listIDBorrowed.append(" '").append(id.toString()).append("', ");
            }
            StringBuilder listIDOrdered = new StringBuilder("Ordered ID: ");
            for (Long id : listOrdered) {
                listIDOrdered.append(" '").append(id.toString()).append("', ");
            }
            StringBuilder listCanBorrowed = new StringBuilder("ID can borrowed: ");
            for (Long id : listIdBook) {
                listCanBorrowed.append(" '").append(id.toString()).append("', ");
            }

            String[] data = new String[4];
            data[0] = listIDNull.toString();
            data[1] = listIDBorrowed.toString();
            data[2] = listIDOrdered.toString();
            data[3] = listCanBorrowed.toString();

            throw new ExistNullOrBorrowedBookException("Order unsuccessful. Enter correct your Id", data);
        }

        // empty list
        if (listIdBook.isEmpty())
            throw new EmptyException("List book with status NORMAL is empty. All book is can't borrowed");

        // create session
        for (Long id : listIdBook) {
            // id must exist and not borrowed
            Session session = new Session(account.getIdCard(), id);
            sessionService.save(session);

            // set book status is ordered
            bookService.getBookById(id).setStatus(ORDERED.getStatus());
        }

        // list book has been borrowed
        borrowBookDAO.setListId(listIdBook);
    }

    // *** good ***
    public void orderBookByHead(ListDao listdao, HttpServletRequest httpServletRequest) {
        Account account = getAccount(httpServletRequest);

        // exception handle
        if (listdao.getListId() == null) {
            throw new NullException("List book Id is null. Enter 'listId' of book");
        }
        if (listdao.getListId().size() == 0) {
            throw new EmptyException("List book is empty. Enter book ID to borrow book");
        }

        // create list book can ordered
        List<Long> listIdCanOrder = new ArrayList<>();
        List<Long> listNullId = new ArrayList<>();
        List<Long> listIdCantOrder = new ArrayList<>();

        List<Long> listIdBooks = new ArrayList<>();

        // find book not borrowed or ordered by head book
        for (Long id : listdao.getListId()) {
            List<Book> list = new ArrayList<>();

            if (!headBookService.isNotExist(id))
                list = bookService.findByHeadBookId(id, NORMAL.getStatus());

            if (headBookService.isNotExist(id))
                listNullId.add(id);
            else if (list.isEmpty()) {
                listIdCantOrder.add(id);
            } else {
                listIdCanOrder.add(id);
                listIdBooks.add(list.get(0).getId());
            }

        }

        // return library if has book is borrowed or null and user want return when has any book cant borrowed
        if (!listIdCantOrder.isEmpty() || !listNullId.isEmpty()) {
            StringBuilder listIDNull = new StringBuilder("Head Book ID Null: ");
            for (Long id : listNullId) {
                listIDNull.append(" '").append(id.toString()).append("', ");
            }
            StringBuilder listIDCantOrder = new StringBuilder("ID head book cant order because empty: ");
            for (Long id : listIdCantOrder) {
                listIDCantOrder.append(" '").append(id.toString()).append("', ");
            }
            StringBuilder listIDCanOrder = new StringBuilder("ID head book you can order: ");
            for (Long id : listIdCanOrder) {
                listIDCanOrder.append(" '").append(id.toString()).append("', ");
            }

            String[] data = new String[3];
            data[0] = listIDNull.toString();
            data[1] = listIDCantOrder.toString();
            data[2] = listIDCanOrder.toString();

            throw new ExistNullOrBorrowedBookException("Order unsuccessful. Enter correct your Id", data);
        }

        orderBooks(new ListDao(listIdBooks), httpServletRequest);
    }

    public void moreTime(ListDao listDao, HttpServletRequest httpServletRequest) {
        // get card ID in token from http request
        Account account = getAccount(httpServletRequest);

        // exception handle
        if (listDao.getListId() == null) {
            throw new NullException("List book Id is null. Enter 'listId' of book");
        }
        if (listDao.getListId().size() == 0) {
            throw new EmptyException("List book is empty. Enter book ID to borrow book");
        }

        List<Long> listIdCanOrder = new ArrayList<>();
        List<Long> listNotYourId = new ArrayList<>();
        List<Long> listNull = new ArrayList<>();
        List<Long> listOrderedTime = new ArrayList<>();

        List<Long> listIdOfYou = new ArrayList<>();

        for (Session session : account.getCardInformation().getList_Sessions()) {
            listIdOfYou.add(session.getIdBook());
        }

        for (Long id : listDao.getListId()) {

            String status = "";

            if (bookService.getById(id) != null) {
                Session session = bookService.getById(id).getSession();
                if (session != null) {
                    status = session.getStatus();
                }
            }

            if (!bookService.isExist(id)) {
                listNull.add(id);
            } else if (!listIdOfYou.contains(id)) {
                listNotYourId.add(id);
            } else if (status.equals(EXPIRECOMMING.name()) || status.equals(TIMEOUT.name())) {
                listIdCanOrder.add(id);
            } else {
                listOrderedTime.add(id);
            }
        }

        // return library if has book is borrowed or null and user want return when has any book cant borrowed
        if (!listNull.isEmpty() || !listNotYourId.isEmpty() || !listOrderedTime.isEmpty()) {
            StringBuilder listIDNull = new StringBuilder("Null ID book: ");
            for (Long id : listNull) {
                listIDNull.append(" '").append(id.toString()).append("', ");
            }
            StringBuilder listIDBorrowed = new StringBuilder("Not your ID book: ");
            for (Long id : listNotYourId) {
                listIDBorrowed.append(" '").append(id.toString()).append("', ");
            }
            StringBuilder listIDCanOrdered = new StringBuilder("Your ID Book can order : ");
            for (Long id : listIdCanOrder) {
                listIDCanOrdered.append(" '").append(id.toString()).append("', ");
            }
            StringBuilder listIDOrderedTime = new StringBuilder("Your ID you has ordered: ");
            for (Long id : listOrderedTime) {
                listIDOrderedTime.append(" '").append(id.toString()).append("', ");
            }


            String[] data = new String[4];
            data[0] = listIDNull.toString();
            data[1] = listIDBorrowed.toString();
            data[2] = listIDOrderedTime.toString();
            data[3] = listIDCanOrdered.toString();

            throw new ExistNullOrBorrowedBookException("More Time unsuccessful. Enter correct your Id", data);
        }

        for (Long id : listIdCanOrder) {
            Session session = bookService.getBookById(id).getSession();
            session.setStatus(MORETIMEORDER.name());
        }

        listDao.setListId(listIdCanOrder);
    }

    // *** good ***
    public void payBook(ListDao payBookDAO, HttpServletRequest httpServletRequest) {
        // get card ID in token from http request
        Account account = getAccount(httpServletRequest);

        // get card and get list session to handle
        Card cardLibrary = repositoryCard.getOne(account.getIdCard());
        Collection<Session> listSession = cardLibrary.getList_Sessions();

        // card id, list book id not null and list book not empty
        if (payBookDAO.getListId() == null) {
            throw new NullException("List book ID is null. Enter attribute 'listIdBooks' and put your id you want return");
        }
        if (payBookDAO.getListId().size() == 0) {
            throw new EmptyException("List book ID is empty. Enter book ID to return book");
        }

        // 3 list to filter input: listPayBook is list we will return; listNonValue is list not have in your borrowed book list
        // listNullId is list id null
        List<Long> listPayBook = new ArrayList<>();
        List<Long> listNonValue = new ArrayList<>();
        List<Long> listNullId = new ArrayList<>();

        List<Long> listIdInSession = new ArrayList<>();

        // list id book in session
        for (Session session : listSession) {
            listIdInSession.add(session.getIdBook());
        }
        // filter list enter
        for (Long id : payBookDAO.getListId()) {
            if (!bookService.isExist(id)) {
                listNullId.add(id);
            } else if (!listIdInSession.contains(id)) {
                listNonValue.add(id);
            } else {
                listPayBook.add(id);
            }
        }

        // exception empty
        if (listPayBook.isEmpty())
            throw new EmptyException("List book return to library is empty in your List ID you just enter.");

        // set status book back to normal
        for (Long id : listPayBook) {
            bookService.getBookById(id).setStatus(NORMAL.getStatus());
        }

        for (Session session : listSession) {
            if (listPayBook.contains(session.getIdBook())) {
                sessionService.delete(session.getId());
            }
        }

        payBookDAO.setListId(listPayBook);
    }

    public boolean isExist(Long id) {
        return repositoryCard.existsById(id);
    }


    // *** good ***
    public Card createNewCard(HttpServletRequest httpServletRequest) {
        String username = getUsernameFromRequest(httpServletRequest);

        Account account = accountRepository.findByUsername(username);

        Card card = createNewCard();
        card.setId_account(account.getId());
        account.setIdCard(card.getId());

        accountRepository.save(account);
        return card;
    }

    public Card createNewCard() {
        Card card = new Card();
        card.setStatus(StatusCard.ACTIVE.getStatus());
        long year = TimeUnit.MILLISECONDS.convert(365, TimeUnit.DAYS);
        card.setExpiration_date(new Timestamp(System.currentTimeMillis() + year));
        repositoryCard.save(card);
        return card;
    }

    public void update(Card card) {
        repositoryCard.save(card);
    }

    public Card update(Card cardLibrary, Long id) {
        if (!isExist(id)) {
            throw new ApiRequestException("this id is not exist");
        }
        Card cardLibrary1 = repositoryCard.getOne(id);
        cardLibrary1.set(cardLibrary);
        repositoryCard.save(cardLibrary1);

        return cardLibrary1;
    }

    public void back(Long id) {
        Session session = sessionService.getbyID(id);
        session.setStatus("da tra");
        sessionService.update(session, id);

        Book book = bookService.getById(session.getIdBook());
        book.setStatus("binh thuong");
//        bookService.updateBook(book);
    }

    public void delete(long id) {
        if (!isExist(id)) {
            throw new ApiRequestException("this id is not exist");
        }
        repositoryCard.deleteById(id);
    }

    // *** good ***
    public String getUsernameFromRequest(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization").replace(jwtPropertiesConfiguration.getTokenPrefix(), "");

        String username = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token).getBody().getSubject();

        return username;
    }
}

