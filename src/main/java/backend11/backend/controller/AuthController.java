package backend11.backend.controller;

import backend11.backend.dto.LoginRequest;
import backend11.backend.dto.SignupRequest;
import backend11.backend.dto.TokenResponse;
import backend11.backend.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "인증", description = "회원가입과 로그인 API")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    @Operation(summary = "회원가입", description = "학생 정보를 등록하고 생성된 회원 ID를 반환합니다.")
    public ResponseEntity<Long> signup(@RequestBody SignupRequest request) {
        Long memberId = authService.signup(request);
        return ResponseEntity.ok(memberId);
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "아이디와 비밀번호를 확인한 뒤 JWT accessToken을 반환합니다.")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
