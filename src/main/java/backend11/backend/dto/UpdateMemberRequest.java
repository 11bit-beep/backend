package backend11.backend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UpdateMemberRequest {
    private String name;
    private int grade;
    private int studentClass;
    private int number;
}