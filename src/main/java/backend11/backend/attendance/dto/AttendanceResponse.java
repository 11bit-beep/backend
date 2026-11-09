package backend11.backend.attendance.dto;

import backend11.backend.attendance.domain.Attendance;
import backend11.backend.user.domain.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record AttendanceResponse(
        Long id,
        UserSummary user,
        LocalDate date,
        LocalDateTime checkInAt,
        LocalDateTime checkOutAt,
        String type,
        String place
) {
    public static AttendanceResponse from(Attendance attendance) {
        User user = attendance.getUser();
        return new AttendanceResponse(
                attendance.getId(),
                new UserSummary(user.getId(), user.getUsername()),
                attendance.getDate(),
                attendance.getCheckInAt(),
                attendance.getCheckOutAt(),
                attendance.getType(),
                attendance.getPlace()
        );
    }

    public record UserSummary(Long id, String username) {
    }
}
