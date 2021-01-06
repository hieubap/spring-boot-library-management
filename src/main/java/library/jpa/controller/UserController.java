package library.jpa.controller;

import library.jpa.Dao.HeadBookDao;
import library.jpa.Dao.ListDao;
import library.jpa.entity.HeadBook;
import library.jpa.responceEntity.EntityResponse;
import library.jpa.service.CardService;
import library.jpa.service.HeadBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@PreAuthorize("hasAnyRole('USER','LIBRARIAN','MANAGER','ADMIN')")
@RequestMapping(value = "/library/jpa")
public class UserController {
    private final CardService cardService;
    private final HeadBookService headBookService;

    @Autowired
    public UserController(CardService cardService, HeadBookService headBookService) {
        this.cardService = cardService;
        this.headBookService = headBookService;
    }

    // *** ok ***
    @RequestMapping(value = "/card/order", method = RequestMethod.POST)
    public EntityResponse<Object> oderBook(@RequestBody ListDao borrowBookDAO, HttpServletRequest httpServletRequest) {
        cardService.orderBooks(borrowBookDAO,httpServletRequest);
        return new EntityResponse<>(HttpStatus.OK, "You just order book. Wait for approval", borrowBookDAO);
    }

    // *** ok ***
    @RequestMapping(value = "/card/order/head", method = RequestMethod.POST)
    public EntityResponse<Object> orderBookByHeadBook(@RequestBody ListDao borrowBookDAO, HttpServletRequest httpServletRequest) {
        cardService.orderBookByHead(borrowBookDAO,httpServletRequest);
        return new EntityResponse<>(HttpStatus.OK, "You just order book. Wait for approval", borrowBookDAO);
    }

    // *** ok ***
    @RequestMapping(value = "/card/order/moreTime", method = RequestMethod.POST)
    public EntityResponse<Object> moreTimeExpire(@RequestBody ListDao borrowBookDAO, HttpServletRequest httpServletRequest) {
        cardService.moreTime(borrowBookDAO,httpServletRequest);
        return new EntityResponse<>(HttpStatus.OK, "You just order more time to your book. Wait for approval", borrowBookDAO);
    }

    // *** ok ***
    @PutMapping(value = {"/card/giveback","/card/order/cancel"})
    public EntityResponse<Object> giveBookBack(@RequestBody ListDao listDao, HttpServletRequest httpServletRequest) {
        cardService.payBook(listDao,httpServletRequest);
        return new EntityResponse<>(HttpStatus.OK, "Book back Successful. follow is list book ID that you just return", listDao);
    }

    // *** ok ***
    @GetMapping(value = "/book/head/id={id}")
    public EntityResponse<HeadBook> findBook(@PathVariable Long id) {
        HeadBook book = headBookService.getHeadBookById(id);
        return new EntityResponse<>(HttpStatus.OK, "Information of HeadBook with id '" + id +"'", book);
    }

    // *** ok ***
    @GetMapping(value = "/book/head/list")
    public EntityResponse<List<HeadBook>> getAllBook() {
        System.out.println("receive request to /book/head/list");
        List<HeadBook> books = headBookService.getAllHeadBook();
        return new EntityResponse<List<HeadBook>>(HttpStatus.OK, "Find all headBooks", books);
    }

    // *** ok ***
    @GetMapping("/book/head/find")
    public EntityResponse<List<HeadBook>> findByDao(@RequestBody HeadBookDao headBookDAO) {
        return new EntityResponse<>(HttpStatus.OK, "find successful", headBookService.findByDao(headBookDAO));
    }
}
