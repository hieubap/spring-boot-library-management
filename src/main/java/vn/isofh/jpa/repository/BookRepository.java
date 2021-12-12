package vn.isofh.jpa.repository;

import vn.isofh.jpa.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

//@Repository
public interface BookRepository extends JpaRepository<Book,Long> {
    public int countByHeadBookId(Long id);

    public int countByHeadBookIdAndStatusIsNot(Long id,String status);
    public List<Book> findByHeadBookIdAndStatusContains(Long id,String status);
}