package library.userdetailservice.repository;

import library.userdetailservice.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account,Long> {
    public boolean existsByUsername(String username);
    public boolean existsByUsernameAndPassword(String user,String password);

    public Account findByUsername(String username);
//    public Account findBy(Long id);

}