package backend11.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttendanceRequest {
    @Schema(description = "출석 유형", example = "출석")
    private String type;

    @Schema(description = "출석 장소", example = "교실")
    private String place;
}
