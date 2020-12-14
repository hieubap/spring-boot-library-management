package library.jpa.repository;

import library.jpa.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Session,Long> {
//    public boolean isExistByIdBook(Long id);
}
