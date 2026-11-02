package backend11.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateMemberRequest(
        @NotBlank(message = "이름을 입력해 주세요.")
        @Schema(description = "학생 이름", example = "김출석")
        String name,

        @NotNull(message = "학년을 입력해 주세요.")
        @Min(value = 1, message = "학년은 1 이상이어야 합니다.")
        @Schema(description = "학년", example = "2")
        Integer grade,

        @NotNull(message = "반을 입력해 주세요.")
        @Min(value = 1, message = "반은 1 이상이어야 합니다.")
        @Schema(description = "반", example = "3")
        Integer studentClass,

        @NotNull(message = "번호를 입력해 주세요.")
        @Min(value = 1, message = "번호는 1 이상이어야 합니다.")
        @Schema(description = "번호", example = "15")
        Integer number
) {
}
