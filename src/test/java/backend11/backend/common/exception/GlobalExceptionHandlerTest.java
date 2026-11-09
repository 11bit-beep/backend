package backend11.backend.common.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void 커스텀_예외는_에러_코드의_상태와_메시지로_응답한다() {
        ResponseEntity<Map<String, String>> response =
                handler.handleCustom(new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        assertThat(response.getStatusCode().value()).isEqualTo(404);
        assertThat(response.getBody())
                .containsEntry("code", "MEMBER_NOT_FOUND")
                .containsEntry("message", "회원을 찾을 수 없습니다.");
    }

    @Test
    void 출석_중복은_기존과_같은_400으로_응답한다() {
        ResponseEntity<Map<String, String>> response =
                handler.handleCustom(new CustomException(ErrorCode.ALREADY_CHECKED_IN));

        assertThat(response.getStatusCode().value()).isEqualTo(400);
        assertThat(response.getBody()).containsEntry("message", "오늘 이미 출석 처리되었습니다.");
    }
}
