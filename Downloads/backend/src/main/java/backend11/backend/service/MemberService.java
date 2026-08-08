package com.jisoo._bitproject.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    // 내 정보 조회
    public Member getMember(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));
    }

    // 내 정보 수정
    @Transactional
    public Member updateMember(Long id, String name, String department) {
        Member member = getMember(id);
        member.updateInfo(name, department);
        return member;
    }
}