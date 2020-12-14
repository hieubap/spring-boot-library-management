package library.jpa.controller;

import library.jpa.DAO.ListDao;
import library.jpa.entity.Session;
import library.jpa.responceEntity.EntityResponse;
import library.jpa.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@ResponseBody
@PreAuthorize("hasAnyRole('ADMIN','LIBRARIAN')")
public class SessionController {// xu li trong service
    @Autowired
    SessionService sessionService;

    @RequestMapping(value = "/session/create", method = RequestMethod.POST)
    public EntityResponse<Session> create(@RequestBody Session session) {
        sessionService.create(session);
        return new EntityResponse<>(HttpStatus.OK,"create Session successful",session);
    }
    @RequestMapping(value = "/session/update", method = RequestMethod.POST)
    public EntityResponse<Session> update(@RequestBody Session session,@RequestParam Long id) {
        return new EntityResponse<>(HttpStatus.OK,"create Session successful",
                sessionService.update(session,id));
    }
    @DeleteMapping(value = "/session/{id}")
    public EntityResponse<Object> delete(@PathVariable Long id) {
        sessionService.delete(id);
        return new EntityResponse<>(HttpStatus.OK, "delete successful",null);
    }
}
