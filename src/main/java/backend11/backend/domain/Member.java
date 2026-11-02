package backend11.backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(
        name = "members",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_member_identity",
                columnNames = {"grade", "student_class", "number", "name"}
        )
)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true,
            foreignKey = @ForeignKey(name = "fk_members_user"))
    @JsonIgnore
    private User user;

    @Column(nullable = false)
    private String name; // 이름

    @Column(nullable = false)
    private int grade; // 학년

    @Column(nullable = false)
    private int studentClass; // 반

    @Column(nullable = false)
    private int number; // 번호

    @Builder
    public Member(User user, String name, int grade, int studentClass, int number) {
        this.user = user;
        this.name = name;
        this.grade = grade;
        this.studentClass = studentClass;
        this.number = number;
    }

    public String getUsername() {
        return user.getUsername();
    }

    // 정보 수정 메서드
    public void updateInfo(String name, int grade, int studentClass, int number) {
        this.name = name;
        this.grade = grade;
        this.studentClass = studentClass;
        this.number = number;
    }
}
