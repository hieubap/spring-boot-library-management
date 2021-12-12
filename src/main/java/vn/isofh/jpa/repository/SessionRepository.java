package vn.isofh.jpa.repository;

import vn.isofh.jpa.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Session,Long> {
//    public boolean isExistByIdBook(Long id);
}
