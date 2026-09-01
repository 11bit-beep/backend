package backend11.backend.service;

import backend11.backend.domain.Attendance;
import backend11.backend.domain.User;
import backend11.backend.repository.AttendanceRepository;
import backend11.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;

    // 출석하기
    public Attendance checkIn(Long userId, String type, String place) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        // 오늘 이미 출석했는지 확인
        if (attendanceRepository.findByUserIdAndDate(userId, LocalDate.now()) != null) {
            throw new IllegalArgumentException("ㅇ오늘 이미 출석 처리되었습니다.");
        }

        Attendance attendance = new Attendance(user, type, place);

        return attendanceRepository.save(attendance);
    }

    // 퇴실하기
    public Attendance checkOut(Long userId) {
        Attendance attendance = attendanceRepository.findByUserIdAndDate(
                userId,
                LocalDate.now()
        );

        if (attendance == null) {
            throw new IllegalArgumentException("오늘 출석 기록이 없습니다.");
        }

        if (attendance.getCheckOutAt() != null) {
            throw new IllegalArgumentException("이미 퇴실 처리되었습니다.");
        }

        attendance.updateCheckOut();

        return attendance;
    }
}