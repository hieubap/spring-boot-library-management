package vn.isofh.jpa.service;


import vn.isofh.exception.exception.ApiRequestException;
import vn.isofh.exception.exception.NullException;
import vn.isofh.jpa.Dao.ListDao;
import vn.isofh.jpa.entity.Book;
import vn.isofh.jpa.entity.Session;
import vn.isofh.jpa.repository.BookRepository;
import vn.isofh.jpa.repository.CardRepository;
import vn.isofh.jpa.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static vn.isofh.jpa.enums.StatusBook.*;
import static vn.isofh.jpa.enums.StatusSession.*;

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


            if (!session.getStatus().equals(GIVEBACK.name())&&!session.getStatus().equals(ORDERED.name())
                    &&!session.getStatus().equals(MORETIMEORDER.name())) {
                sessionRepository.deleteById(session.getId());
                continue;
            }

            if (listDao.getStatus()) {

                if (session.getStatus().equals(ORDER.name())) {
                    // set status book to borrowed if user already want borrowed book
                    bookRepository.getOne(session.getIdBook()).setStatus(BORROWED.getStatus());

                    // set status is approved
                    session.setStatus(APPROVED.name());
                }
                else if (session.getStatus().equals(MORETIMEORDER.name())) {
                    Date timestamp = session.getDate_borrowed();

                    long days = TimeUnit.MILLISECONDS.convert(7, TimeUnit.DAYS);
                    timestamp.setTime(timestamp.getTime()+days);

                    session.setExpiration_date(timestamp);

                    // set status is approved
                    session.setStatus(APPROVED.name());
                }
                else {
                    Book book = bookRepository.getOne(session.getIdBook());
                    book.setStatus(NORMAL.getStatus());

                    sessionRepository.delete(session);
                }
            } else {
                // if order then delete session
                if (session.getStatus().equals(ORDER.name())||session.getStatus().equals(GIVEBACK.name()))
                {
                    sessionRepository.delete(session);
                    bookRepository.getOne(session.getIdBook()).setStatus(NORMAL.getStatus());
                }
                // if moreTime or give back then set deny
                else if (session.getStatus().equals(MORETIMEORDER.name()))
                    session.setStatus(DENY.name());
            }
        }
    }

    public void delete(long id) {
        if (!isExist(id))
            throw new ApiRequestException("this session is not exist");
        sessionRepository.deleteById(id);
    }
}
