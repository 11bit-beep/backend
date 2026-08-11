package backend11.backend.controller;

import backend11.backend.domain.Member;
import backend11.backend.service.MemberService;
import backend11.backend.dto.UpdateMemberRequest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    // 내 정보 조회
    @GetMapping("/{id}")
    public Member getMember(@PathVariable Long id) {
        return memberService.getMember(id);
    }

    // 내 정보 수정
    @PutMapping("/{id}")
    public Member updateMember(@PathVariable Long id, @RequestBody UpdateMemberRequest request) {
        return memberService.updateMember(
                id,
                request.getName(),
                request.getGrade(),
                request.getStudentClass(),
                request.getNumber()
        );
    }
}