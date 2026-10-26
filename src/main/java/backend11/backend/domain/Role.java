package backend11.backend.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    USER("ROLE_USER", "사용자"),
    ADMIN("ROLE_ADMIN", "관리자"),
    STUDENT("ROLE_STUDENT", "학생"),
    TEACHER("ROLE_TEACHER", "선생님");

    private final String key;
    private final String title;
}