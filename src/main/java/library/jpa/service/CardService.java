package library.jpa.service;

import com.google.common.base.Strings;
import io.jsonwebtoken.Jwts;
import library.exception.exception.*;
import library.jpa.Dao.EditCardFormDao;
import library.jpa.Dao.ListDao;
import library.jpa.entity.Book;
import library.jpa.entity.Card;
import library.jpa.entity.Session;
import library.jpa.enums.StatusCard;
import library.jpa.repository.CardRepository;
import library.security.Dao.FormAdminEdit;
import library.security.Dao.InformationUserDaoResponse;
import library.security.configuration.jwt_config.JwtPropertiesConfiguration;
import library.userdetailservice.model.Account;
import library.userdetailservice.repository.AccountRepository;
import org.apache.catalina.mapper.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static library.jpa.enums.StatusBook.*;
import static library.jpa.enums.StatusSession.*;

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

    // *** good ***
    public void orderBooks(ListDao borrowBookDAO, HttpServletRequest httpServletRequest) {
        // get card ID in token from http request
        Account account = getAccount(httpServletRequest);

        // check punish
        Card card = account.getCardInformation();
        if (card.getStatus().equals(StatusCard.INACTIVE.getStatus())) {
            throw new ApiRequestException("Your Card is inactive. You can't order Book");
        } else if (card.getStatus().equals(StatusCard.PENALTY.getStatus())
                && card.getList_Sessions().size() >= 5) {
            throw new ApiRequestException("Your Card is penalty. You can't borrowed over 5 book");
        }

        // exception handle
        if (borrowBookDAO.getListId() == null) {
            throw new NullException("List book Id is null. Enter 'listId' of book");
        }
        if (borrowBookDAO.getListId().isEmpty()) {
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
            else if (bookService.getBookById(id).getStatus().equals(BORROWED.getStatus())) {
                listBorrowedId.add(id);
            } else if (bookService.getBookById(id).getStatus().equals(ORDERED.getStatus())) {
                listOrdered.add(id);
            }
            // else add to list book
            else {
                listIdBook.add(id);
            }
        }

        // return library if has book is borrowed or null and user want return when has any book cant borrowed
        if (!listBorrowedId.isEmpty() || !listNullID.isEmpty() || !listOrdered.isEmpty()) {
            Map<String,List<Long>> map = new HashMap<>();
            map.put("List ID Null",listNullID);
            map.put("List ID Borrowed",listBorrowedId);
            map.put("List ID is Ordered",listOrdered);
            map.put("List ID you can order",listIdBook);

            throw new ExistNullOrBorrowedBookException("Order unsuccessful. Enter correct your Id", map);
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
                list = bookService.findByIdAndStatus(id, NORMAL.getStatus());

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
            Map<String,List<Long>> map = new HashMap<>();
            map.put("List Id null",listNullId);
            map.put("List Id can't",listIdCantOrder);
            map.put("List Id you can order",listIdCanOrder);

            throw new ExistNullOrBorrowedBookException("Order unsuccessful. Enter correct your Id", map);
        }

        orderBooks(new ListDao(listIdBooks), httpServletRequest);
    }

    // *** ok ***
    public InformationUserDaoResponse createCard(FormAdminEdit formAdminEdit) {
        preExceptionIdOrUsername(formAdminEdit.getUsername(), formAdminEdit.getIdAccount());

        // get account switch username or id from request
        Account account = getAccountByIdOrUsername(formAdminEdit.getUsername(), formAdminEdit.getIdAccount());

        // set status card of user is miss
        Card cardMiss = getByID(account.getIdCard());
        cardMiss.setStatus(StatusCard.MISS.getStatus());
        updateById(cardMiss);

        // create new card
        Card card = createNewCard();
        card.setId_account(account.getId());

        // set ID Role account to change
        account.setIdCard(card.getId());

        // set role to account to response to client
        account.setCardInformation(card);

        // save account to database
        accountRepository.save(account);

        return new InformationUserDaoResponse(account);
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

            if (bookService.getBookById(id) != null) {
                Session session = bookService.getBookById(id).getSession();
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
            if (!session.getStatus().equals(EXPIRECOMMING.name())) {
                throw new ApiRequestException("not expired date coming");
            }
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
            throw new NullException("List book ID is null. Enter attribute 'listId' and put your id you want return");
        }
        if (payBookDAO.getListId().isEmpty()) {
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
            throw new EmptyException("List book return to library is empty in your List ID you just enter. Check your ID return !");

        for (Session session : listSession) {
            if (listPayBook.contains(session.getIdBook())) {
                sessionService.getbyID(session.getId()).setStatus(GIVEBACK.name());
            }
        }

        payBookDAO.setListId(listPayBook);
    }

    public boolean isExist(Long id) {
        return repositoryCard.existsById(id);
    }

    public Card createNewCard() {
        Card card = new Card();
        card.setStatus(StatusCard.ACTIVE.getStatus());
        long year = TimeUnit.MILLISECONDS.convert(365, TimeUnit.DAYS);
        card.setExpiration_date(new Timestamp(System.currentTimeMillis() + year));
        repositoryCard.save(card);
        return card;
    }

    public List<Card> managerEdit(EditCardFormDao editCardFormDao) {
        // exception handle
        if (editCardFormDao.getListId() == null) {
            throw new NullException("List Card Id is null. Enter 'listId' of Card");
        }
        if (editCardFormDao.getListId().size() == 0) {
            throw new EmptyException("List ID is empty. Enter book ID to borrow Card");
        }
        if (Strings.isNullOrEmpty(editCardFormDao.getStatus())) {
            throw new EmptyException("Status is null or empty. enter 'status' attribute");
        }

        for (Long id : editCardFormDao.getListId()) {
            if (!repositoryCard.existsById(id)) {
                throw new ExistException("id '" + id + "' of card ís not exist");
            }
        }

        if (!StatusCard.isDefine(editCardFormDao.getStatus())) {
            throw new ApiRequestException("status '" + editCardFormDao.getStatus() + "' not defined", StatusCard.getStatusString());
        }

        List<Card> cardList = new ArrayList<Card>();

        for (Long id : editCardFormDao.getListId()) {
            Card card = repositoryCard.getOne(id);
            card.setStatus(editCardFormDao.getStatus());
            cardList.add(card);
        }
        return cardList;
    }

    public void updateById(Card card) {
        repositoryCard.save(card);
    }

    public Card updateById(Card cardLibrary, Long id) {
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

        Book book = bookService.getBookById(session.getIdBook());
        book.setStatus(NORMAL.getStatus());

        sessionService.delete(id);
    }

    public void delete(long id) {
        if (!isExist(id)) {
            throw new ExistException("this id '" + id + "' is not exist");
        }
        repositoryCard.deleteById(id);
    }

    // *** good ***
    public String getUsernameFromRequest(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization").replace(jwtPropertiesConfiguration.getTokenPrefix(), "");
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token).getBody().getSubject();
    }

    public void preExceptionIdOrUsername(String username, Long id) {
        if (username == null && id == null)
            throw new NullException("account ID and Username is null. Enter 'idAccount' or 'username' to request.");
        if (!accountRepository.existsByUsername(username) && username != null)
            throw new ExistException("This Username '" + username + "' is not existed");
        if (id != null && !accountRepository.existsById(id))
            throw new ExistException("This User ID '" + id + "' is not existed");
    }

    public Account getAccountByIdOrUsername(String username, Long id) {
        if (username != null) {
            return accountRepository.findByUsername(username);
        } else {
            return accountRepository.getOne(id);
        }
    }

    public Account getAccount(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization").replace(jwtPropertiesConfiguration.getTokenPrefix(), "");

        String username = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token).getBody().getSubject();

        return accountRepository.findByUsername(username);
    }
}

