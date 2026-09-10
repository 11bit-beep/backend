package backend11.backend.service;

import backend11.backend.config.JwtTokenProvider;
import backend11.backend.domain.Member;
import backend11.backend.domain.Role;
import backend11.backend.domain.User;
import backend11.backend.dto.LoginRequest;
import backend11.backend.dto.SignupRequest;
import backend11.backend.dto.TokenResponse;
import backend11.backend.repository.MemberRepository;
import backend11.backend.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public Long signup(SignupRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()
                || memberRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 아이디 입니다.");
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User user = User.builder()
                .username(request.getUsername())
                .password(encodedPassword)
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
    public TokenResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이디 입니다."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        String accessToken = jwtTokenProvider.createToken(user.getUsername());
        return new TokenResponse(accessToken);
    }

}
