package library.jpa.repository;

import library.jpa.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book,Long> {
    public int countByHeadBookId(Long id);
    public int countByHeadBookIdAndStatusIsNot(Long id,String status);
    public List<Book> findByHeadBookIdAndStatusContains(Long id,String status);
}