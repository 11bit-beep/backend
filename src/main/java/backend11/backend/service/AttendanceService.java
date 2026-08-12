package backend11.backend.service;

import backend11.backend.domain.Attendance;
import backend11.backend.domain.User;
import backend11.backend.repository.AttendanceRepository;
import backend11.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository; //

    // 출석 체크
    public Attendance checkIn(Long userId, String type, String place) {
        // 1. ID로 User 엔티티 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        // 2. 수정된 생성자에 맞게 객체 생성
        Attendance attendance = new Attendance(user, type, place);

        return attendanceRepository.save(attendance);
    }
}