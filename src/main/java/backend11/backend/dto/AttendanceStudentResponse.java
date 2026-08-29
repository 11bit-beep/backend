package backend11.backend.dto;

import java.time.LocalDateTime;

public record AttendanceStudentResponse(
        Long memberId,
        String name,
        int grade,
        int studentClass,
        int number,
        AttendanceStatus status,
        LocalDateTime checkInAt,
        LocalDateTime checkOutAt,
        String type,
        String place
) {
}
