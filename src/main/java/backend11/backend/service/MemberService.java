package backend11.backend.service;

import backend11.backend.domain.Member;
import backend11.backend.domain.Role;
import backend11.backend.dto.SignupRequest;
import backend11.backend.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long signUp(SignupRequest requestDto) {

        if (memberRepository.findByUsername(requestDto.getUsername()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 아이디 입니다.");
        }

        Member member = Member.builder()
                .username(requestDto.getUsername())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .name(requestDto.getName())
                .grade(requestDto.getGrade())
                .studentClass(requestDto.getStudentClass())
                .number(requestDto.getNumber())
                .role(Role.USER)
                .build();

        // DB에 저장하고 생성된 PK(아이디)를 반환
        return memberRepository.save(member).getId();
    }

    // 내 정보 조회
    public Member getMember(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));
    }

    // 내 정보 수정
    @Transactional
    public Member updateMember(Long id, String name, int grade, int studentClass, int number) {
        Member member = getMember(id);
        member.updateInfo(name, grade, studentClass, number);

        return member;
    }
}