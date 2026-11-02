package backend11.backend.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class AttendancePlaceTest {

    @ParameterizedTest
    @ValueSource(strings = {"LAB-1", "LAB1", "lab 1", "LAB 1실", " lab-1실 "})
    void LAB1의_여러_표기를_LAB_1로_찾는다(String place) {
        assertThat(AttendancePlace.find(place)).contains(AttendancePlace.LAB_1);
    }

    @Test
    void 정의되지_않은_장소는_찾지_못한다() {
        assertThat(AttendancePlace.find("교실")).isEmpty();
        assertThat(AttendancePlace.find("LAB-4")).isEmpty();
    }

    @Test
    void 비어_있는_장소는_찾지_못한다() {
        assertThat(AttendancePlace.find(null)).isEmpty();
        assertThat(AttendancePlace.find("  ")).isEmpty();
    }

    @Test
    void 장소는_라벨과_비교_키를_가진다() {
        assertThat(AttendancePlace.LAB_3.getLabel()).isEqualTo("LAB-3");
        assertThat(AttendancePlace.LAB_3.getComparisonKey()).isEqualTo("LAB3");
    }
}
