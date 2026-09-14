package backend11.backend.controller;

import backend11.backend.domain.Member;
import backend11.backend.dto.UpdateMemberRequest;
import backend11.backend.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/members")
@Tag(name = "회원", description = "내 정보 조회, 수정, 탈퇴 API")
@SecurityRequirement(name = "bearerAuth")
public class MemberController {

    @Autowired
    private MemberService memberService;

    // 내 정보 가져오기
    @GetMapping("/me")
    @Operation(summary = "내 정보 조회", description = "로그인한 사용자의 회원 정보를 조회합니다.")
    public Member getMyMember(Authentication authentication) {
        String username = authentication.getName();
        Member member = memberService.getMemberByUsername(username);
        return member;
    }

    // 내 정보 수정하기
    @PutMapping("/me")
    @Operation(summary = "내 정보 수정", description = "로그인한 사용자의 이름, 학년, 반, 번호를 수정합니다.")
    public Member updateMyMember(Authentication authentication, @RequestBody UpdateMemberRequest request) {

        String username = authentication.getName();

        String name = request.getName();
        int grade = request.getGrade();
        int studentClass = request.getStudentClass();
        int number = request.getNumber();

        Member updatedMember = memberService.updateMemberByUsername(username, name, grade, studentClass, number);

        return updatedMember;
    }

    @DeleteMapping("/me")
    @Operation(summary = "회원 탈퇴", description = "로그인한 사용자의 회원 정보를 삭제합니다.")
    public String deleteMyMember(Authentication authentication) {
        String username = authentication.getName();
        memberService.deleteMemberByUsername(username);
        return "회원탈퇴가 완료되었습니다.";
    }
}
