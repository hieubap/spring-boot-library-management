package library.jpa.controller;

import library.jpa.DAO.ListDao;
import library.jpa.DAO.PayBookDAO;
import library.jpa.entity.Card;
import library.jpa.responceEntity.EntityResponse;
import library.jpa.service.CardService;
import library.jpa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@ResponseBody
@RequestMapping(value = "/card")
public class CardController {
    @Autowired
    CardService cardService;
    @Autowired
    UserService studentService;

    // *** ok ***
    @RequestMapping(value = {"/","/list"}, method = RequestMethod.GET)
    public EntityResponse<List<Card>> getList() {
        return new EntityResponse<>(HttpStatus.OK, "Find all card", cardService.getAll());
    }
    @RequestMapping(value = "/list/borrowed", method = RequestMethod.GET)
    public EntityResponse<List<Card>> getListBorrowed() {
        return new EntityResponse<>(HttpStatus.OK, "Find all card", cardService.getAll());
    }
    @RequestMapping(value = "/list/expire", method = RequestMethod.GET)
    public EntityResponse<List<Card>> getListMiss() {
        return new EntityResponse<>(HttpStatus.OK, "Find all card", cardService.getAll());
    }


//    @RequestMapping(value = "/update/{id}",method = RequestMethod.DELETE)
//    public EntityResponse<Object> update(@RequestBody Card cardLibrary, @PathVariable Long id){
//        return new EntityResponse<>(HttpStatus.OK,"delete successful",cardService.update(cardLibrary,id));
//    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public EntityResponse<Object> delete(@PathVariable Long id) {
        cardService.delete(id);
        return new EntityResponse<>(HttpStatus.OK, "delete successful", null);
    }

}