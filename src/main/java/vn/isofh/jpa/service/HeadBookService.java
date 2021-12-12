package vn.isofh.jpa.service;

import vn.isofh.exception.exception.ExistException;
import vn.isofh.exception.exception.NullException;
import vn.isofh.jpa.Dao.HeadBookDao;
import vn.isofh.jpa.entity.Book;
import vn.isofh.jpa.entity.HeadBook;
import vn.isofh.jpa.repository.BookRepository;
import vn.isofh.jpa.repository.HeadBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

import static vn.isofh.jpa.enums.StatusBook.NORMAL;

@Service
@Transactional
public class HeadBookService {
    private final HeadBookRepository headBookRepository;
    private final BookRepository bookRepository;

    @Autowired
    public HeadBookService(HeadBookRepository headBookRepository,
                           BookRepository bookRepository) {
        this.headBookRepository = headBookRepository;
        this.bookRepository = bookRepository;
    }

//    @HystrixCommand(fallbackMethod = "renderError")
    public List<HeadBook> getAllHeadBook() {
        throw new NullException("null active");
//        if (headBookRepository.findAll().isEmpty()) {
//            throw new EmptyException("list headBooks is empty");
//        }
//        return headBookRepository.findAll();
    }

    public void renderError(){
        System.out.println("error fallback");
    }

    public HeadBook getHeadBookById(Long id) {
        if (isNotExist(id)) {
            throw new ExistException("this head book id '" + id + "' is not exist");
        }
        return headBookRepository.getOne(id);
    }

    public List<HeadBook> findByDao(HeadBookDao headBookDAO) {
        headBookDAO.autoSet();

        return headBookRepository.findByNameContainsAndPublisherContainsAndAuthorContaining(
                headBookDAO.getName(),
                headBookDAO.getPublisher(),
                headBookDAO.getAuthor()
        );
    }

    public void addHeadBook(HeadBookDao headBookdao) {
        if (headBookdao.getNumberBooks() == null || headBookdao.getName() == null ||
                headBookdao.getAuthor() == null || headBookdao.getPublisher() == null ||
                headBookdao.getPrice() == null || headBookdao.getNumberOfPages() == null) {
            throw new NullException("Attribute 'id', 'name', 'author', 'publisher', 'price', 'numberOfPages', 'numberBooks' field of Head Book is null. All Attribute must set");
        }

        HeadBook headBook = new HeadBook();

        // if head book is exist then not create new head book
        if (!isNotExist(headBookdao)) {
            headBook.setHeadBook(headBookdao);
            headBookRepository.save(headBook);
        } else
        {
            headBook = headBookRepository.findByNameContaining(headBookdao.getName());
        }

        // add new book by number Books
        for (int i = 0; i < headBookdao.getNumberBooks(); i++) {
            Book book = new Book();

            book.setHeadBookId(headBook.getId());
            book.setStatus(NORMAL.getStatus());
            book.setAddedDate(new Timestamp(System.currentTimeMillis()));

            bookRepository.save(book);
        }

    }


    public void updateHeadBook(HeadBook headBook, Long id) {
        if (isNotExist(id)) {
            throw new ExistException("this headBook id " + id + " is not exist");
        }
        HeadBook headBook1 = headBookRepository.getOne(headBook.getId());
        headBook1.setHeadBook(headBook);
        headBookRepository.save(headBook1);
    }


    public void deleteHeadBookById(Long id) {
        if (isNotExist(id)) {
            throw new ExistException("this headBook id " + id + " is not exist");
        }
        headBookRepository.deleteById(id);
    }

    public boolean isNotExist(Long id) {
        return !headBookRepository.existsById(id);
    }

    public boolean isNotExist(HeadBookDao headBookDAO) {
        HeadBook h = headBookRepository.findByNameContainsAndPublisherContainsAndAuthorContains(
                headBookDAO.getName(),
                headBookDAO.getPublisher(),
                headBookDAO.getAuthor()
        );
        return null != h;
    }
}
