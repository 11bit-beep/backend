package backend11.backend.repository;

import backend11.backend.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUsername(String username);

    boolean existsByUsername(String username);


    boolean existsByGradeAndStudentClassAndNumberAndName(
            int grade, int studentClass, int number, String name);

    Optional<Member> findByGradeAndStudentClassAndNumberAndName(
            int grade, int studentClass, int number, String name);
}