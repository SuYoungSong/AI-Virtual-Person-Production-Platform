package untitle.endproject.demonstration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import untitle.endproject.demonstration.domain.WorkChar;

import java.util.List;

@Repository
public interface WorkCharRepository extends JpaRepository<WorkChar,String> {
    List<WorkChar> findByIdEquals(String id);
}