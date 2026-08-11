package backend11.backend.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;



@Getter
@NoArgsConstructor
@Entity
@Table(name = "members")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long  id;
    @Column(nullable = false, unique = true)
    private String username; //ID

    @Column(nullable = false)
    private String password; //비번

    @Column(nullable = false)
    private String name; //이름

    @Column(nullable = false)
    private int grade; //학년

    @Column(nullable = false)
    private int studentClass; //반

    @Column(nullable = false)
    private int number;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;



    @Builder
    public Member(String username, String password, String name, int grade, int studentClass, int number, Role role,String department) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.grade = grade;
        this.studentClass = studentClass;
        this.number = number;


        this.role = role;
    }

    // 정보 수정 메서드
    public void updateInfo(String name, int grade,int studentClass, int number)  {
        this.name = name;
        this.grade = grade;
        this.studentClass = studentClass;
        this.number = number;
    }

}
