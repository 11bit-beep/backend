package backend11.backend;

import backend11.backend.dto.SignupRequest;
import backend11.backend.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import backend11.backend.exception.CustomException;
import backend11.backend.exception.ErrorCode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class StudentSignupIntegrationTest {

    @Autowired
    private AuthService authService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void 새_아이디라도_같은_학생이면_구분된_오류를_반환한다() throws Exception {
        Long firstId = authService.signup(request("signup-student-a", 1));

        assertThat(firstId).isNotNull();
        assertThatThrownBy(() -> authService.signup(request("signup-student-b", 1)))
                .isInstanceOfSatisfying(CustomException.class, exception ->
                        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.MEMBER_IDENTITY_ALREADY_REGISTERED));

        assertThat(authService.signup(request("signup-student-c", 2))).isNotNull();
    }

    private SignupRequest request(String username, int number) throws Exception {
        return objectMapper.readValue("""
                {"name":"가입검증학생","username":"%s","password":"password1234",
                 "grade":1,"studentClass":1,"number":%d}
                """.formatted(username, number), SignupRequest.class);
    }
}
