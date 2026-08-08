package com.jisoo._bitproject.member;

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
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String department;

    @Builder
    public Member(String username, String password, String name, String department) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.department = department;
    }

    // 정보 수정 메서드
    public void updateInfo(String name, String department) {
        this.name = name;
        this.department = department;
    }

}
