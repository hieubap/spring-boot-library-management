package vn.isofh.jpa.repository;

import vn.isofh.jpa.entity.HeadBook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HeadBookRepository extends JpaRepository<HeadBook,Long> {
    public List<HeadBook> findByNameContains(String data);
    public HeadBook findByNameContaining(String data);

    public HeadBook findByNameContainsAndPublisherContainsAndAuthorContains(String name,String publisher,String author);

//    @Query("select h from HeadBook h where h.author = ?1")
    public List<HeadBook> findByNameContainsAndPublisherContainsAndAuthorContaining(String name,String publisher,String author);

    public List<HeadBook> findByAuthorContains(String data);
    public List<HeadBook> findByPublisherContains(String data);
}
