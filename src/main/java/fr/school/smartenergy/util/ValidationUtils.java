package fr.school.smartenergy.util;

public class ValidationUtils {

    private ValidationUtils() {}

    public static boolean isNullOrBlank(String value) {
        return value == null || value.isBlank();
    }

    public static boolean isPositive(double value) {
        return value > 0;
    }

    public static boolean isPositiveOrZero(double value) {
        return value >= 0;
    }

    public static boolean isPositiveInt(int value) {
        return value > 0;
    }

    /**
     * Tries to parse a double from a String.
     * Returns true if successful and the value is positive.
     */
    public static boolean isValidPositiveDouble(String value) {
        if (isNullOrBlank(value)) return false;
        try {
            double d = Double.parseDouble(value.trim());
            return d > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValidPositiveInt(String value) {
        if (isNullOrBlank(value)) return false;
        try {
            int i = Integer.parseInt(value.trim());
            return i > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
