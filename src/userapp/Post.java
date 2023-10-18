package userapp;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * This class represents a post with properties such as postId, post content, author, likes, shares, and date/time.
 */
public class Post {
    private final IntegerProperty postId;
    private final StringProperty postContent;
    private final StringProperty author;
    private final IntegerProperty likes;
    private final IntegerProperty shares;
    private final StringProperty dateTime;

    /**
     * Constructor to create a new Post object with specified properties.
     *
     * @param postId       The unique identifier for the post.
     * @param postContent  The content of the post.
     * @param author       The author of the post.
     * @param likes        The number of likes on the post.
     * @param shares       The number of shares of the post.
     * @param dateTime     The date and time the post was created.
     */
    public Post(int postId, String postContent, String author, int likes, int shares, String dateTime) {
        this.postId = new SimpleIntegerProperty(postId);
        this.postContent = new SimpleStringProperty(postContent);
        this.author = new SimpleStringProperty(author);
        this.likes = new SimpleIntegerProperty(likes);
        this.shares = new SimpleIntegerProperty(shares);
        this.dateTime = new SimpleStringProperty(dateTime);
    }

    /**
     * Get the post's unique identifier.
     *
     * @return The post's unique identifier.
     */
    public int getPostId() {
        return postId.get();
    }

    /**
     * Get the content of the post.
     *
     * @return The content of the post.
     */
    public String getPostContent() {
        return postContent.get();
    }

    /**
     * Get the author of the post.
     *
     * @return The author of the post.
     */
    public String getAuthor() {
        return author.get();
    }

    /**
     * Get the number of likes on the post.
     *
     * @return The number of likes on the post.
     */
    public int getLikes() {
        return likes.get();
    }

    /**
     * Get the number of shares of the post.
     *
     * @return The number of shares of the post.
     */
    public int getShares() {
        return shares.get();
    }

    /**
     * Get the date and time the post was created.
     *
     * @return The date and time of post creation.
     */
    public String getDateTime() {
        return dateTime.get();
    }

    /**
     * Export the post's data to a CSV file.
     *
     * @param stage The JavaFX stage used for the file dialog.
     */
    public void export(Stage stage) {
        FileChooser fileChooser = new FileChooser();

        // Set extension filter for text files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show save file dialog
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            try {
                PrintWriter writer = new PrintWriter(file);
                writer.println(this.csvString());
                writer.close();
                Alerts.show("info", "Post Exported!", "Post has been exported successfully!", "");
            } catch (IOException ex) {
                Alerts.show("error", "File Error", ex.getMessage(), "");
            }
        } else {
            Alerts.show("error", "File Error", "No file selected", "");
        }
    }

    /**
     * Convert the post's data to a CSV string.
     *
     * @return A CSV-formatted string representation of the post's data.
     */
    public String csvString() {
        StringBuilder csv = new StringBuilder();
        csv.append(postId.get()).append(",");

        // Escape double quotes within string fields
        String escapedPostContent = postContent.get().replace("\"", "\"\"");
        String escapedAuthor = author.get().replace("\"", "\"\"");

        csv.append("\"").append(escapedPostContent).append("\",");
        csv.append("\"").append(escapedAuthor).append("\",");
        csv.append(likes.get()).append(",");
        csv.append(shares.get()).append(",");
        csv.append(dateTime.get()).append(",");
        return csv.toString();
    }
}
