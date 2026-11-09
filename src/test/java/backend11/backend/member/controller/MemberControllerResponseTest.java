package backend11.backend.member.controller;

import backend11.backend.member.domain.Member;
import backend11.backend.member.dto.MemberProfileResponse;
import backend11.backend.member.dto.UpdateMemberRequest;
import backend11.backend.member.service.MemberService;
import backend11.backend.user.domain.Role;
import backend11.backend.user.domain.User;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberControllerResponseTest {

    @Mock
    private MemberService memberService;

    @InjectMocks
    private MemberController memberController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void 내_정보_조회_응답에_비밀번호가_포함되지_않는다() throws Exception {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("student");
        when(memberService.getMemberByUsername("student")).thenReturn(member());

        MemberProfileResponse response = memberController.getMyMember(authentication).getBody();
        String json = objectMapper.writeValueAsString(response);

        assertThat(json).contains("\"username\":\"student\"", "\"name\":\"학생\"");
        assertThat(json).doesNotContain("password", "secret-password");
    }

    @Test
    void 내_정보_수정_응답에도_비밀번호가_포함되지_않는다() throws Exception {
        Authentication authentication = mock(Authentication.class);
        UpdateMemberRequest request = mock(UpdateMemberRequest.class);
        when(authentication.getName()).thenReturn("student");
        when(request.name()).thenReturn("학생");
        when(request.grade()).thenReturn(1);
        when(request.studentClass()).thenReturn(2);
        when(request.number()).thenReturn(3);
        when(memberService.updateMemberByUsername("student", "학생", 1, 2, 3))
                .thenReturn(member());

        MemberProfileResponse response = memberController.updateMyMember(authentication, request).getBody();
        String json = objectMapper.writeValueAsString(response);

        assertThat(json).contains("\"username\":\"student\"", "\"studentClass\":2");
        assertThat(json).doesNotContain("password", "secret-password");
    }

    private Member member() {
        User user = User.builder()
                .username("student")
                .password("secret-password")
                .role(Role.STUDENT)
                .build();
        return Member.builder()
                .user(user)
                .name("학생")
                .grade(1)
                .studentClass(2)
                .number(3)
                .build();
    }
}
