package vn.isofh.userdetailservice.service;

import vn.isofh.userdetailservice.model.Role;
import vn.isofh.userdetailservice.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }
    public Role getRole(Long id){
        return roleRepository.getOne(id);
    }
    public List<Role> getAll(){ return roleRepository.findAll();}
    public Role getRoleWithName(String name){
        return roleRepository.findByName(name);
    }

}
