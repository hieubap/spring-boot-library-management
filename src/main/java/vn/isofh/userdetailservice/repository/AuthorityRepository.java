package vn.isofh.userdetailservice.repository;

import vn.isofh.userdetailservice.model.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority,Long> {
}
