package backend11.backend.service;

import backend11.backend.domain.Attendance;
import backend11.backend.repository.AttendanceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;

    public AttendanceService(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }

    // 출석 체크
    public Attendance checkIn(Long userId) {
        Attendance attendance = new Attendance(userId);
        return attendanceRepository.save(attendance);
    }
}