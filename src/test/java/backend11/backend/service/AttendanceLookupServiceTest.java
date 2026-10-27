package backend11.backend.service;

import backend11.backend.dto.AttendanceJoinRow;
import backend11.backend.dto.AttendanceLookupResponse;
import backend11.backend.dto.AttendanceStatus;
import backend11.backend.dto.AttendanceSummaryResponse;
import backend11.backend.repository.AttendanceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AttendanceLookupServiceTest {

    @Mock
    private AttendanceRepository attendanceRepository;

    @InjectMocks
    private AttendanceLookupService attendanceLookupService;

    private final LocalDate today = LocalDate.now();

    // 출석한 학생 (출석 기록 있음, 퇴실 안 함)
    private AttendanceJoinRow attendedRow(Long memberId, String name, int grade,
                                          int studentClass, int number, String place) {
        return new AttendanceJoinRow(
                memberId, name, grade, studentClass, number,
                memberId, LocalDateTime.now(), null, "NORMAL", place
        );
    }

    // 미출석 학생 (LEFT JOIN 결과라 출석 컬럼이 전부 null)
    private AttendanceJoinRow absentRow(Long memberId, String name, int grade,
                                        int studentClass, int number) {
        return new AttendanceJoinRow(
                memberId, name, grade, studentClass, number,
                null, null, null, null, null
        );
    }

    @Test
    void 반별_조회는_출석_기록이_없는_학생도_미출석으로_반환한다() {
        when(attendanceRepository.findClassAttendanceRows(1, 2, today)).thenReturn(List.of(
                attendedRow(1L, "출석 학생", 1, 2, 1, "LAB-1"),
                absentRow(2L, "미출석 학생", 1, 2, 2)
        ));

        AttendanceLookupResponse response = attendanceLookupService.getByClass(1, 2, today);

        assertThat(response.totalCount()).isEqualTo(2);
        assertThat(response.attendedCount()).isEqualTo(1);
        assertThat(response.absentCount()).isEqualTo(1);
        assertThat(response.students())
                .extracting(student -> student.status())
                .containsExactly(AttendanceStatus.CHECKED_IN, AttendanceStatus.ABSENT);
    }

    @Test
    void 실별_조회는_반과_관계없이_해당_실에_출석한_학생만_반환한다() {
        // 서비스가 "LAB1"을 정규화해서 "LAB1" 키로 레포지토리를 호출한다고 가정
        when(attendanceRepository.findPlaceAttendanceRows(eq("LAB1"), eq(today))).thenReturn(List.of(
                attendedRow(1L, "출석 학생", 1, 2, 1, "LAB-1"),
                // 예전에 다른 표기로 저장된 데이터도 같은 실로 조회되어야 한다.
                attendedRow(3L, "다른 반 출석 학생", 1, 3, 1, "lab 1실")
        ));
        when(attendanceRepository.findPlaceAttendanceRows(eq("LAB2"), eq(today))).thenReturn(List.of());

        AttendanceLookupResponse labOne = attendanceLookupService.getByPlace("LAB1", today);
        AttendanceLookupResponse labTwo = attendanceLookupService.getByPlace("LAB-2", today);

        assertThat(labOne.totalCount()).isEqualTo(2);
        assertThat(labOne.attendedCount()).isEqualTo(2);
        assertThat(labOne.absentCount()).isZero();
        assertThat(labOne.students())
                .extracting(student -> student.name())
                .containsExactly("출석 학생", "다른 반 출석 학생");
        assertThat(labOne.students())
                .extracting(student -> student.place())
                .containsOnly("LAB-1");
        assertThat(labTwo.totalCount()).isZero();
        assertThat(labTwo.attendedCount()).isZero();
        assertThat(labTwo.absentCount()).isZero();
    }

    @Test
    void 전체_요약은_1학년_1반부터_4반과_세_개의_실을_반환한다() {
        // 기본값: 모든 반/실은 빈 목록
        lenient().when(attendanceRepository.findClassAttendanceRows(anyInt(), anyInt(), any()))
                .thenReturn(List.of());
        lenient().when(attendanceRepository.findPlaceAttendanceRows(any(), any()))
                .thenReturn(List.of());

        // 2반: 출석 1명 + 미출석 1명
        when(attendanceRepository.findClassAttendanceRows(1, 2, today)).thenReturn(List.of(
                attendedRow(1L, "출석 학생", 1, 2, 1, "LAB-1"),
                absentRow(2L, "미출석 학생", 1, 2, 2)
        ));
        // 3반: 출석 1명
        when(attendanceRepository.findClassAttendanceRows(1, 3, today)).thenReturn(List.of(
                attendedRow(3L, "다른 반 출석 학생", 1, 3, 1, "lab 1실")
        ));
        // LAB-1: 2명 출석
        when(attendanceRepository.findPlaceAttendanceRows(eq("LAB1"), eq(today))).thenReturn(List.of(
                attendedRow(1L, "출석 학생", 1, 2, 1, "LAB-1"),
                attendedRow(3L, "다른 반 출석 학생", 1, 3, 1, "lab 1실")
        ));

        AttendanceSummaryResponse response = attendanceLookupService.getSummary(today);

        assertThat(response.classes())
                .extracting(summary -> summary.studentClass())
                .containsExactly(1, 2, 3, 4);
        assertThat(response.classes().get(0).totalCount()).isZero();
        assertThat(response.classes().get(1).totalCount()).isEqualTo(2);
        assertThat(response.classes().get(1).attendedCount()).isEqualTo(1);
        assertThat(response.classes().get(1).absentCount()).isEqualTo(1);
        assertThat(response.classes().get(2).totalCount()).isEqualTo(1);
        assertThat(response.classes().get(2).attendedCount()).isEqualTo(1);
        assertThat(response.classes().get(3).totalCount()).isZero();

        assertThat(response.places())
                .extracting(summary -> summary.place())
                .containsExactly("LAB-1", "LAB-2", "LAB-3");
        assertThat(response.places().get(0).attendedCount()).isEqualTo(2);
        assertThat(response.places().get(1).attendedCount()).isZero();
        assertThat(response.places().get(2).attendedCount()).isZero();
    }
}