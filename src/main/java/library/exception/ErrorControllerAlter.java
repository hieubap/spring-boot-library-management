package library.exception;

import library.exception.exception.ResourceException;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@RestController
public class ErrorControllerAlter implements ErrorController {

    @RequestMapping("/error")
    public void handlerError(HttpServletRequest httpServletRequest){
        System.out.println("ERROR");
        int status = Integer.valueOf(httpServletRequest.getAttribute(RequestDispatcher.ERROR_STATUS_CODE).toString());

//        HttpSecurity
        throw new ResourceException(status);
    }


    @Override
    public String getErrorPath() {
        return "/error";
    }
}
