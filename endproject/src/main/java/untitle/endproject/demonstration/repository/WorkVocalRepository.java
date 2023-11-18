package untitle.endproject.demonstration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import untitle.endproject.demonstration.domain.WorkVocal;

import java.util.List;

@Repository
public interface WorkVocalRepository extends JpaRepository<WorkVocal,String> {
    List<WorkVocal> findByIdEquals(String id);
}