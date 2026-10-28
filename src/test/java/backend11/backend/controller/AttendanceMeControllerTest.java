package backend11.backend.controller;

import backend11.backend.dto.AttendanceMeResponse;
import backend11.backend.dto.AttendanceStatus;
import backend11.backend.service.AttendanceLookupService;
import backend11.backend.service.AttendanceMeService;
import backend11.backend.service.AttendanceService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AttendanceMeControllerTest {

    @Mock
    private AttendanceService attendanceService;

    @Mock
    private AttendanceLookupService attendanceLookupService;

    @Mock
    private AttendanceMeService attendanceMeService;

    @InjectMocks
    private AttendanceController attendanceController;

    @Test
    void 요청한_날짜를_인증된_사용자명으로_조회한다() {
        Authentication authentication = mock(Authentication.class);
        LocalDate date = LocalDate.of(2026, 10, 28);
        AttendanceMeResponse expected = new AttendanceMeResponse(
                date, AttendanceStatus.ABSENT, null, null, null, null
        );
        when(authentication.getName()).thenReturn("student");
        when(attendanceMeService.getMine("student", date)).thenReturn(expected);

        ResponseEntity<AttendanceMeResponse> response = attendanceController.getMine(authentication, date);

        assertThat(response.getBody()).isSameAs(expected);
        verify(attendanceMeService).getMine("student", date);
    }
}
