package userapp;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * This class provides utility methods for displaying different types of alerts in a JavaFX application.
 */
public class Alerts {

    /**
     * Display an alert with the specified type, title, header, and content.
     *
     * @param type    The type of the alert (e.g., "info," "warning," or "error").
     * @param title   The title of the alert (can be an empty string).
     * @param header  The header text of the alert (can be an empty string).
     * @param content The content text of the alert (can be an empty string).
     */
    public static void show(String type, String title, String header, String content) {

        Alert alert;

        // Determine the alert type based on the 'type' parameter
        if (type.equals("info")) {
            alert = new Alert(AlertType.INFORMATION);
        } else if (type.equals("warning")) {
            alert = new Alert(AlertType.WARNING);
        } else if (type.equals("error")) {
            alert = new Alert(AlertType.ERROR);
        } else {
            alert = new Alert(AlertType.NONE);
        }

        // Set the title, header, and content if they are not empty
        if (!title.isEmpty()) {
            alert.setTitle(title);
        }
        if (!header.isEmpty()) {
            alert.setHeaderText(header);
        }
        if (!content.isEmpty()) {
            alert.setContentText(content);
        }

        // Show the alert and wait for the user's response
        alert.showAndWait().ifPresent(response -> {
            // Handle the user's response here
        });
    }
}
