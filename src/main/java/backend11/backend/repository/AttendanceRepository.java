package backend11.backend.repository;

import backend11.backend.domain.Attendance;
import backend11.backend.dto.AttendanceJoinRow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    Attendance findByUserIdAndDate(Long userId, LocalDate date);

    long deleteAllByUser_Id(Long userId);
    Attendance findFirstByUserIdAndDateOrderByCheckInAtDesc(Long userId, LocalDate date);

    /**
     * 반 학생 전체를 기준으로 출석을 LEFT JOIN한다.
     * Member가 참조하는 User를 기준으로 출석 기록을 연결한다.
     */
    @Query("""
            select new backend11.backend.dto.AttendanceJoinRow(
                m.id, m.name, m.grade, m.studentClass, m.number,
                a.id, a.checkInAt, a.checkOutAt, a.type, a.place
            )
            from Member m
            left join Attendance a on a.user = m.user and a.date = :date
            where m.grade = :grade
              and m.studentClass = :studentClass
            order by m.number asc, a.checkInAt desc
            """)
    List<AttendanceJoinRow> findClassAttendanceRows(
            @Param("grade") int grade,
            @Param("studentClass") int studentClass,
            @Param("date") LocalDate date
    );

    /** 지정한 날짜와 실(place)에 출석 기록이 있는 모든 학생을 조회한다. */
    @Query("""
            select new backend11.backend.dto.AttendanceJoinRow(
                m.id, m.name, m.grade, m.studentClass, m.number,
                a.id, a.checkInAt, a.checkOutAt, a.type, a.place
            )
            from Member m
            join Attendance a on a.user = m.user
            where a.date = :date
              and upper(replace(replace(replace(a.place, '-', ''), ' ', ''), '실', '')) = :placeKey
            order by m.grade asc, m.studentClass asc, m.number asc, a.checkInAt desc
            """)
    List<AttendanceJoinRow> findPlaceAttendanceRows(
            @Param("placeKey") String placeKey,
            @Param("date") LocalDate date
    );
}