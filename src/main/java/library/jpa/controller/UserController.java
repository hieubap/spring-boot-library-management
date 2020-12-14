package library.jpa.controller;

import library.jpa.entity.User;
import library.jpa.responceEntity.EntityResponse;
import library.jpa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

@RestController
@PreAuthorize("hasRole('ADMIN')")
public class UserController {
    @Autowired
    UserService studentService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/student/showall", method = RequestMethod.GET)
    public EntityResponse getAll()
    {
        EntityResponse entityResponse = new EntityResponse(200,
                new Timestamp(System.currentTimeMillis()),"successful",studentService.showStudent());
        return entityResponse;
    }

    @RequestMapping(value = "/student/id={id}", method = RequestMethod.GET)
    public EntityResponse getById(@PathVariable Long id) {
        EntityResponse entityResponse = new EntityResponse(200, new Timestamp(System.currentTimeMillis()),"get ok",
                studentService.getByID(id));
        return entityResponse;
    }

    @RequestMapping(value = "/student/find/name={name}", method = RequestMethod.GET)
    public EntityResponse<List<User>> findbyname(@PathVariable String name) {
        return new EntityResponse<>(HttpStatus.OK,"find ok",studentService.findbyname(name));
    }
    @RequestMapping(value = "/student/find/phone={phone}", method = RequestMethod.GET)
    public EntityResponse<List<User>> findbyphone(@PathVariable String phone) {
        return new EntityResponse<>(HttpStatus.OK,"find by phone",studentService.findbyphone(phone)) ;
    }

    @RequestMapping(value = "/student/create", method = RequestMethod.POST)
    public EntityResponse<User> create(@RequestBody User student){
        studentService.add(student);
        return new EntityResponse<>(HttpStatus.OK,"create successful",student);
    }

    @RequestMapping(value = {"/student/update"}, method = RequestMethod.PUT)//?id=
    public EntityResponse<User> updatebyid(@RequestBody User student, @RequestParam Long id){
        return new EntityResponse<>(HttpStatus.OK,"update successful",
                studentService.updateById(student,id));
    }

    @RequestMapping(value = "/student/delete/{id}", method = RequestMethod.DELETE)
    public EntityResponse<User> delete(@PathVariable Long id) {
        studentService.delete(id);
        return new EntityResponse<>(HttpStatus.OK,"delete successful",null);
    }
}
