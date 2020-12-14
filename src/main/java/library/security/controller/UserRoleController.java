package library.security.controller;

import library.jpa.DAO.HeadBookDao;
import library.jpa.DAO.ListDao;
import library.jpa.entity.HeadBook;
import library.jpa.responceEntity.EntityResponse;
import library.jpa.service.CardService;
import library.jpa.service.HeadBookService;
import library.security.DAO.FormEditUser;
import library.security.DAO.InformationUserDao;
import library.userdetailservice.userdetail.UserDetailServiceAlter;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@PreAuthorize("hasAnyRole('USER','LIBRARIAN','MANAGER','ADMIN')")
@RequestMapping(value = "/manager")
public class UserRoleController {
    private final UserDetailServiceAlter userDetailServiceAlter;
    private final CardService cardService;
    private final HeadBookService headBookService;

    public UserRoleController(UserDetailServiceAlter userDetailServiceAlter, CardService cardService, HeadBookService headBookService) {
        this.userDetailServiceAlter = userDetailServiceAlter;
        this.cardService = cardService;
        this.headBookService = headBookService;
    }

    // *** ok ***
    @GetMapping(value = "/user/profile")
    public EntityResponse<Object> queryUserProfile(HttpServletRequest httpServletRequest) {
        InformationUserDao profile = userDetailServiceAlter.getProfile(httpServletRequest);
        String message = "Successful Get Information of Username '" + profile.getUsername() + "'";
        return new EntityResponse<>(HttpStatus.OK, message, profile);
    }

    // *** ok ***
    @PutMapping(value = "/user/profile/edit")
    public EntityResponse<Object> edit(HttpServletRequest httpServletRequest, @RequestBody FormEditUser dao) {
        InformationUserDao profile = userDetailServiceAlter.editProfile(httpServletRequest, dao);
        String message = "Edit Profile Successful";
        return new EntityResponse<>(HttpStatus.OK, message, profile);
    }

    // *** ok ***
    @RequestMapping(value = "/card/order", method = RequestMethod.POST)
    public EntityResponse<Object> oderBook(@RequestBody ListDao borrowBookDAO, HttpServletRequest httpServletRequest) {
        cardService.orderBooks(borrowBookDAO,httpServletRequest);
        return new EntityResponse<>(HttpStatus.OK, "You just order book. Wait for approval", borrowBookDAO);
    }

    // *** ok ***
    @RequestMapping(value = "/card/head/order", method = RequestMethod.POST)
    public EntityResponse<Object> orderBookByHeadBook(@RequestBody ListDao borrowBookDAO, HttpServletRequest httpServletRequest) {
        cardService.orderBookByHead(borrowBookDAO,httpServletRequest);
        return new EntityResponse<>(HttpStatus.OK, "You just order book. Wait for approval", borrowBookDAO);
    }

    // *** ok ***
    @RequestMapping(value = "/card/moretime", method = RequestMethod.POST)
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
        List<HeadBook> books = headBookService.getAllHeadBook();
        return new EntityResponse<List<HeadBook>>(HttpStatus.OK, "Find all headbooks", books);
    }

    // *** ok ***
    @GetMapping("/book/head/find")
    public EntityResponse<List<HeadBook>> findbydao(@RequestBody HeadBookDao headBookDAO) {
        return new EntityResponse<>(HttpStatus.OK, "find successful", headBookService.find(headBookDAO));
    }
}
