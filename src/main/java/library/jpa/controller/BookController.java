package library.jpa.controller;

import library.jpa.entity.Book;
import library.jpa.exportExcel.ExcelHandle;
import library.jpa.responceEntity.EntityResponse;
import library.jpa.service.BookService;
import library.jpa.service.HeadBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping(value = "/book")
public class BookController {
    private final BookService bookService;
    private final HeadBookService headBookService;

    @Autowired
    public BookController(BookService bookService, HeadBookService headBookService) {
        this.bookService = bookService;
        this.headBookService = headBookService;
    }

    @GetMapping(value = "/ex")
    @PreAuthorize("hasAuthority('BOOK_READ')")
    public EntityResponse export() throws IOException {
        ExcelHandle.excelBook(bookService.getAllBook());
        EntityResponse entityResponse = new EntityResponse(200,
                new Timestamp(System.currentTimeMillis()), "export successful", null);
        return entityResponse;
    }

    @GetMapping(value = "/listbook")
    @PreAuthorize("hasAuthority('BOOK_READ')")
    public EntityResponse<List<Book>> getAllBook() {
        List<Book> books = bookService.getAllBook();
        return new EntityResponse<>(HttpStatus.OK, "Find all books", books);
    }

    @GetMapping(value = "/id={id}")
    @PreAuthorize("hasAuthority('BOOK_READ')")
    public EntityResponse<Book> findBook(@PathVariable Long id) {
        return new EntityResponse<>(HttpStatus.OK, "information of book with id = " + id, bookService.getBookById(id));
    }

//    @PostMapping(path = "/add")
//    @PreAuthorize("hasAuthority('BOOK_WRITE')")
//    public EntityResponse<Book> addBook(@RequestBody Book book) {
//        bookService.addBook(book);
//        return new EntityResponse<>(HttpStatus.CREATED, "Create Successful", book);
//    }

    @PutMapping(value = "/update")
    @PreAuthorize("hasAuthority('BOOK_WRITE')")
    public EntityResponse<Book> update(@RequestBody Book book, @RequestParam Long id) {
        bookService.updateBook(book, id);
        return new EntityResponse<>(HttpStatus.OK, "Update Successful", book);
    }

    @DeleteMapping(value = "/delete")
    @PreAuthorize("hasAuthority('BOOK_WRITE')")
    public EntityResponse<Book> delete(@RequestParam Long id) {
        bookService.deleteBookById(id);
        return new EntityResponse<>(HttpStatus.OK, "Delete successful", bookService.getBookById(id));
    }
}
