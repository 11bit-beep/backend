package backend11.backend.service;

import backend11.backend.domain.Attendance;
import backend11.backend.domain.Member;
import backend11.backend.domain.Role;
import backend11.backend.domain.User;
import backend11.backend.dto.MemberProfileResponse;
import backend11.backend.repository.AttendanceRepository;
import backend11.backend.repository.MemberRepository;
import backend11.backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(MemberService.class)
class MemberAccountPersistenceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Test
    void 회원은_User를_참조하고_응답에_비밀번호를_담지_않는다() {
        User user = userRepository.save(User.builder()
                .username("student")
                .password("encoded-password")
                .role(Role.STUDENT)
                .build());
        memberRepository.save(Member.builder()
                .user(user)
                .name("학생")
                .grade(1)
                .studentClass(2)
                .number(3)
                .build());

        Member member = memberService.getMemberByUsername("student");
        MemberProfileResponse response = MemberProfileResponse.from(member);

        assertThat(member.getUser().getId()).isEqualTo(user.getId());
        assertThat(response.username()).isEqualTo("student");
        assertThat(response.role()).isEqualTo(Role.STUDENT);
        assertThat(MemberProfileResponse.class.getRecordComponents())
                .extracting(component -> component.getName())
                .doesNotContain("password", "user");
    }

    @Test
    void 탈퇴하면_회원과_로그인_계정과_출석_기록을_함께_삭제한다() {
        User user = userRepository.save(User.builder()
                .username("student")
                .password("encoded-password")
                .role(Role.STUDENT)
                .build());
        memberRepository.save(Member.builder()
                .user(user)
                .name("학생")
                .grade(1)
                .studentClass(2)
                .number(3)
                .build());
        attendanceRepository.save(new Attendance(user, "NORMAL", "LAB-1"));

        memberService.deleteMemberByUsername("student");
        userRepository.flush();

        assertThat(memberRepository.findByUser_Username("student")).isEmpty();
        assertThat(userRepository.findByUsername("student")).isEmpty();
        assertThat(attendanceRepository.count()).isZero();
    }
}
