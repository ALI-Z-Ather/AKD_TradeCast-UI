package tradecast.utils;

import java.util.List;
import java.util.stream.Collectors;

public final class OrdinalText {

    private OrdinalText() {
    }

    public static String ordinal(int oneBasedIndex) {
        int n = oneBasedIndex;
        int mod100 = n % 100;
        if (mod100 >= 11 && mod100 <= 13) {
            return n + "th";
        }
        return switch (n % 10) {
            case 1 -> n + "st";
            case 2 -> n + "nd";
            case 3 -> n + "rd";
            default -> n + "th";
        };
    }

    public static String joinWithAmpersand(List<Integer> sortedOneBased) {
        List<String> parts = sortedOneBased.stream()
                .map(OrdinalText::ordinal)
                .collect(Collectors.toList());
        if (parts.isEmpty()) {
            return "";
        }
        if (parts.size() == 1) {
            return parts.get(0);
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < parts.size(); i++) {
            if (i > 0) {
                if (i == parts.size() - 1) {
                    sb.append(" & ");
                } else {
                    sb.append(", ");
                }
            }
            sb.append(parts.get(i));
        }
        return sb.toString();
    }
}
