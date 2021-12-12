package vn.isofh.jpa.repository;

import vn.isofh.jpa.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card,Long> {
}
