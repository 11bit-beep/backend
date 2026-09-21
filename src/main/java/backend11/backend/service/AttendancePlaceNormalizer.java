package backend11.backend.service;

import java.util.Locale;

/** 출석 장소의 여러 표기를 API에서 사용하는 한 가지 형식으로 맞춘다. */
public final class AttendancePlaceNormalizer {

    private AttendancePlaceNormalizer() {
    }

    public static String normalize(String place) {
        if (place == null || place.isBlank()) {
            throw new IllegalArgumentException("출석 실을 입력해 주세요.");
        }

        String trimmedPlace = place.trim();
        return switch (toComparisonKey(trimmedPlace)) {
            case "LAB1" -> "LAB-1";
            case "LAB2" -> "LAB-2";
            case "LAB3" -> "LAB-3";
            default -> trimmedPlace;
        };
    }

    public static String toComparisonKey(String place) {
        if (place == null || place.isBlank()) {
            throw new IllegalArgumentException("출석 실을 입력해 주세요.");
        }

        return place.trim()
                .toUpperCase(Locale.ROOT)
                .replaceAll("\\s+", "")
                .replace("-", "")
                .replace("실", "");
    }
}
