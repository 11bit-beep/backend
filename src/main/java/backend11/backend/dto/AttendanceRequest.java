package backend11.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttendanceRequest {
    @NotBlank(message = "출석 유형을 입력해 주세요.")
    @Schema(description = "출석 유형", example = "출석")
    private String type;

    @NotBlank(message = "출석 장소를 입력해 주세요.")
    @Schema(description = "출석 장소", example = "교실")
    private String place;
}
