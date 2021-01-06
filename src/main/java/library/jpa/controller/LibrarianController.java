package library.jpa.controller;

import library.jpa.Dao.ListDao;
import library.jpa.entity.Session;
import library.jpa.responceEntity.EntityResponse;
import library.jpa.service.CardService;
import library.jpa.service.SessionService;
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
