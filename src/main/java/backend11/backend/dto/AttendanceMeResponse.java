package backend11.backend.dto;

import backend11.backend.domain.Attendance;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record AttendanceMeResponse(
        LocalDate date,
        AttendanceStatus status,
        LocalDateTime checkInAt,
        LocalDateTime checkOutAt,
        String type,
        String place
) {
    public static AttendanceMeResponse from(LocalDate date, Attendance attendance) {
        if (attendance == null) {
            return new AttendanceMeResponse(date, AttendanceStatus.ABSENT, null, null, null, null);
        }

        AttendanceStatus status = attendance.getCheckOutAt() == null
                ? AttendanceStatus.CHECKED_IN
                : AttendanceStatus.CHECKED_OUT;

        return new AttendanceMeResponse(
                date,
                status,
                attendance.getCheckInAt(),
                attendance.getCheckOutAt(),
                attendance.getType(),
                attendance.getPlace()
        );
    }
}
