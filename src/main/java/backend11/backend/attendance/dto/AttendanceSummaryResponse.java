package backend11.backend.attendance.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;

public record AttendanceSummaryResponse(
        LocalDate date,
        List<AttendanceClassSummary> classes,
        List<AttendancePlaceSummary> places
) {
    @Schema(name = "AttendanceClassSummary")
    public record AttendanceClassSummary(
            int grade,
            int studentClass,
            int totalCount,
            int attendedCount,
            int absentCount
    ) {
    }

    @Schema(name = "AttendancePlaceSummary")
    public record AttendancePlaceSummary(
            String place,
            int attendedCount
    ) {
    }
}
