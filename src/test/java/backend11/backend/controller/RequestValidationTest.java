package backend11.backend.controller;

import backend11.backend.exception.GlobalExceptionHandler;
import backend11.backend.service.AttendanceLookupService;
import backend11.backend.service.AttendanceService;
import backend11.backend.service.AuthService;
import backend11.backend.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class RequestValidationTest {

    @Mock
    private AuthService authService;

    @Mock
    private AttendanceService attendanceService;

    @Mock
    private AttendanceLookupService attendanceLookupService;

    @Mock
    private MemberService memberService;

    @InjectMocks
    private AuthController authController;

    @InjectMocks
    private AttendanceController attendanceController;

    @InjectMocks
    private MemberController memberController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        mockMvc = MockMvcBuilders.standaloneSetup(
                        authController, attendanceController, memberController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setValidator(validator)
                .build();
    }

    @Test
    void 학생_회원가입에서_빈_이름을_거절한다() throws Exception {
        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":" ","username":"student01","password":"password1234",
                                 "grade":2,"studentClass":3,"number":15}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("이름을 입력해 주세요."));

        verifyNoInteractions(authService);
    }

    @Test
    void 교사_회원가입에서_빈_교사_코드를_거절한다() throws Exception {
        mockMvc.perform(post("/api/auth/teacher/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"선생님","username":"teacher01","password":"password1234",
                                 "teacherCode":" "}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("교사 코드를 입력해 주세요."));

        verifyNoInteractions(authService);
    }

    @Test
    void 로그인에서_빈_아이디를_거절한다() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":" ","password":"password1234"}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("아이디를 입력해 주세요."));

        verifyNoInteractions(authService);
    }

    @Test
    void 회원_수정에서_누락된_번호를_거절한다() throws Exception {
        mockMvc.perform(put("/api/members/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"김출석","grade":2,"studentClass":3}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("번호를 입력해 주세요."));

        verifyNoInteractions(memberService);
    }

    @Test
    void 출석에서_빈_장소를_거절한다() throws Exception {
        mockMvc.perform(post("/api/attendance/check_in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"type":"NORMAL","place":" "}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("출석 장소를 입력해 주세요."));

        verifyNoInteractions(attendanceService);
    }
}
