package backend11.backend.service;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class AttendancePlaceNormalizerTest {

    @ParameterizedTest
    @ValueSource(strings = {"LAB-2", "LAB2", "lab 2", "LAB 2실", " lab-2실 "})
    void lab2의_여러_표기를_하나로_맞춘다(String place) {
        assertThat(AttendancePlaceNormalizer.normalize(place)).isEqualTo("LAB-2");
        assertThat(AttendancePlaceNormalizer.toComparisonKey(place)).isEqualTo("LAB2");
    }
}
