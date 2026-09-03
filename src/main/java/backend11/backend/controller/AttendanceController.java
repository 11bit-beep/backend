package backend11.backend.controller;

import backend11.backend.domain.Attendance;
import backend11.backend.dto.AttendanceLookupResponse;
import backend11.backend.dto.AttendanceRequest;
import backend11.backend.service.AttendanceLookupService;
import backend11.backend.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final AttendanceLookupService attendanceLookupService;

    // 출석 체크 API
    @PostMapping("/check_in")
    public ResponseEntity<Attendance> checkIn(
            Authentication authentication,
            @RequestBody AttendanceRequest request
    ) {
        Attendance attendance = attendanceService.checkIn(
                authentication.getName(),
                request.getType(),
                request.getPlace()
        );
        return ResponseEntity.ok(attendance);
    }

    // 퇴실 API
    @PutMapping("/check_out")
    public ResponseEntity<Attendance> checkOut(Authentication authentication) {
        Attendance attendance = attendanceService.checkOut(authentication.getName());
        return ResponseEntity.ok(attendance);
    }

    // 반별 출석 조회: 출석 기록이 없는 학생도 ABSENT로 포함한다.
    @GetMapping("/classes/{grade}/{studentClass}")
    public ResponseEntity<AttendanceLookupResponse> getByClass(
            @PathVariable int grade,
            @PathVariable int studentClass,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return ResponseEntity.ok(attendanceLookupService.getByClass(grade, studentClass, date));
    }

    // 실별 출석 조회: 해당 반 명단 중 지정한 실에 출석하지 않은 학생을 ABSENT로 포함한다.
    @GetMapping("/places/{place}")
    public ResponseEntity<AttendanceLookupResponse> getByPlace(
            @PathVariable String place,
            @RequestParam int grade,
            @RequestParam int studentClass,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return ResponseEntity.ok(
                attendanceLookupService.getByPlace(grade, studentClass, place, date)
        );
    }
}
