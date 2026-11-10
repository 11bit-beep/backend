package backend11.backend.attendance.dto;

import backend11.backend.attendance.domain.AttendanceStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record AttendanceLookupResponse(
        LocalDate date,
        String scope,
        int totalCount,
        int attendedCount,
        int absentCount,
        List<AttendanceStudentResponse> students
) {
    @Schema(name = "AttendanceStudentResponse")
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
}
