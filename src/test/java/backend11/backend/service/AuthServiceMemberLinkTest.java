package backend11.backend.service;

import backend11.backend.config.JwtTokenProvider;
import backend11.backend.domain.Member;
import backend11.backend.domain.User;
import backend11.backend.dto.SignupRequest;
import backend11.backend.repository.MemberRepository;
import backend11.backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceMemberLinkTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private AuthService authService;

    @Test
    void 학생_회원가입은_저장한_User를_Member에_연결한다() {
        SignupRequest request = org.mockito.Mockito.mock(SignupRequest.class);
        when(request.getUsername()).thenReturn("student");
        when(request.getPassword()).thenReturn("password");
        when(request.getName()).thenReturn("학생");
        when(request.getGrade()).thenReturn(1);
        when(request.getStudentClass()).thenReturn(2);
        when(request.getNumber()).thenReturn(3);
        when(userRepository.findByUsername("student")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password")).thenReturn("encoded-password");
        when(memberRepository.save(any(Member.class))).thenAnswer(invocation -> invocation.getArgument(0));

        authService.signup(request);

        ArgumentCaptor<User> savedUser = ArgumentCaptor.forClass(User.class);
        ArgumentCaptor<Member> savedMember = ArgumentCaptor.forClass(Member.class);
        verify(userRepository).save(savedUser.capture());
        verify(memberRepository).save(savedMember.capture());
        assertThat(savedMember.getValue().getUser()).isSameAs(savedUser.getValue());
        assertThat(savedUser.getValue().getPassword()).isEqualTo("encoded-password");
        assertThat(savedMember.getValue().getUsername()).isEqualTo("student");
    }
}
