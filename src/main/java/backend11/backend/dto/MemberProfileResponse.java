package backend11.backend.dto;

import backend11.backend.domain.Member;
import backend11.backend.domain.Role;
import backend11.backend.domain.User;

public record MemberProfileResponse(
        Long id,
        String username,
        String name,
        int grade,
        int studentClass,
        int number,
        Role role
) {
    public static MemberProfileResponse from(Member member) {
        User user = member.getUser();
        return new MemberProfileResponse(
                member.getId(),
                user.getUsername(),
                member.getName(),
                member.getGrade(),
                member.getStudentClass(),
                member.getNumber(),
                user.getRole()
        );
    }
}
