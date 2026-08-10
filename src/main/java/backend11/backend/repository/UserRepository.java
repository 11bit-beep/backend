package backend11.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import backend11.backend.domain.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
