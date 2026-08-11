package backend11.backend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Getter
@NoArgsConstructor
public class SignupRequest {
    private String name;
    private String username;
    private String password;
    private int grade;
    private int studentClass;
    private int number;
}
