package backend11.backend.service;

import backend11.backend.domain.Member;
import backend11.backend.domain.User;
import backend11.backend.repository.AttendanceRepository;
import backend11.backend.repository.MemberRepository;
import backend11.backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final UserRepository userRepository;
    private final AttendanceRepository attendanceRepository;

    // 내 정보 조회 (id)
    public Member getMember(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다."));
    }

    // 내 정보 조회 (아이디)
    public Member getMemberByUsername(String username) {
        return memberRepository.findByUser_Username(username)
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
    // 로그인 계정과 연결된 회원 정보 및 출석 기록을 함께 삭제한다.
    @Transactional
    public void deleteAccountByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다."));
        attendanceRepository.deleteAllByUser_Id(user.getId());
        memberRepository.findByUser_Username(username).ifPresent(memberRepository::delete);
        memberRepository.flush();
        userRepository.delete(user);
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
