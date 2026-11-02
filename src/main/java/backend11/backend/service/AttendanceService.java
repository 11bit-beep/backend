package backend11.backend.service;

import backend11.backend.domain.Attendance;
import backend11.backend.domain.User;
import backend11.backend.exception.CustomException;
import backend11.backend.exception.ErrorCode;
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
    public Attendance checkIn(String username, String type, String place) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

        // 오늘 이미 출석했는지 확인
        if (attendanceRepository.findByUserIdAndDate(user.getId(), LocalDate.now()) != null) {
            throw new CustomException(ErrorCode.ALREADY_CHECKED_IN);
        }

        String normalizedPlace = AttendancePlaceNormalizer.normalize(place);
        Attendance attendance = new Attendance(user, type, normalizedPlace);

        return attendanceRepository.save(attendance);
    }

    // 퇴실하기
    public Attendance checkOut(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

        Attendance attendance = attendanceRepository.findByUserIdAndDate(
                user.getId(),
                LocalDate.now()
        );

        if (attendance == null) {
            throw new CustomException(ErrorCode.ATTENDANCE_NOT_FOUND);
        }

        if (attendance.getCheckOutAt() != null) {
            throw new CustomException(ErrorCode.ALREADY_CHECKED_OUT);
        }

        attendance.updateCheckOut();

        return attendance;
    }
}
