package backend11.backend.service;

import backend11.backend.dto.AttendanceJoinRow;
import backend11.backend.dto.AttendanceClassSummary;
import backend11.backend.dto.AttendanceLookupResponse;
import backend11.backend.dto.AttendancePlaceSummary;
import backend11.backend.dto.AttendanceStatus;
import backend11.backend.dto.AttendanceStudentResponse;
import backend11.backend.dto.AttendanceSummaryResponse;
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

    private static final int SUMMARY_GRADE = 1;
    private static final List<Integer> SUMMARY_CLASSES = List.of(1, 2, 3, 4);
    private static final List<String> SUMMARY_PLACES = List.of("LAB-1", "LAB-2", "LAB-3");

    private final AttendanceRepository attendanceRepository;

    public AttendanceSummaryResponse getSummary(LocalDate date) {
        LocalDate lookupDate = date == null ? LocalDate.now() : date;

        List<AttendanceClassSummary> classes = SUMMARY_CLASSES.stream()
                .map(studentClass -> toClassSummary(
                        SUMMARY_GRADE,
                        studentClass,
                        getByClass(SUMMARY_GRADE, studentClass, lookupDate)
                ))
                .toList();

        List<AttendancePlaceSummary> places = SUMMARY_PLACES.stream()
                .map(place -> new AttendancePlaceSummary(
                        place,
                        getByPlace(place, lookupDate).attendedCount()
                ))
                .toList();

        return new AttendanceSummaryResponse(lookupDate, classes, places);
    }

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
            String place,
            LocalDate date
    ) {
        if (place == null || place.isBlank()) {
            throw new IllegalArgumentException("출석 실을 입력해 주세요.");
        }

        LocalDate lookupDate = date == null ? LocalDate.now() : date;
        String normalizedPlace = place.trim();
        List<AttendanceJoinRow> rows = attendanceRepository.findPlaceAttendanceRows(
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

    private AttendanceClassSummary toClassSummary(
            int grade,
            int studentClass,
            AttendanceLookupResponse response
    ) {
        return new AttendanceClassSummary(
                grade,
                studentClass,
                response.totalCount(),
                response.attendedCount(),
                response.absentCount()
        );
    }

    private void validateClassScope(int grade, int studentClass) {
        if (grade <= 0 || studentClass <= 0) {
            throw new IllegalArgumentException("학년과 반은 1 이상이어야 합니다.");
        }
    }
}
