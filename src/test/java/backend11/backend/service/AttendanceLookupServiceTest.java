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
    void 실별_조회는_해당_실에_출석한_기록만_출석으로_판정한다() {
        AttendanceLookupResponse labOne = attendanceLookupService.getByPlace(
                1,
                2,
                "LAB-1",
                LocalDate.now()
        );
        AttendanceLookupResponse labTwo = attendanceLookupService.getByPlace(
                1,
                2,
                "LAB-2",
                LocalDate.now()
        );

        assertThat(labOne.attendedCount()).isEqualTo(1);
        assertThat(labOne.absentCount()).isEqualTo(1);
        assertThat(labTwo.attendedCount()).isZero();
        assertThat(labTwo.absentCount()).isEqualTo(2);
    }

    private void saveMember(String username, String name, int number) {
        memberRepository.save(Member.builder()
                .username(username)
                .password("password")
                .name(name)
                .grade(1)
                .studentClass(2)
                .number(number)
                .role(Role.USER)
                .build());
    }
}
