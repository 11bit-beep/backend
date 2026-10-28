package backend11.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TeacherSignupRequest {
    @NotBlank(message = "이름을 입력해 주세요.")
    private String name;
    @NotBlank(message = "아이디를 입력해 주세요.")
    private String username;
    @NotBlank(message = "비밀번호를 입력해 주세요.")
    private String password;
    @NotBlank(message = "교사 코드를 입력해 주세요.")
    private String teacherCode;
}
