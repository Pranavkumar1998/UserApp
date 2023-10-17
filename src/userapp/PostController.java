package userapp;

import java.io.IOException;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.Node;

public class PostController {

	@FXML
	private TextField postIdField;

	@FXML
	private TextArea postContentField;

	@FXML
	private TextField authorField;

	@FXML
	private TextField likesField;

	@FXML
	private TextField sharesField;

	@FXML
	TextField singlePostIdField;

	@FXML
	TextField noPostField;

	ComboBox<String> postUserField;

	@FXML
	Label headerLabel;

	@FXML
	Hyperlink vipAccessSubMenu1, vipAccessSubMenu2;

	@FXML
	GridPane addPostPanel;

	@FXML
	VBox retrievePostPanel;

	@FXML
	HBox usernameListHolder;

	@FXML
	GridPane blankPanel;

	@FXML
	private VBox tableHolder;

	private User user;

	public void initialize(User user) {

		this.user = user;
		addPostPanel.setVisible(false);
		retrievePostPanel.setVisible(false);
		blankPanel.setVisible(true);
		if (user.isVip()) {
			vipAccessSubMenu1.setVisible(true);
			vipAccessSubMenu2.setVisible(true);
		}

		this.loadUsernames();
	}

	public void navigateScene(ActionEvent event) {

		Hyperlink hyperlink = (Hyperlink) event.getSource();
		String menu_id = hyperlink.getId();
		// System.out.println(menu_id);

		Scene scene = ((Node) event.getSource()).getScene();

		MenuController mc = new MenuController();

		mc.navigate(scene, user, menu_id);

	}

	private void loadUsernames() {

		Model model = new Model();
		String[] usernames = model.getUsernames();
		model.close();
		postUserField = new ComboBox<>();
		postUserField.getItems().addAll(usernames);
		postUserField.getStyleClass().add("generic-input");
		postUserField.maxWidth(100);
		
		usernameListHolder.getChildren().add(postUserField);

		
	}

	public void showRetrievePostPanel() {

		retrievePostPanel.setVisible(true);
		addPostPanel.setVisible(false);
		blankPanel.setVisible(false);

		headerLabel.setText("Retrieve Post(s)");

	}

	public void showAddPostPanel() {

		addPostPanel.setVisible(true);
		retrievePostPanel.setVisible(false);
		blankPanel.setVisible(false);

		headerLabel.setText("Add Post");

	}

	public void deletePost(ActionEvent event) {

		addPostPanel.setVisible(false);
		retrievePostPanel.setVisible(false);
		blankPanel.setVisible(true);

		headerLabel.setText("Delete Post");

		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Delete Post");
		dialog.setHeaderText("Please enter Post ID:");
		dialog.setContentText("postId:");

		Optional<String> result = dialog.showAndWait();
		result.ifPresent(postId -> {

			String error = Validator.isInt("Post Id", postId);
			if (error != "") {
				Alerts.show("error", "Error!", error, "");
				return;
			}
			int postId_n = Integer.parseInt(postId);

			Model model = new Model();

			if (model.deletePost(postId_n)) {

				Alerts.show("info", "Post Deleted", "Post with Id: " + postId + " has been deleted!", "");
			} else {

				Alerts.show("error", "Error", "No Post with this ID was found", "");

			}

		});

	}

	public void exportPost(ActionEvent event) {

		addPostPanel.setVisible(false);
		retrievePostPanel.setVisible(false);
		blankPanel.setVisible(true);

		headerLabel.setText("Export Post");

		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Export Post");
		dialog.setHeaderText("Please enter Post ID:");
		dialog.setContentText("postId:");

		Optional<String> result = dialog.showAndWait();
		result.ifPresent(postId -> {

			String error = Validator.isInt("Post Id", postId);
			if (error != "") {
				Alerts.show("error", "Error!", error, "");
				return;
			}
			int postId_n = Integer.parseInt(postId);

			Model model = new Model();

			Post post = model.getPost(postId_n);

			if (post != null) {

				Scene scene = ((Node) event.getSource()).getScene();
				Stage stage = (Stage) scene.getWindow();

				post.export(stage);
			} else {

				Alerts.show("error", "Error", "No Post with this ID was found", "");

			}

		});

	}

