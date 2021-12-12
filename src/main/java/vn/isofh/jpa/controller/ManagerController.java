package vn.isofh.jpa.controller;


import vn.isofh.jpa.Dao.EditCardFormDao;
import vn.isofh.jpa.Dao.HeadBookDao;
import vn.isofh.jpa.entity.Card;
import vn.isofh.jpa.responceEntity.EntityResponse;
import vn.isofh.jpa.service.CardService;
import vn.isofh.jpa.service.HeadBookService;
import vn.isofh.security.Dao.FormAdminEdit;
import vn.isofh.security.Dao.InformationUserDaoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/library/jpa/manager")
@PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
public class ManagerController {
    private final CardService cardService;
    private final HeadBookService headBookService;

    @Autowired
    public ManagerController(CardService cardService,
                             HeadBookService headBookService) {
        this.cardService = cardService;
        this.headBookService = headBookService;
    }

    // *** ok ***
    @PostMapping(value = "/card/create")
    public EntityResponse<Object> createCardWhenMiss(@RequestBody FormAdminEdit userDao) {
        InformationUserDaoResponse data = cardService.createCard(userDao);
        return new EntityResponse<>(HttpStatus.OK, "Create New Card for user '" + data.getUsername() + "' is Successful", data);
    }

    // *** ok ***
    @GetMapping(value = "/card/list")
    public EntityResponse<List<Card>> getAll() {
        return new EntityResponse<>(HttpStatus.OK, "All Card return successful !", cardService.getAll());
    }

    // *** ok ***
    @GetMapping(value = "/card/{id}")
    public EntityResponse<Card> getID(@PathVariable Long id) {
        return new EntityResponse<>(HttpStatus.OK, "Get by ID '" + id + "'", cardService.getByID(id));
    }

    // *** ok ***
    @PostMapping(value = "/book/add/head")
    public EntityResponse<HeadBookDao> addBook(@RequestBody HeadBookDao headBook) {
        headBookService.addHeadBook(headBook);
        String message = headBook.getNumberBooks() + " Books is added. Successful!";
        return new EntityResponse<>(HttpStatus.CREATED, message, headBook);
    }

    // *** ok ***
    @PutMapping(value = "/card/status/change")
    public EntityResponse<List<Card>> editStatusCard(@RequestBody EditCardFormDao editCardFormDao) {
        String message = " Card is change to status '" + editCardFormDao.getStatus() + "' Successful!";
        return new EntityResponse<>(HttpStatus.CREATED, message, cardService.managerEdit(editCardFormDao));
    }

}
