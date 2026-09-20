package backend11.backend.service;

import backend11.backend.domain.Attendance;
import backend11.backend.domain.Member;
import backend11.backend.domain.Role;
import backend11.backend.domain.User;
import backend11.backend.dto.AttendanceLookupResponse;
import backend11.backend.dto.AttendanceStatus;
import backend11.backend.dto.AttendanceSummaryResponse;
import backend11.backend.repository.AttendanceRepository;
import backend11.backend.repository.MemberRepository;
import backend11.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AttendanceLookupServiceTest {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private UserRepository userRepository;

    private AttendanceLookupService attendanceLookupService;

    @BeforeEach
    void setUp() {
        attendanceLookupService = new AttendanceLookupService(attendanceRepository);

        saveMember("present", "출석 학생", 1);
        saveMember("absent", "미출석 학생", 2);

        User presentUser = userRepository.save(User.builder()
                .username("present")
                .password("password")
                .build());
        attendanceRepository.save(new Attendance(presentUser, "NORMAL", "LAB-1"));

        saveMember("other-class", "다른 반 출석 학생", 1, 3, 1);
        User otherClassUser = userRepository.save(User.builder()
                .username("other-class")
                .password("password")
                .build());
        // 예전에 다른 표기로 저장된 데이터도 같은 실로 조회되어야 한다.
        attendanceRepository.save(new Attendance(otherClassUser, "NORMAL", "lab 1실"));
    }

    @Test
    void 반별_조회는_출석_기록이_없는_학생도_미출석으로_반환한다() {
        AttendanceLookupResponse response = attendanceLookupService.getByClass(
                1,
                2,
                LocalDate.now()
        );

        assertThat(response.totalCount()).isEqualTo(2);
        assertThat(response.attendedCount()).isEqualTo(1);
        assertThat(response.absentCount()).isEqualTo(1);
        assertThat(response.students())
                .extracting(student -> student.status())
                .containsExactly(AttendanceStatus.CHECKED_IN, AttendanceStatus.ABSENT);
    }

    @Test
    void 실별_조회는_반과_관계없이_해당_실에_출석한_학생만_반환한다() {
        AttendanceLookupResponse labOne = attendanceLookupService.getByPlace(
                "LAB1",
                LocalDate.now()
        );
        AttendanceLookupResponse labTwo = attendanceLookupService.getByPlace(
                "LAB-2",
                LocalDate.now()
        );

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
        AttendanceSummaryResponse response = attendanceLookupService.getSummary(LocalDate.now());

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

    private void saveMember(String username, String name, int number) {
        saveMember(username, name, 1, 2, number);
    }

    private void saveMember(
            String username,
            String name,
            int grade,
            int studentClass,
            int number
    ) {
        memberRepository.save(Member.builder()
                .username(username)
                .password("password")
                .name(name)
                .grade(grade)
                .studentClass(studentClass)
                .number(number)
                .role(Role.USER)
                .build());
    }
}
