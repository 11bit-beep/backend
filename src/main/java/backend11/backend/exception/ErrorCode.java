package backend11.backend.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다."),
    MEMBER_IDENTITY_ALREADY_REGISTERED(HttpStatus.CONFLICT, "이미 등록된 학생 정보입니다."),
    ACCOUNT_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않는 회원입니다."),
    USERNAME_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않는 아이디입니다."),
    USERNAME_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 존재하는 아이디입니다."),
    PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    INVALID_TEACHER_CODE(HttpStatus.BAD_REQUEST, "교사 코드가 올바르지 않습니다."),
    ALREADY_CHECKED_IN(HttpStatus.BAD_REQUEST, "오늘 이미 출석 처리되었습니다."),
    ATTENDANCE_NOT_FOUND(HttpStatus.BAD_REQUEST, "오늘 출석 기록이 없습니다."),
    ALREADY_CHECKED_OUT(HttpStatus.BAD_REQUEST, "이미 퇴실 처리되었습니다."),
    PLACE_REQUIRED(HttpStatus.BAD_REQUEST, "출석 실을 입력해 주세요."),
    INVALID_CLASS_SCOPE(HttpStatus.BAD_REQUEST, "학년과 반은 1 이상이어야 합니다.");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
