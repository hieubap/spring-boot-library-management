package vn.isofh.jpa.controller;

import vn.isofh.jpa.responceEntity.EntityResponse;
import vn.isofh.security.Dao.FormAdminEdit;
import vn.isofh.security.Dao.InformationUserDaoResponse;
import vn.isofh.userdetailservice.service.RoleService;
import vn.isofh.userdetailservice.userdetail.UserDetailServiceAlter;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/library/jpa/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final RoleService roleService;
    private final UserDetailServiceAlter userDetailServices;

    public AdminController(RoleService roleService, UserDetailServiceAlter userDetailServiceAlter) {
        this.roleService = roleService;
        this.userDetailServices = userDetailServiceAlter;
    }

    // *** ok ***
    @GetMapping(value = "/role/list")
    public EntityResponse<Object> getRoleAll() {
        return new EntityResponse<>(HttpStatus.OK, "successful", roleService.getAll());
    }

    // *** ok ***
    @PutMapping(value = "/role/set")
    public EntityResponse<Object> adminSetRole(@RequestBody FormAdminEdit userDao) {
        InformationUserDaoResponse data = userDetailServices.adminSetRole(userDao);
        return new EntityResponse<>(HttpStatus.OK, "Set Role for user '" + data.getUsername() + "' is Successful", data);
    }

    // *** ok ***
    @GetMapping(value = "/user/list")
    public EntityResponse<Object> getUserAll() {
        return new EntityResponse<>(HttpStatus.OK, "Get List User Successful", userDetailServices.getAllAccount());
    }

    // *** ok ***
    @GetMapping(value = "/user/{id}")
    public EntityResponse<Object> getUserAll(@PathVariable Long id) {
        return new EntityResponse<>(HttpStatus.OK, "Get User by ID '" + id + "' Successful", userDetailServices.getByID(id));
    }
}
