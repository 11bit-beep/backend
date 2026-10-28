package backend11.backend.service;

import backend11.backend.domain.Attendance;
import backend11.backend.domain.User;
import backend11.backend.dto.AttendanceMeResponse;
import backend11.backend.repository.AttendanceRepository;
import backend11.backend.repository.UserRepository;
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
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        Attendance attendance = attendanceRepository.findFirstByUserIdAndDateOrderByCheckInAtDesc(
                user.getId(), lookupDate
        );
        return AttendanceMeResponse.from(lookupDate, attendance);
    }
}
