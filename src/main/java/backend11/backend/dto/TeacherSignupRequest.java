package backend11.backend.dto;

import jakarta.validation.constraints.NotBlank;

public record TeacherSignupRequest(
        @NotBlank(message = "이름을 입력해 주세요.")
        String name,

        @NotBlank(message = "아이디를 입력해 주세요.")
        String username,

        @NotBlank(message = "비밀번호를 입력해 주세요.")
        String password,

        @NotBlank(message = "교사 코드를 입력해 주세요.")
        String teacherCode
) {
}
