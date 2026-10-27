package backend11.backend.service;

import backend11.backend.security.TokenProvider;
import backend11.backend.domain.Member;
import backend11.backend.domain.Role;
import backend11.backend.domain.User;
import backend11.backend.dto.LoginRequest;
import backend11.backend.dto.SignupRequest;
import backend11.backend.dto.TeacherSignupRequest;
import backend11.backend.dto.TokenResponse;
import backend11.backend.repository.MemberRepository;
import backend11.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    @Value("${app.teacher-code}")
    private String teacherCode;

    // 기존 학생 회원가입
    @Transactional
    public Long signup(SignupRequest request) {
        checkDuplicateUsername(request.getUsername());

        String encodedPassword =
                passwordEncoder.encode(request.getPassword());

        User user = User.builder()
                .username(request.getUsername())
                .password(encodedPassword)
                .role(Role.STUDENT)
                .build();

        userRepository.save(user);

        Member member = Member.builder()
                .username(request.getUsername())
                .password(encodedPassword)
                .name(request.getName())
                .grade(request.getGrade())
                .studentClass(request.getStudentClass())
                .number(request.getNumber())
                .role(Role.USER)
                .build();

        return memberRepository.save(member).getId();
    }

    // 신규 선생님 회원가입
    @Transactional
    public Long teacherSignup(TeacherSignupRequest request) {
        if (request.getTeacherCode() == null
                || !teacherCode.equals(request.getTeacherCode())) {
            throw new IllegalArgumentException("교사 코드가 올바르지 않습니다.");
        }

        checkDuplicateUsername(request.getUsername());

        User teacher = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .role(Role.TEACHER)
                .build();

        return userRepository.save(teacher).getId();
    }

    // 학생과 선생님 공통 로그인
    public TokenResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() ->
                        new IllegalArgumentException("존재하지 않는 아이디입니다."));

        if (!passwordEncoder.matches(
                request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException(
                    "비밀번호가 일치하지 않습니다.");
        }

        String accessToken = tokenProvider.createToken(
                user.getUsername(),
                user.getRole()
        );

        return new TokenResponse(accessToken, user.getRole().getKey());
    }

    private void checkDuplicateUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("아이디를 입력해주세요.");
        }

        if (userRepository.findByUsername(username).isPresent()
                || memberRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException(
                    "이미 존재하는 아이디입니다.");
        }
    }
}
