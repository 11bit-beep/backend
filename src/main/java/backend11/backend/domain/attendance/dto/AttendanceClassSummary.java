package backend11.backend.domain.attendance.dto;

public record AttendanceClassSummary(
        int grade,
        int studentClass,
        int totalCount,
        int attendedCount,
        int absentCount
) {
}
