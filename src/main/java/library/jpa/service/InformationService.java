package library.jpa.service;

import library.exception.exception.ApiRequestException;
import library.exception.exception.ExistException;
import library.exception.exception.NullException;
import library.userdetailservice.model.Information;
import library.jpa.repository.InformationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class InformationService {
    private final InformationRepository studentRepo;

    @Autowired
    public InformationService(InformationRepository studentRepo) {
        this.studentRepo = studentRepo;
    }

    public List<Information> getAllInformation() {
        return studentRepo.findAll();
    }

    public Information getByID(Long id) {
        if (isNotExist(id))
            throw new ExistException("this id is not exist");
        return studentRepo.getOne(id);
    }

    public boolean isNotExist(Long id) {
        return !studentRepo.existsById(id);
    }

    public void add(Information student) {
        if (student.getName() == null) {
            throw new ApiRequestException("name of student cant null. enter name");
        }
        if (student.getPhone() == null) {
            throw new ApiRequestException("phone of student cant null. enter mssv");
        }
        studentRepo.save(student);
    }

    public Information updateById(Information student, Long id) {
        if (student.getName() == null) {
            throw new NullException("name of student cant null. enter name");
        }
        if (id == null) {
            throw new NullException("id cannot null");
        }
        return getByID(id);
    }

    public void delete(Long id) {
        if (!isNotExist(id)) {
            throw new ApiRequestException("this Id Information '" + id + "' is not exists");
        }
        studentRepo.deleteById(id);
    }
}
