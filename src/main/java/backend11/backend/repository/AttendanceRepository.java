package backend11.backend.repository;

import backend11.backend.domain.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    Attendance findByUserIdAndDate(Long userId, LocalDate date);
}