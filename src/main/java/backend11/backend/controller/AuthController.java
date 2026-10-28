package backend11.backend.controller;

import backend11.backend.dto.LoginRequest;
import backend11.backend.dto.SignupRequest;
import backend11.backend.dto.TeacherSignupRequest;
import backend11.backend.dto.TokenResponse;
import backend11.backend.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "인증", description = "회원가입과 로그인 API")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    @Operation(summary = "학생 회원가입")
    public ResponseEntity<Long> signup(
            @Valid @RequestBody SignupRequest request) {
        return ResponseEntity.ok(authService.signup(request));
    }

    @PostMapping("/teacher/signup")
    @Operation(summary = "선생님 회원가입")
    public ResponseEntity<Long> teacherSignup(
            @Valid @RequestBody TeacherSignupRequest request) {
        return ResponseEntity.ok(authService.teacherSignup(request));
    }

    @PostMapping("/login")
    @Operation(summary = "공통 로그인")
    public ResponseEntity<TokenResponse> login(
            @Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
