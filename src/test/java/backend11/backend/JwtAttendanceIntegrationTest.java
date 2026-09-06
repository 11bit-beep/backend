package backend11.backend;

import backend11.backend.domain.User;
import backend11.backend.repository.AttendanceRepository;
import backend11.backend.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class JwtAttendanceIntegrationTest {

    private static final Pattern ACCESS_TOKEN_PATTERN =
            Pattern.compile("\\\"accessToken\\\"\\s*:\\s*\\\"([^\\\"]+)\\\"");

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final HttpClient httpClient = HttpClient.newHttpClient();

    @BeforeEach
    void setUp() {
        attendanceRepository.deleteAll();
        userRepository.deleteAll();
        userRepository.save(User.builder()
                .username("student")
                .password(passwordEncoder.encode("password"))
                .build());
    }

    @AfterEach
    void tearDown() {
        attendanceRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void 로그인_토큰이_있어야_본인_출석을_처리할_수_있다() throws Exception {
        HttpResponse<String> anonymousResponse = sendCheckIn(null);
        assertThat(anonymousResponse.statusCode()).isIn(401, 403);

        HttpRequest loginRequest = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl("/api/auth/login")))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(
                        "{\"username\":\"student\",\"password\":\"password\"}"
                ))
                .build();
        HttpResponse<String> loginResponse = httpClient.send(
                loginRequest,
                HttpResponse.BodyHandlers.ofString()
        );

        assertThat(loginResponse.statusCode()).isEqualTo(200);
        Matcher tokenMatcher = ACCESS_TOKEN_PATTERN.matcher(loginResponse.body());
        assertThat(tokenMatcher.find()).isTrue();

        HttpResponse<String> authenticatedResponse = sendCheckIn(tokenMatcher.group(1));

        assertThat(authenticatedResponse.statusCode()).isEqualTo(200);
        assertThat(attendanceRepository.count()).isEqualTo(1);
        assertThat(attendanceRepository.findAll().getFirst().getUser().getUsername())
                .isEqualTo("student");
    }

    private HttpResponse<String> sendCheckIn(String accessToken) throws Exception {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl("/api/attendance/check_in")))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(
                        "{\"type\":\"NORMAL\",\"place\":\"LAB-1\"}"
                ));

        if (accessToken != null) {
            requestBuilder.header("Authorization", "Bearer " + accessToken);
        }

        return httpClient.send(requestBuilder.build(), HttpResponse.BodyHandlers.ofString());
    }

    private String baseUrl(String path) {
        return "http://localhost:" + port + path;
    }
}
