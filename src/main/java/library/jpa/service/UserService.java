package library.jpa.service;

import library.exception.exception.ApiRequestException;
import library.jpa.entity.User;
import library.jpa.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class UserService {
    private final UserRepository studentRepo;

    @Autowired
    public UserService(UserRepository studentRepo) {
        this.studentRepo = studentRepo;
    }

    public List<User> showStudent(){
        return studentRepo.findAll();
    }
    public User getByID(Long id){
        if (!isExist(id))
            throw new ApiRequestException("this id is not exist");
        return studentRepo.getOne(id);
    }

    public List<User> findbyname(String name){
        return studentRepo.findByNameContains(name);
    }
    public List<User> findbyphone(String phone){
        return studentRepo.findByPhoneContains(phone);
    }
//    public List<Student> findbygender(Boolean gender){
//        return studentRepo.findByGenderContains(gender);
//    }

    public boolean isExist(Long id){return studentRepo.existsById(id);}

    public void add(User student){
        if (student.getName()== null){
            throw new ApiRequestException("name of student cant null. enter name");
        }
        if (student.getPhone()==null){
            throw new ApiRequestException("phone of student cant null. enter mssv");
        }


        studentRepo.save(student);
    }
    public User updateById(User student, Long id){
        if (student.getName()== null){
            throw new ApiRequestException("name of student cant null. enter name");
        }
        if (id == null){
            throw new ApiRequestException("id cannot null");
        }
        User buffer = getByID(id);
        return buffer;
    }
    public void delete(Long id){
        if(!isExist(id)){
            throw new ApiRequestException("khong ton tai sinh vien co id = " + id);
        }
        studentRepo.deleteById(id);
    }
}
