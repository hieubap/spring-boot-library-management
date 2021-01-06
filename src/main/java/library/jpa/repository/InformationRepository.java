package library.jpa.repository;


import library.userdetailservice.model.Information;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InformationRepository extends JpaRepository<Information,Long> {

    public List<Information> findByNameContains(String name);
    public List<Information> findByPhoneContains(String name);
    public List<Information> findByGenderContains(Boolean name);
}
