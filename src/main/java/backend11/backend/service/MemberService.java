package backend11.backend.service;

import backend11.backend.domain.Member;
import backend11.backend.domain.Role;
import backend11.backend.dto.SignupRequest;
import backend11.backend.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long signUp(SignupRequest requestDto) {

        // 1) 아이디 중복
        if (memberRepository.existsByUsername(requestDto.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 사용 중인 아이디입니다.");
        }

        // 2) 학년 + 반 + 번호 + 이름 중복
        if (memberRepository.existsByGradeAndStudentClassAndNumberAndName(
                requestDto.getGrade(),
                requestDto.getStudentClass(),
                requestDto.getNumber(),
                requestDto.getName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 등록된 학생 정보입니다.");
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

        return memberRepository.save(member).getId();
    }

    // 내 정보 조회 (id)
    public Member getMember(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다."));
    }

    // 내 정보 조회 (아이디)
    public Member getMemberByUsername(String username) {
        return memberRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다."));
    }

    // 내 정보 수정 (id)
    @Transactional
    public Member updateMember(Long id, String name, int grade, int studentClass, int number) {
        Member member = getMember(id);
        validateIdentityNotTaken(member, name, grade, studentClass, number);
        member.updateInfo(name, grade, studentClass, number);
        return member;
    }

    // 내 정보 수정 (아이디)
    @Transactional
    public Member updateMemberByUsername(String username, String name,
                                         int grade, int studentClass, int number) {
        Member member = getMemberByUsername(username);
        validateIdentityNotTaken(member, name, grade, studentClass, number);
        member.updateInfo(name, grade, studentClass, number);
        return member;
    }

    private void validateIdentityNotTaken(Member target, String name,
                                          int grade, int studentClass, int number) {
        boolean taken = memberRepository
                .findByGradeAndStudentClassAndNumberAndName(grade, studentClass, number, name)
                .filter(other -> !other.getId().equals(target.getId()))
                .isPresent();

        if (taken) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 등록된 학생 정보입니다.");
        }
    }
}