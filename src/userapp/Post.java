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

public class Post {
	private final IntegerProperty postId;
	private final StringProperty postContent;
	private final StringProperty author;
	private final IntegerProperty likes;
	private final IntegerProperty shares;
	private final StringProperty dateTime;

	public Post(int postId, String postContent, String author, int likes, int shares, String dateTime) {
		this.postId = new SimpleIntegerProperty(postId);
		this.postContent = new SimpleStringProperty(postContent);
		this.author = new SimpleStringProperty(author);
		this.likes = new SimpleIntegerProperty(likes);
		this.shares = new SimpleIntegerProperty(shares);
		this.dateTime = new SimpleStringProperty(dateTime);
	}

	public int getPostId() {
		return postId.get();
	}

	public String getPostContent() {
		return postContent.get();
	}

	public String getAuthor() {
		return author.get();
	}

	public int getLikes() {
		return likes.get();
	}

	public int getShares() {
		return shares.get();
	}

	public String getDateTime() {
		return dateTime.get();
	}

	public void export(Stage stage) {

		FileChooser fileChooser = new FileChooser();

		// Set extension filter for text files
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extFilter);

		// Show save file dialog
		File file = fileChooser.showSaveDialog(stage);

		if (file != null) {

			try {
				PrintWriter writer;
				writer = new PrintWriter(file);
				writer.println(this.csvString());
				writer.close();
				Alerts.show("info", "Post Exported!", "Post has been exported successfully!", "");
			} catch (IOException ex) {
				Alerts.show("error", "File Error", ex.getMessage(), "");
			}

		} else {
			
			Alerts.show("error", "File Error", "No File seleceted", "");

		}

	}
	

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