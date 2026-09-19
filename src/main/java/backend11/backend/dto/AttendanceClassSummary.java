package backend11.backend.dto;

public record AttendanceClassSummary(
        int grade,
        int studentClass,
        int totalCount,
        int attendedCount,
        int absentCount
) {
}
