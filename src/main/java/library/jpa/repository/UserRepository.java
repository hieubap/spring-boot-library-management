package library.jpa.repository;


import library.jpa.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Long> {

    public List<User> findByNameContains(String name);
    public List<User> findByPhoneContains(String name);
    public List<User> findByGenderContains(Boolean name);
}
