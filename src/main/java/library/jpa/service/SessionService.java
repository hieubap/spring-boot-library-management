package library.jpa.service;


import library.exception.exception.ApiRequestException;
import library.exception.exception.NullException;
import library.jpa.DAO.ListDao;
import library.jpa.entity.Book;
import library.jpa.entity.Session;
import library.jpa.enum_.StatusBook;
import library.jpa.enum_.StatusSession;
import library.jpa.repository.BookRepository;
import library.jpa.repository.CardRepository;
import library.jpa.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static library.jpa.enum_.StatusBook.BORROWED;
import static library.jpa.enum_.StatusSession.*;

@Service
@Transactional
public class SessionService {
    private final SessionRepository sessionRepository;
    private final BookRepository bookRepository;
    private final CardRepository cardRepository;

    @Autowired
    public SessionService(SessionRepository repositoryCard,
                          BookRepository headBookRepository,
                          CardRepository cardRepository) {
        this.sessionRepository = repositoryCard;
        this.bookRepository = headBookRepository;
        this.cardRepository = cardRepository;
    }

    public Session getbyID(Long id) {
        return sessionRepository.getOne(id);
    }

    public List<Session> getAll() {
        return sessionRepository.findAll();
    }

    public boolean isExist(Long id) {
        return sessionRepository.existsById(id);
    }

    public void create(Session session) {
        // kiểm tra thông tin sách và số thẻ request có null không
        if (session.getIdBook() == null || session.getIdCard() == null)
            throw new ApiRequestException("cant create Session: Error : booknumber or cardnumber is null");
        else {
            // nếu không null thì có tồn tại trong db hay không
            if (!cardRepository.existsById(session.getIdCard()))
                throw new ApiRequestException("card is not exist");
            if (!bookRepository.existsById(session.getIdBook()))
                throw new ApiRequestException("book is not exist");
        }

        // kiểm tra book đã được mượn chưa
        Book book = bookRepository.getOne(session.getIdBook());
        if (book.getStatus() == "borrowed") {
            throw new ApiRequestException("this book has borrowed");
        }
        System.out.println("count = " + bookRepository.countByHeadBookIdAndStatusIsNot(book.getHeadBookId(), "borrowed"));

        // --------- dưới 5 quyển không mượn được. không tính các quyển đang cho mượn
        if (bookRepository.countByHeadBookIdAndStatusIsNot(book.getHeadBookId(), "borrowed") <= 5) {
            throw new ApiRequestException("has down 5 book '" + book.getHeadBook().getName() + "' so can't borrowed");
        }

        book.setStatus("borrowed");
        bookRepository.save(book);
        session.setDate_borrowed(new Date());
        long days = TimeUnit.MILLISECONDS.convert(30, TimeUnit.DAYS);
        session.setExpiration_date(new Timestamp(System.currentTimeMillis() + days));
        session.setStatus("con han");

        sessionRepository.save(session);
    }

    public void save(Session session) {

        session.setDate_borrowed(new Date());
        long days = TimeUnit.MILLISECONDS.convert(30, TimeUnit.DAYS);
        session.setExpiration_date(new Timestamp(System.currentTimeMillis() + days));
        session.setStatus(ORDER.name());
        sessionRepository.save(session);
    }

//    public boolean isExistByIdBook(Long id){
//        return sessionRepository.isExistByIdBook(id);
//    }

    public Session update(Session session, Long id) {
        Session session1 = sessionRepository.getOne(id);
        session1.set(session);
        sessionRepository.save(session1);
        return session1;
    }

    public void approval(ListDao listDao) {
        if (listDao.getListId() == null)
            throw new NullException("List id session is null. Enter attribute 'listId' and give id session to approved order.");
        if (listDao.getListId().isEmpty())
            throw new NullException("List ID is empty. Enter id session");

        for (Long id : listDao.getListId()) {
            Session session;
            if (sessionRepository.existsById(id))
                session = sessionRepository.getOne(id);
            else
                throw new NullException("Id session '" + id + "' is null. Check your ID Session ");

            if (listDao.getStatus())
                session.setStatus(APPROVED.name());
            else
                session.setStatus(DENY.name());

            // set status book to borrowed if user already want borrowed book
            bookRepository.getOne(session.getIdBook()).setStatus(BORROWED.getStatus());
        }

    }


    public void delete(long id) {
        if (!isExist(id))
            throw new ApiRequestException("this session is not exist");
        sessionRepository.deleteById(id);
    }
}
