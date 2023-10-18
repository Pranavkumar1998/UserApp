package userapp;

public class Validator {

    /**
     * Check if the length of a string is within a specified range.
     *
     * @param label The label or name of the field being validated.
     * @param value The value to be checked.
     * @param min   The minimum allowed length.
     * @param max   The maximum allowed length.
     * @return A string describing any validation errors, or an empty string if the value is valid.
     */
    public static String stringLength(String label, String value, int min, int max) {
        String errors = "";

        if (value.length() > max) {
            errors += label + " should not be more than " + max + " characters. ";
        }
        if (min > 0 && value.length() == 0) {
            errors += label + " cannot be empty. ";
        } else if (value.length() < min) {
            errors += label + " should not be less than " + min + " characters. ";
        }

        return errors.trim();
    }

    /**
     * Combine multiple error messages into a single string.
     *
     * @param errors An array of error messages.
     * @return A concatenated string containing all the error messages, or an empty string if no errors exist.
     */
    public static String allError(String[] errors) {
        StringBuilder errorBuilder = new StringBuilder();

        for (String error : errors) {
            if (error != null && !error.isEmpty()) {
                errorBuilder.append(error).append("\n");
            }
        }

        return errorBuilder.toString().trim();
    }

    /**
     * Check if a string can be parsed as an integer.
     *
     * @param label The label or name of the field being validated.
     * @param value The value to be checked.
     * @return A string describing any validation errors, or an empty string if the value is a valid integer.
     */
    public static String isInt(String label, String value) {
        try {
            int intValue = Integer.parseInt(value);
            // If parsing succeeds, it's a valid integer
            return "";
        } catch (NumberFormatException e) {
            // Parsing failed, not a valid integer
            return label + " is not a valid number. ";
        }
    }
}
