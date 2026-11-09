package backend11.backend.attendance.domain;

import java.util.Locale;
import java.util.Optional;

public enum AttendancePlace {
    LAB_1("LAB-1"),
    LAB_2("LAB-2"),
    LAB_3("LAB-3");

    private final String label;

    AttendancePlace(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public String getComparisonKey() {
        return toComparisonKey(label);
    }

    public static Optional<AttendancePlace> find(String place) {
        if (place == null || place.isBlank()) {
            return Optional.empty();
        }

        String comparisonKey = toComparisonKey(place);
        for (AttendancePlace candidate : values()) {
            if (candidate.getComparisonKey().equals(comparisonKey)) {
                return Optional.of(candidate);
            }
        }
        return Optional.empty();
    }

    public static String toComparisonKey(String place) {
        return place.trim()
                .toUpperCase(Locale.ROOT)
                .replaceAll("\\s+", "")
                .replace("-", "")
                .replace("실", "");
    }
}
