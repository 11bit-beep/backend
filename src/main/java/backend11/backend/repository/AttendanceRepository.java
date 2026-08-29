package backend11.backend.repository;

import backend11.backend.domain.Attendance;
import backend11.backend.dto.AttendanceJoinRow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    Attendance findByUserIdAndDate(Long userId, LocalDate date);

    /**
     * 반 학생 전체를 기준으로 출석을 LEFT JOIN한다.
     * Member와 User가 분리된 현재 모델에서는 두 엔티티의 username을 연결 키로 사용한다.
     */
    @Query("""
            select new backend11.backend.dto.AttendanceJoinRow(
                m.id, m.name, m.grade, m.studentClass, m.number,
                a.id, a.checkInAt, a.checkOutAt, a.type, a.place
            )
            from Member m
            left join User u on u.username = m.username
            left join Attendance a on a.user = u and a.date = :date
            where m.grade = :grade
              and m.studentClass = :studentClass
            order by m.number asc, a.checkInAt desc
            """)
    List<AttendanceJoinRow> findClassAttendanceRows(
            @Param("grade") int grade,
            @Param("studentClass") int studentClass,
            @Param("date") LocalDate date
    );

    /**
     * 지정한 반의 학생 명단을 기준으로 특정 실(place)의 출석만 LEFT JOIN한다.
     * 해당 실에 출석 기록이 없으면 다른 실의 기록 유무와 관계없이 미출석으로 처리한다.
     */
    @Query("""
            select new backend11.backend.dto.AttendanceJoinRow(
                m.id, m.name, m.grade, m.studentClass, m.number,
                a.id, a.checkInAt, a.checkOutAt, a.type, a.place
            )
            from Member m
            left join User u on u.username = m.username
            left join Attendance a on a.user = u
                and a.date = :date
                and a.place = :place
            where m.grade = :grade
              and m.studentClass = :studentClass
            order by m.number asc, a.checkInAt desc
            """)
    List<AttendanceJoinRow> findPlaceAttendanceRows(
            @Param("grade") int grade,
            @Param("studentClass") int studentClass,
            @Param("place") String place,
            @Param("date") LocalDate date
    );
}
