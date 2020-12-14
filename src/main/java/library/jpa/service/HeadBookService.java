package library.jpa.service;

import library.exception.exception.EmptyException;
import library.exception.exception.ExistException;
import library.exception.exception.NullException;
import library.jpa.DAO.HeadBookDao;
import library.jpa.entity.Book;
import library.jpa.entity.HeadBook;
import library.jpa.repository.BookRepository;
import library.jpa.repository.HeadBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

import static library.jpa.enum_.StatusBook.NORMAL;

@Service
@Transactional
public class HeadBookService {
    private final HeadBookRepository headBookRepository;
    private final BookRepository bookRepository;

    @Autowired
    public HeadBookService(HeadBookRepository headBookRepository, BookRepository bookRepository) {
        this.headBookRepository = headBookRepository;
        this.bookRepository = bookRepository;
    }

    public List<HeadBook> getAllHeadBook() {
        if (headBookRepository.count() == 0) {
            throw new EmptyException("list headBooks is empty");
        }
        return headBookRepository.findAll();
    }

    public HeadBook getHeadBookById(Long id) {
        if (isNotExist(id)) {
            throw new ExistException("this head book id '" + id + "' is not exist");
        }
        return headBookRepository.getOne(id);
    }

//    public List<HeadBook> findbyname(String name) {
//        return headBookRepository.findByNameContains(name);
//    }
//
//    public List<HeadBook> findbyauthor(String name) {
//        return headBookRepository.findByAuthorContains(name);
//    }
//
//    public List<HeadBook> findbypublish(String name) {
//        return headBookRepository.findByPublisherContains(name);
//    }

    public List<HeadBook> find(HeadBookDao headBookDAO) {
        headBookDAO.autoSet();

        return headBookRepository.findByNameContainsAndPublisherContainsAndAuthorContaining(
                headBookDAO.getName(),
                headBookDAO.getPublisher(),
                headBookDAO.getAuthor()
        );
    }


//    public void addHeadBook(HeadBook headBook) {
//        if (headBook.getId() == null || headBook.getName() == null ||
//                headBook.getAuthor() == null || headBook.getPublisher() == null ||
//                headBook.getPrice() == null || headBook.getNumberOfPages() == null) {
//            throw new ApiRequestException("id/name/author/publisher/price/numberofpage field of headbook is null");
//        }
//        if (isExist(headBook.getId())) {
//            throw new ExistException("this headbook id " + headBook.getId() + " is not exist");
//        }
//
//        headBookRepository.save(headBook);
//    }

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
            throw new ExistException("this headbook id " + id + " is not exist");
        }
        HeadBook headBook1 = headBookRepository.getOne(headBook.getId());
        headBook1.setHeadBook(headBook);
        headBookRepository.save(headBook1);
    }


    public void deleteHeadBookById(Long id) {
        if (isNotExist(id)) {
            throw new ExistException("this headbook id " + id + " is not exist");
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
