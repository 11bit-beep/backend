package backend11.backend.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;          // 학생 ID
    private LocalDate date;       // 출석 날짜
    private LocalDateTime checkInAt;   // 출석 시간
    private LocalDateTime checkOutAt;  // 퇴실 시간

    // 출석
    public Attendance(Long userId) {
        this.userId = userId;
        this.date = LocalDate.now();
        this.checkInAt = LocalDateTime.now();
    }

    // 퇴실
    public void updateCheckOut() {
        this.checkOutAt = LocalDateTime.now();
    }
}

