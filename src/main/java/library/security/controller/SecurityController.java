package library.security.controller;

import library.jpa.responceEntity.EntityResponse;
import library.jpa.service.CardService;
import library.jpa.service.HeadBookService;
import library.security.DAO.FormRegister;
import library.security.DAO.InformationUserDao;
import library.userdetailservice.userdetail.UserDetailServiceAlter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class SecurityController {
    private final UserDetailServiceAlter userDetailServiceAlter;
    private final CardService cardService;
    private final HeadBookService headBookService;

    public SecurityController(UserDetailServiceAlter userDetailServiceAlter, CardService cardService, HeadBookService headBookService) {
        this.userDetailServiceAlter = userDetailServiceAlter;
        this.cardService = cardService;
        this.headBookService = headBookService;
    }

    // *** ok ***
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public EntityResponse<Object> register(@RequestBody FormRegister userDao) {
        InformationUserDao data = userDetailServiceAlter.createNewAccount(userDao);
        return new EntityResponse<>(HttpStatus.OK, "Register User Successful. A New Card has create for you .", data);
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public EntityResponse<Object> logout() {
        return new EntityResponse<>(HttpStatus.OK, "logout successful", null);
    }

}