package backend11.backend.domain.attendance.dto;

import java.time.LocalDate;
import java.util.List;

public record AttendanceLookupResponse(
        LocalDate date,
        String scope,
        int totalCount,
        int attendedCount,
        int absentCount,
        List<AttendanceStudentResponse> students
) {
}
