package vn.isofh.jpa.service;

import vn.isofh.exception.exception.EmptyException;
import vn.isofh.exception.exception.ExistException;
import vn.isofh.exception.exception.NullException;
import vn.isofh.jpa.entity.Book;
import vn.isofh.jpa.enums.StatusBook;
import vn.isofh.jpa.repository.BookRepository;
import vn.isofh.jpa.repository.HeadBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Service
@Transactional
public class BookService {
    private final BookRepository bookRepository;
    private final HeadBookRepository headBookRepository;

    @Autowired
    public BookService(BookRepository bookRepository,
                       HeadBookRepository headBookRepository) {
        this.bookRepository = bookRepository;
        this.headBookRepository = headBookRepository;
    }

    public List<Book> getAllBook() {
        if (bookRepository.findAll().isEmpty()) {
            throw new EmptyException("List book is empty");
        }
        return bookRepository.findAll();
    }

    public Book getBookById(Long id) {
        if (!isExist(id)) {
            throw new ExistException("this book id '" + id + "' is not exist");
        }
        return bookRepository.getOne(id);
    }

    public List<Book> findByIdAndStatus(Long id, String status) {
        return bookRepository.findByHeadBookIdAndStatusContains(id, status);
    }

    public Book addBook(Book book) {
        if (book.getHeadBook() == null) {
            throw new NullException("headBookId field of book is not null");
        } else if (book.getHeadBook() != null) {
            headBookRepository.save(book.getHeadBook());
        } else if (isExist(book.getId())) {
            throw new ExistException("The id" + book.getId() + " is exist !");
        } else if (!headBookRepository.existsById(book.getHeadBookId())) {
            throw new ExistException("this headBook with id = " + book.getHeadBookId() + " is not exist! create headBook");
        }

        book.setStatus(StatusBook.NORMAL.getStatus());
        book.setAddedDate(new Timestamp(System.currentTimeMillis()));
        bookRepository.save(book);
        return book;
    }

    public Book updateBookById(Book book, Long id) {
        if (!isExist(id)) {
            throw new ExistException("this book id " + id + " is not exist");
        }
        Book book1 = bookRepository.getOne(book.getId());
        book1.set(book);
        bookRepository.save(book1);
        return book1;
    }

    public void deleteBookById(Long id) {
        if (!isExist(id)) {
            throw new ExistException("this book id " + id + " is not exist");
        }
        bookRepository.deleteById(id);
    }

    public boolean isExist(Long id) {
        return bookRepository.existsById(id);
    }
}
