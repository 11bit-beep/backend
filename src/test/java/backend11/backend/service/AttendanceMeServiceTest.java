package backend11.backend.service;

import backend11.backend.domain.Attendance;
import backend11.backend.domain.User;
import backend11.backend.dto.AttendanceStatus;
import backend11.backend.repository.AttendanceRepository;
import backend11.backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AttendanceMeServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AttendanceRepository attendanceRepository;

    @InjectMocks
    private AttendanceMeService attendanceMeService;

    @Test
    void 로그인한_사용자의_지정_날짜_출석만_조회한다() {
        LocalDate date = LocalDate.of(2026, 10, 27);
        LocalDateTime checkInAt = date.atTime(8, 30);
        LocalDateTime checkOutAt = date.atTime(17, 0);
        User user = mock(User.class);
        Attendance attendance = mock(Attendance.class);
        when(user.getId()).thenReturn(42L);
        when(userRepository.findByUsername("student")).thenReturn(Optional.of(user));
        when(attendanceRepository.findFirstByUserIdAndDateOrderByCheckInAtDesc(42L, date))
                .thenReturn(attendance);
        when(attendance.getCheckInAt()).thenReturn(checkInAt);
        when(attendance.getCheckOutAt()).thenReturn(checkOutAt);
        when(attendance.getType()).thenReturn("NORMAL");
        when(attendance.getPlace()).thenReturn("LAB-2");

        var response = attendanceMeService.getMine("student", date);

        assertThat(response.date()).isEqualTo(date);
        assertThat(response.status()).isEqualTo(AttendanceStatus.CHECKED_OUT);
        assertThat(response.checkInAt()).isEqualTo(checkInAt);
        assertThat(response.checkOutAt()).isEqualTo(checkOutAt);
        assertThat(response.place()).isEqualTo("LAB-2");
        verify(attendanceRepository).findFirstByUserIdAndDateOrderByCheckInAtDesc(42L, date);
    }

    @Test
    void 출석_기록이_없으면_본인_미출석_상태를_반환한다() {
        LocalDate date = LocalDate.of(2026, 10, 28);
        User user = mock(User.class);
        when(user.getId()).thenReturn(42L);
        when(userRepository.findByUsername("student")).thenReturn(Optional.of(user));

        var response = attendanceMeService.getMine("student", date);

        assertThat(response.date()).isEqualTo(date);
        assertThat(response.status()).isEqualTo(AttendanceStatus.ABSENT);
        assertThat(response.checkInAt()).isNull();
        assertThat(response.checkOutAt()).isNull();
        assertThat(response.type()).isNull();
        assertThat(response.place()).isNull();
    }
}
