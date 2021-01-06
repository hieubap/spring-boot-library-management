package library.jpa.controller;

import library.jpa.responceEntity.EntityResponse;
import library.jpa.service.CardService;
import library.jpa.service.HeadBookService;
import library.security.Dao.FormEditUser;
import library.security.Dao.FormRegister;
import library.security.Dao.InformationUserDaoResponse;
import library.userdetailservice.userdetail.UserDetailServiceAlter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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
        System.out.println("run here");
        InformationUserDaoResponse data = userDetailServiceAlter.createNewAccount(userDao);
        return new EntityResponse<>(HttpStatus.OK, "Register User Successful. A New Card has create for you .", data);
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public EntityResponse<Object> logout() {
        return new EntityResponse<>(HttpStatus.OK, "logout successful", null);
    }

    // *** ok ***
    @GetMapping(value = "/user/profile")
    public EntityResponse<Object> queryUserProfile(HttpServletRequest httpServletRequest) {
        InformationUserDaoResponse profile = userDetailServiceAlter.getProfile(httpServletRequest);
        String message = "Successful Get Information of Username '" + profile.getUsername() + "'";
        return new EntityResponse<>(HttpStatus.OK, message, profile);
    }

    // *** ok ***
    @PutMapping(value = "/user/profile/edit")
    public EntityResponse<Object> edit(HttpServletRequest httpServletRequest, @RequestBody FormEditUser dao) {
        InformationUserDaoResponse profile = userDetailServiceAlter.editProfile(httpServletRequest, dao);
        String message = "Edit Profile Successful";
        return new EntityResponse<>(HttpStatus.OK, message, profile);
    }
}