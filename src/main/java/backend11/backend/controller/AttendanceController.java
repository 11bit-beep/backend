package backend11.backend.controller;

import backend11.backend.domain.Attendance;
import backend11.backend.dto.AttendanceLookupResponse;
import backend11.backend.dto.AttendanceRequest;
import backend11.backend.service.AttendanceLookupService;
import backend11.backend.service.AttendanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
@Tag(name = "출석", description = "출석·퇴실 처리와 출석 현황 조회 API")
@SecurityRequirement(name = "bearerAuth")
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final AttendanceLookupService attendanceLookupService;

    // 출석 체크 API
    @PostMapping("/check_in")
    @Operation(summary = "출석 처리", description = "로그인한 사용자의 출석을 기록합니다.")
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
    @Operation(summary = "퇴실 처리", description = "로그인한 사용자의 당일 출석 기록에 퇴실 시간을 저장합니다.")
    public ResponseEntity<Attendance> checkOut(Authentication authentication) {
        Attendance attendance = attendanceService.checkOut(authentication.getName());
        return ResponseEntity.ok(attendance);
    }

    // 반별 출석 조회: 출석 기록이 없는 학생도 ABSENT로 포함한다.
    @GetMapping("/classes/{grade}/{studentClass}")
    @Operation(summary = "반별 출석 조회", description = "지정한 학년·반의 학생과 출석 상태를 조회합니다. 기록이 없는 학생도 ABSENT로 포함됩니다.")
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
    @Operation(summary = "실별 출석 조회", description = "지정한 장소의 출석 현황을 조회합니다. 해당 반 명단 중 기록이 없는 학생도 ABSENT로 포함됩니다.")
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
