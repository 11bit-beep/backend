package com.jisoo._bitproject.member;

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
        return memberService.updateMember(id, request.getName(), request.getDepartment());
    }
}