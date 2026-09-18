package backend11.backend.controller;

import backend11.backend.config.JwtTokenProvider;
import backend11.backend.config.SecurityConfig;
import backend11.backend.domain.Attendance;
import backend11.backend.dto.AttendanceRequest;
import backend11.backend.dto.LoginRequest;
import backend11.backend.dto.TokenResponse;
import backend11.backend.service.AttendanceLookupService;
import backend11.backend.service.AttendanceService;
import backend11.backend.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticatedApiControllerTest {

    @Mock
    private AuthService authService;

    @Mock
    private AttendanceService attendanceService;

    @Mock
    private AttendanceLookupService attendanceLookupService;

    @InjectMocks
    private AuthController authController;

    @InjectMocks
    private AttendanceController attendanceController;

    @Test
    void 로그인은_생성된_액세스_토큰을_응답한다() {
        LoginRequest request = mock(LoginRequest.class);
        TokenResponse tokenResponse = new TokenResponse("access-token");
        when(authService.login(request)).thenReturn(tokenResponse);

        ResponseEntity<TokenResponse> response = authController.login(request);

        assertThat(response.getBody()).isSameAs(tokenResponse);
    }

    @Test
    void 출석은_URL의_ID가_아닌_인증된_사용자명으로_처리한다() {
        Authentication authentication = mock(Authentication.class);
        AttendanceRequest request = mock(AttendanceRequest.class);
        Attendance attendance = mock(Attendance.class);
        when(authentication.getName()).thenReturn("student");
        when(request.getType()).thenReturn("NORMAL");
        when(request.getPlace()).thenReturn("LAB-1");
        when(attendanceService.checkIn("student", "NORMAL", "LAB-1")).thenReturn(attendance);

        ResponseEntity<Attendance> response = attendanceController.checkIn(authentication, request);

        assertThat(response.getBody()).isSameAs(attendance);
        verify(attendanceService).checkIn("student", "NORMAL", "LAB-1");
    }

    @Test
    void 퇴실은_인증된_사용자명으로_처리한다() {
        Authentication authentication = mock(Authentication.class);
        Attendance attendance = mock(Attendance.class);
        when(authentication.getName()).thenReturn("student");
        when(attendanceService.checkOut("student")).thenReturn(attendance);

        ResponseEntity<Attendance> response = attendanceController.checkOut(authentication);

        assertThat(response.getBody()).isSameAs(attendance);
        verify(attendanceService).checkOut("student");
    }

    @Test
    void 개발용_프론트엔드_주소만_CORS로_허용한다() {
        SecurityConfig securityConfig = new SecurityConfig(mock(JwtTokenProvider.class));
        CorsConfigurationSource source = securityConfig.corsConfigurationSource();
        CorsConfiguration configuration = source.getCorsConfiguration(
                new MockHttpServletRequest("OPTIONS", "/api/auth/login")
        );

        assertThat(configuration).isNotNull();
        assertThat(configuration.checkOrigin("http://127.0.0.1:5500"))
                .isEqualTo("http://127.0.0.1:5500");
        assertThat(configuration.checkOrigin("http://localhost:5500"))
                .isEqualTo("http://localhost:5500");
        assertThat(configuration.checkOrigin("https://untrusted.example"))
                .isNull();
        assertThat(configuration.getAllowedMethods())
                .containsExactlyInAnyOrder("GET", "POST", "PUT", "DELETE", "OPTIONS");
        assertThat(configuration.getAllowedHeaders())
                .containsExactlyInAnyOrder("Authorization", "Content-Type", "Accept");
        assertThat(configuration.getAllowCredentials()).isTrue();
    }
}
