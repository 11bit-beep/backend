package backend11.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Getter
@NoArgsConstructor
public class SignupRequest {
    @Schema(description = "학생 이름", example = "김출석")
    private String name;

    @Schema(description = "로그인 아이디", example = "student01")
    private String username;

    @Schema(description = "비밀번호", example = "password1234")
    private String password;

    @Schema(description = "학년", example = "2")
    private int grade;

    @Schema(description = "반", example = "3")
    private int studentClass;

    @Schema(description = "번호", example = "15")
    private int number;
}
