package backend11.backend.attendance.service;

import backend11.backend.attendance.domain.Attendance;
import backend11.backend.attendance.dto.AttendanceMeResponse;
import backend11.backend.attendance.repository.AttendanceRepository;
import backend11.backend.common.exception.CustomException;
import backend11.backend.common.exception.ErrorCode;
import backend11.backend.user.domain.User;
import backend11.backend.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AttendanceMeService {

    private final UserRepository userRepository;
    private final AttendanceRepository attendanceRepository;

    public AttendanceMeResponse getMine(String username, LocalDate date) {
        LocalDate lookupDate = date == null ? LocalDate.now() : date;
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

        Attendance attendance = attendanceRepository.findFirstByUserIdAndDateOrderByCheckInAtDesc(
                user.getId(), lookupDate
        );
        return AttendanceMeResponse.from(lookupDate, attendance);
    }
}
