package userapp;

public class Validator {

	
	public static String stringLength(String label, String value, int min, int max) {

		String errors = "";

		if (value.length() > max) {
			errors += label + " should not be more than " + max + " characters";
		}
		if (min > 0 && value.length() == 0) {
			errors += label + " can not be empty";
		} else if (value.length() < min) {
			errors += label + " should not be less than " + min + " characters";
		}
		
		return errors;

	}
	
	
	public static String allError(String[] errors) {
	    StringBuilder errorBuilder = new StringBuilder();

	    for (String error : errors) {
	        if (error != null && !error.isEmpty()) {
	            errorBuilder.append(error).append("\n");
	        }
	    }

	    return errorBuilder.toString().trim(); 
	}
	
	
	
	public static String isInt(String label, String value) {
	    try {
	        int intValue = Integer.parseInt(value);
	        // If parsing succeeds, it's a valid integer
	        return "";
	    } catch (NumberFormatException e) {
	        // Parsing failed, not a valid integer
	        return label + " is not a valid number";
	    }
	}

	


}
