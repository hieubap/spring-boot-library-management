package library.security.controller;

import library.jpa.responceEntity.EntityResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class RedixController {
    @RequestMapping(value = "/redix")
    public String getRedirect(HttpServletRequest httpServletRequest) {
        return "redirect:manager/role/all";
    }

    @RequestMapping(value = "/token")
    public String getToken(HttpServletRequest httpServletRequest) {
        System.out.println(httpServletRequest.getHeader("Authorization"));
        return "redirect:manager/role/all";
    }


    @RequestMapping(value = {"/", "/paths/list"})
    @ResponseBody
    public EntityResponse<Object> get() {
        List<String> data = new ArrayList<String>();
        data.add("(POST) /register : register account with role User" +
                ". Json with: 'username', 'password'");
        data.add("(POST) /login : login account. Json with: 'username', 'password'");
        data.add("(GET)  /logout : logout");
        data.add("(GET)  /book/head/list : show all headbooks in database");
        data.add("(POST) /book/head/add : add list books with head book." +
                " This will create new headbook if headbook is not exist." +
                " Add 'numberBook' to add number book to database." +
                " Json with : 'name', 'author', 'publisher', 'price', 'numberOfPages', 'numberBooks'");
        data.add("(GET)  /book/head/find : find all headbook with dao request. " +
                "Json with: 'name', 'publisher', 'author'");
        data.add("(POST) /card/borrow : borrow book with idCard and list idBook. dont borrowed any book if exist book" +
                " is borrowed with field returnBorrow (default = false). " +
                "Json with: 'idCard', 'listIdBooks', 'returnBooks'");
        data.add("(PUT)  /card/back : return book back library with idCard and list idBook." +
                "Json with: 'idCard', 'listIdBooks'");
        data.add("(GET)  /card/{id} : show list book borrowed of by id card");


        return new EntityResponse<>(HttpStatus.OK,"list path and tutorial",data);
    }

}
