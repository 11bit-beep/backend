package backend11.backend.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import backend11.backend.domain.user.entity.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}