package backend11.backend.dto;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.management.relation.Role;

@Getter
@NoArgsConstructor
public class TeacherSignupRequest {


    private String name;
    private String username;
    private String password;
    private String teacherCode;


}