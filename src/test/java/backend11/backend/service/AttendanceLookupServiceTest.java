package backend11.backend.service;

import backend11.backend.domain.Attendance;
import backend11.backend.domain.Member;
import backend11.backend.domain.Role;
import backend11.backend.domain.User;
import backend11.backend.dto.AttendanceLookupResponse;
import backend11.backend.dto.AttendanceStatus;
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
        attendanceRepository.save(new Attendance(otherClassUser, "NORMAL", "LAB-1"));
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
                "LAB-1",
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
        assertThat(labTwo.totalCount()).isZero();
        assertThat(labTwo.attendedCount()).isZero();
        assertThat(labTwo.absentCount()).isZero();
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
