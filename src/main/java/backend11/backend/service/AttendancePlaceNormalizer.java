package backend11.backend.service;

import backend11.backend.domain.AttendancePlace;

/** 출석 장소의 여러 표기를 API에서 사용하는 한 가지 형식으로 맞춘다. */
public final class AttendancePlaceNormalizer {

    private AttendancePlaceNormalizer() {
    }

    public static String normalize(String place) {
        validate(place);

        return AttendancePlace.find(place)
                .map(AttendancePlace::getLabel)
                .orElse(place.trim());
    }

    public static String toComparisonKey(String place) {
        validate(place);

        return AttendancePlace.toComparisonKey(place);
    }

    private static void validate(String place) {
        if (place == null || place.isBlank()) {
            throw new IllegalArgumentException("출석 실을 입력해 주세요.");
        }
    }
}
