package library.jpa.repository;

import library.jpa.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

//@Repository
public interface BookRepository extends JpaRepository<Book,Long> {
    public int countByHeadBookId(Long id);

    public int countByHeadBookIdAndStatusIsNot(Long id,String status);
    public List<Book> findByHeadBookIdAndStatusContains(Long id,String status);
}