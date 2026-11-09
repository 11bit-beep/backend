package backend11.backend.service;

import backend11.backend.domain.Member;
import backend11.backend.domain.Role;
import backend11.backend.domain.User;
import backend11.backend.dto.LoginRequest;
import backend11.backend.dto.SignupRequest;
import backend11.backend.dto.TeacherSignupRequest;
import backend11.backend.dto.TokenResponse;
import backend11.backend.exception.CustomException;
import backend11.backend.exception.ErrorCode;
import backend11.backend.repository.MemberRepository;
import backend11.backend.repository.UserRepository;
import backend11.backend.security.TokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    private final String teacherCode;

    public AuthService(UserRepository userRepository,
                       MemberRepository memberRepository,
                       PasswordEncoder passwordEncoder,
                       TokenProvider tokenProvider,
                       @Value("${app.teacher-code}") String teacherCode) {
        this.userRepository = userRepository;
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
        this.teacherCode = teacherCode;
    }

    // 기존 학생 회원가입
    @Transactional
    public Long signup(SignupRequest request) {
        checkDuplicateUsername(request.username());
        if (memberRepository.existsByGradeAndStudentClassAndNumberAndName(
                request.grade(), request.studentClass(), request.number(), request.name())) {
            throw new CustomException(ErrorCode.MEMBER_IDENTITY_ALREADY_REGISTERED);
        }

        String encodedPassword =
                passwordEncoder.encode(request.password());

        User user = User.builder()
                .username(request.username())
                .password(encodedPassword)
                .role(Role.STUDENT)
                .build();

        userRepository.save(user);

        Member member = Member.builder()
                .user(user)
                .name(request.name())
                .grade(request.grade())
                .studentClass(request.studentClass())
                .number(request.number())
                .build();

        return memberRepository.save(member).getId();
    }

    // 신규 선생님 회원가입
    @Transactional
    public Long teacherSignup(TeacherSignupRequest request) {
        if (!teacherCode.equals(request.teacherCode())) {
            throw new CustomException(ErrorCode.INVALID_TEACHER_CODE);
        }

        checkDuplicateUsername(request.username());

        User teacher = User.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .name(request.name())
                .role(Role.TEACHER)
                .build();

        return userRepository.save(teacher).getId();
    }

    // 학생과 선생님 공통 로그인
    public TokenResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new CustomException(ErrorCode.USERNAME_NOT_FOUND));

        if (!passwordEncoder.matches(
                request.password(), user.getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_MISMATCH);
        }

        String accessToken = tokenProvider.createToken(
                user.getUsername(),
                user.getRole()
        );

        return new TokenResponse(accessToken, user.getRole().getKey());
    }

    private void checkDuplicateUsername(String username) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new CustomException(ErrorCode.USERNAME_ALREADY_EXISTS);
        }
    }
}
