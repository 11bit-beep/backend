package backend11.backend.service;

import backend11.backend.dto.AttendanceJoinRow;
import backend11.backend.dto.AttendanceLookupResponse;
import backend11.backend.dto.AttendanceStatus;
import backend11.backend.dto.AttendanceStudentResponse;
import backend11.backend.repository.AttendanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AttendanceLookupService {

    private final AttendanceRepository attendanceRepository;

    public AttendanceLookupResponse getByClass(int grade, int studentClass, LocalDate date) {
        validateClassScope(grade, studentClass);
        LocalDate lookupDate = date == null ? LocalDate.now() : date;

        List<AttendanceJoinRow> rows = attendanceRepository.findClassAttendanceRows(
                grade,
                studentClass,
                lookupDate
        );

        return createResponse(lookupDate, "CLASS", rows);
    }

    public AttendanceLookupResponse getByPlace(
            int grade,
            int studentClass,
            String place,
            LocalDate date
    ) {
        validateClassScope(grade, studentClass);
        if (place == null || place.isBlank()) {
            throw new IllegalArgumentException("출석 실을 입력해 주세요.");
        }

        LocalDate lookupDate = date == null ? LocalDate.now() : date;
        String normalizedPlace = place.trim();
        List<AttendanceJoinRow> rows = attendanceRepository.findPlaceAttendanceRows(
                grade,
                studentClass,
                normalizedPlace,
                lookupDate
        );

        return createResponse(lookupDate, "PLACE:" + normalizedPlace, rows);
    }

    private AttendanceLookupResponse createResponse(
            LocalDate date,
            String scope,
            List<AttendanceJoinRow> rows
    ) {
        Map<Long, AttendanceStudentResponse> studentsByMember = new LinkedHashMap<>();

        // 한 학생이 같은 날 여러 번 출석한 기존 데이터가 있어도 최신 기록 한 건만 노출한다.
        for (AttendanceJoinRow row : rows) {
            studentsByMember.putIfAbsent(row.memberId(), toStudentResponse(row));
        }

        List<AttendanceStudentResponse> students = List.copyOf(studentsByMember.values());
        int absentCount = (int) students.stream()
                .filter(student -> student.status() == AttendanceStatus.ABSENT)
                .count();

        return new AttendanceLookupResponse(
                date,
                scope,
                students.size(),
                students.size() - absentCount,
                absentCount,
                students
        );
    }

    private AttendanceStudentResponse toStudentResponse(AttendanceJoinRow row) {
        AttendanceStatus status;
        if (row.attendanceId() == null) {
            status = AttendanceStatus.ABSENT;
        } else if (row.checkOutAt() == null) {
            status = AttendanceStatus.CHECKED_IN;
        } else {
            status = AttendanceStatus.CHECKED_OUT;
        }

        return new AttendanceStudentResponse(
                row.memberId(),
                row.name(),
                row.grade(),
                row.studentClass(),
                row.number(),
                status,
                row.checkInAt(),
                row.checkOutAt(),
                row.type(),
                row.place()
        );
    }

    private void validateClassScope(int grade, int studentClass) {
        if (grade <= 0 || studentClass <= 0) {
            throw new IllegalArgumentException("학년과 반은 1 이상이어야 합니다.");
        }
    }
}
