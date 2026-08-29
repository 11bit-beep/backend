package backend11.backend.dto;

import java.time.LocalDateTime;

/**
 * 학생 명단과 출석 기록을 LEFT JOIN한 결과 한 행을 담는다.
 */
public record AttendanceJoinRow(
        Long memberId,
        String name,
        int grade,
        int studentClass,
        int number,
        Long attendanceId,
        LocalDateTime checkInAt,
        LocalDateTime checkOutAt,
        String type,
        String place
) {
}