	public void createPost(ActionEvent event) {

		String postId = postIdField.getText();
		String postContent = postContentField.getText();
		String author = authorField.getText();
		String likes = likesField.getText();
		String shares = sharesField.getText();

		String errors[] = new String[5];

		errors[0] = Validator.isInt("Post Id", postId);
		errors[1] = Validator.stringLength("Post Content", postContent, 10, 5000);
		errors[2] = Validator.stringLength("Author", author, 2, 50);
		errors[3] = Validator.isInt("Likes", likes);
		errors[4] = Validator.isInt("Shares", shares);

		String allErrors = Validator.allError(errors);

		// Check if there are errors
		if (allErrors != "") {

			Alerts.show("error", "Error!", allErrors, "");

			return;
		}

		int postId_n = Integer.parseInt(postId);
		int likes_n = Integer.parseInt(likes);
		int shares_n = Integer.parseInt(shares);

		Model model = new Model();

		// Check if post id already exist
		if (model.postIdExist(postId_n)) {
			Alerts.show("error", "Error!", "Post Id:  '" + postId + "' already exists.", "");
			model.close();
			return;
		}

		if (model.insertPost(user.getUserId(), postId_n, postContent, author, likes_n, shares_n)) {

			// Clear form
			postIdField.setText("");
			postContentField.setText("");
			authorField.setText("");
			likesField.setText("");
			sharesField.setText("");

			Alerts.show("info", "Post Created", "Post Added Successfully.", "");

		} else {

			Alerts.show("error", "Error", "Error saving Post", "");

		}

		// Close the connection
		model.close();

	}

	public void retrieveSinglePost(ActionEvent event) {

		String postId = singlePostIdField.getText();

		String error = Validator.isInt("Post Id", postId);

		if (error != "") {

			Alerts.show("error", "Error!", error, "");

			return;
		}

		int postId_n = Integer.parseInt(postId);

		Model model = new Model();

		Post post = model.getPost(postId_n);

		if (post != null) {
			Post[] posts = new Post[1];
			posts[0] = post;
			showTable(posts);
		} else {

			Alerts.show("error", "Error", "No Post with this ID was found", "");

		}

		// Close the connection
		model.close();

	}

	public void retrievePosts(ActionEvent event) {

		String noPost = noPostField.getText();
		String postUser = postUserField.getValue();
		
		if(postUser == null) {
			postUser = "All User";
		}

		String[] errors = new String[2];
		errors[0] = Validator.isInt("No of Posts", noPost);
		errors[1] = Validator.stringLength("Username", postUser, 3, 20);

		String allErrors = Validator.allError(errors);

		// Check if there are errors
		if (allErrors != "") {

			Alerts.show("error", "Error!", allErrors, "");

			return;
		}

		int noPost_n = Integer.parseInt(noPost);

		Model model = new Model();

		Post[] posts = model.getPosts(noPost_n, postUser);

		if (posts != null) {
			showTable(posts);
		} else {

			Alerts.show("error", "Error", "No Post was found", "");

		}

		// Close the connection
		model.close();

	}

	public void showTable(Post[] posts) {

		//Clear the table holder
		tableHolder.getChildren().clear();
		
		// Create a TableView
		TableView<Post> tableView = new TableView<>();
		
		tableView.setMaxHeight(Double.MAX_VALUE);
		
		// Create TableColumn instances for each column
		TableColumn<Post, Integer> postIdColumn = new TableColumn<>("Post ID");
		TableColumn<Post, String> postContentColumn = new TableColumn<>("Post Content");
		TableColumn<Post, String> authorColumn = new TableColumn<>("Author");
		TableColumn<Post, Integer> likesColumn = new TableColumn<>("Likes");
		TableColumn<Post, Integer> sharesColumn = new TableColumn<>("Shares");
		TableColumn<Post, String> dateTimeColumn = new TableColumn<>("Date and Time");

		// Set cellValueFactory for each column
		postIdColumn.setCellValueFactory(new PropertyValueFactory<>("postId"));
		postContentColumn.setCellValueFactory(new PropertyValueFactory<>("postContent"));
		authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
		likesColumn.setCellValueFactory(new PropertyValueFactory<>("likes"));
		sharesColumn.setCellValueFactory(new PropertyValueFactory<>("shares"));
		dateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("dateTime"));

		// Set column widths
		postIdColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.1));
		postContentColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.4));
		authorColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.2));
		likesColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.1));
		sharesColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.1));
		dateTimeColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.1));

		// Add the columns to the TableView
		tableView.getColumns().addAll(postIdColumn, postContentColumn, authorColumn, likesColumn, sharesColumn,
				dateTimeColumn);

		// Add sample data to the TableView
		tableView.getItems().addAll(posts);

		tableHolder.getChildren().add(tableView);

	}

}
