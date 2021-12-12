package vn.isofh.jpa.controller;

import vn.isofh.jpa.Dao.ListDao;
import vn.isofh.jpa.entity.Session;
import vn.isofh.jpa.responceEntity.EntityResponse;
import vn.isofh.jpa.service.CardService;
import vn.isofh.jpa.service.SessionService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@PreAuthorize("hasAnyRole('LIBRARIAN','ADMIN','MANAGER')")
@RequestMapping(value = "library/jpa/manager")
public class LibrarianController{
    private final CardService cardService;
    private final SessionService sessionService;

    public LibrarianController(CardService cardService, SessionService sessionService) {
        this.cardService = cardService;
        this.sessionService = sessionService;
    }

    // *** ok ***
    @GetMapping(value = "/session/list")
    public EntityResponse<List<Session>> getList() {
        return new EntityResponse<>(HttpStatus.OK, "find all session", sessionService.getAll());
    }

    // *** ok ***
    @GetMapping(value = "/session/{id}")
    public EntityResponse<Session> getID(@PathVariable Long id) {
        return new EntityResponse<>(HttpStatus.OK, "get by id = " + id, sessionService.getbyID(id));
    }

    // *** ok ***
    @PutMapping(value = "/session/approved")
    public EntityResponse<Object> approval(@RequestBody ListDao listDao) {
        sessionService.approval(listDao);
        return new EntityResponse<>(HttpStatus.OK, "Approved Successful",listDao);
    }
}
