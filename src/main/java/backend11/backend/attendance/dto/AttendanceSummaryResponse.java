package backend11.backend.attendance.dto;

import java.time.LocalDate;
import java.util.List;

public record AttendanceSummaryResponse(
        LocalDate date,
        List<AttendanceClassSummary> classes,
        List<AttendancePlaceSummary> places
) {
}
