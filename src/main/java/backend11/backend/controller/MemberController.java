package backend11.backend.controller;

import backend11.backend.domain.Member;
import backend11.backend.dto.UpdateMemberRequest;
import backend11.backend.service.MemberService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    @Autowired
    private MemberService memberService;

    // 내 정보 가져오기
    @GetMapping("/me")
    public Member getMyMember(Authentication authentication) {
        String username = authentication.getName();
        Member member = memberService.getMemberByUsername(username);
        return member;
    }

    // 내 정보 수정하기
    @PutMapping("/me")
    public Member updateMyMember(Authentication authentication, @RequestBody UpdateMemberRequest request) {

        String username = authentication.getName();

        String name = request.getName();
        int grade = request.getGrade();
        int studentClass = request.getStudentClass();
        int number = request.getNumber();

        Member updatedMember = memberService.updateMemberByUsername(username, name, grade, studentClass, number);

        return updatedMember;
    }
}