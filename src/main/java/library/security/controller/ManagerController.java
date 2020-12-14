package library.security.controller;


import library.jpa.DAO.HeadBookDao;
import library.jpa.entity.Card;
import library.jpa.responceEntity.EntityResponse;
import library.jpa.service.CardService;
import library.jpa.service.HeadBookService;
import library.security.DAO.FormAdminEdit;
import library.security.DAO.InformationUserDao;
import library.userdetailservice.service.RoleService;
import library.userdetailservice.userdetail.UserDetailServiceAlter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/manager")
@PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
public class ManagerController {
    private final RoleService roleService;
    private final CardService cardService;
    private final UserDetailServiceAlter userDetailServices;
    private final HeadBookService headBookService;

    @Autowired
    public ManagerController(RoleService roleService, CardService cardService, UserDetailServiceAlter userDetailServices, HeadBookService headBookService) {
        this.roleService = roleService;
        this.cardService = cardService;
        this.userDetailServices = userDetailServices;
        this.headBookService = headBookService;
    }

    //    @PostMapping(value = "/card/create")
//    public EntityResponse<Card> createCard(HttpServletRequest httpServletRequest) {
//        Card card = cardService.createNewCard(httpServletRequest);
//        return new EntityResponse<>(HttpStatus.OK, "Create Card Successful ", card);
//    }
    // *** ok ***
    @PostMapping(value = "/card/create")
    public EntityResponse<Object> createCardWhenMiss(@RequestBody FormAdminEdit userDao) {
        InformationUserDao data = userDetailServices.createCard(userDao);
        return new EntityResponse<>(HttpStatus.OK, "Create New Card for user '" + data.getUsername() + "' is Successful", data);
    }

    // *** ok ***
    @GetMapping(value = "/card/{id}")
    public EntityResponse<Card> getID(@PathVariable Long id) {
        return new EntityResponse<>(HttpStatus.OK, "Get by ID '" + id + "'", cardService.getByID(id));
    }

    // *** ok ***
    @PostMapping(value = "/book/head/add")
    public EntityResponse<HeadBookDao> addBook(@RequestBody HeadBookDao headBook) {
        headBookService.addHeadBook(headBook);
        String message = headBook.getNumberBooks() + " Books is added. Successful!";
        return new EntityResponse<>(HttpStatus.CREATED, message, headBook);
    }
}
