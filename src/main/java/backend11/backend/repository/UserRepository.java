package backend11.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import backend11.backend.domain.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}