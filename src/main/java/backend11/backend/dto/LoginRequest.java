package backend11.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginRequest {
    @Schema(description = "로그인 아이디", example = "student01")
    private String username;

    @Schema(description = "비밀번호", example = "password1234")
    private String password;
}
