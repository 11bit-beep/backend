package backend11.backend.controller;

import backend11.backend.domain.Attendance;
import backend11.backend.dto.AttendanceRequest;
import backend11.backend.service.AttendanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    // 출석 체크 API
    @PostMapping("/check_in/{userId}")
    public ResponseEntity<Attendance> checkIn(
            @PathVariable Long userId,
            @RequestBody AttendanceRequest request
    ) {
        Attendance attendance = attendanceService.checkIn(userId, request.getType(), request.getPlace());
        return ResponseEntity.ok(attendance);
    }
}