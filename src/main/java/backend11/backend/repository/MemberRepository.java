package backend11.backend.repository;

import backend11.backend.domain.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    @EntityGraph(attributePaths = "user")
    Optional<Member> findByUser_Username(String username);


    boolean existsByGradeAndStudentClassAndNumberAndName(
            int grade, int studentClass, int number, String name);

    Optional<Member> findByGradeAndStudentClassAndNumberAndName(
            int grade, int studentClass, int number, String name);
}
