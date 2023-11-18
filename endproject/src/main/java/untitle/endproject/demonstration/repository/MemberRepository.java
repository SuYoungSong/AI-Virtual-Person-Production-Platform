package untitle.endproject.demonstration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import untitle.endproject.demonstration.domain.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member,String> {

}