package library.jpa.service;

import library.exception.exception.EmptyException;
import library.exception.exception.ExistException;
import library.exception.exception.NullException;
import library.jpa.entity.Book;
import library.jpa.repository.BookRepository;
import library.jpa.repository.HeadBookRepository;
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
        if (bookRepository.count() == 0) {
            throw new EmptyException("List book is empty");
        }
        return bookRepository.findAll();
    }

    public Book getById(Long id) {
        if (isExist(id))
            return bookRepository.getOne(id);
        return null;
    }

    public Book getBookById(Long id) {
        if (!isExist(id)) {
            throw new ExistException("this book id " + id + " is not exist");
        }
        return bookRepository.getOne(id);
    }
    public List<Book> findByHeadBookId(Long id,String status){
        return bookRepository.findByHeadBookIdAndStatusContains(id,status);
    }

    public Book addBook(Book book) {
        if (book.getHeadBook() == null) {
            throw new NullException("headbookid field of book is not null");
        } else if (book.getHeadBook() != null) {
            headBookRepository.save(book.getHeadBook());
        } else if (isExist(book.getId())) {
            throw new ExistException("The id" + book.getId() + " is exist !");
        } else if (!headBookRepository.existsById(book.getHeadBookId())) {
            throw new ExistException("this headbook with id = " + book.getHeadBookId() + " is not exist! create headbook");
        }

        book.setStatus("binh thuong");
        book.setAddedDate(new Timestamp(System.currentTimeMillis()));
        bookRepository.save(book);
        return book;
    }

    public Book updateBook(Book book, Long id) {
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
