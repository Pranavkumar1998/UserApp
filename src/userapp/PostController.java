package userapp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
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

	@FXML
	CheckBox ignoreImportError;
	
	@FXML
	CheckBox firstRowHeaderField;
	
	@FXML
	Text importReportText;

	
	ComboBox<String> postUserField;

	@FXML
	Label headerLabel;

	@FXML
	Hyperlink vipAccessSubMenu1, vipAccessSubMenu2;

	@FXML
	GridPane addPostPanel;

	@FXML
	VBox retrievePostPanel, importPostPanel, chartHolderPanel;

	@FXML
	HBox usernameListHolder;

	@FXML
	GridPane blankPanel;

	@FXML
	private VBox tableHolder;

	private User user;

	
	/**
     * Initialize the controller.
     */
	public void initialize(User user) {

		this.user = user;
		blankPanel.setVisible(true);
		if (user.isVip()) {
			vipAccessSubMenu1.setVisible(true);
			vipAccessSubMenu2.setVisible(true);
		}

		this.loadUsernames();
	}
	

	/**
     * method for menu navigation.
     */
	public void navigateScene(ActionEvent event) {

		Hyperlink hyperlink = (Hyperlink) event.getSource();
		String menu_id = hyperlink.getId();
		// System.out.println(menu_id);

		Scene scene = ((Node) event.getSource()).getScene();

		MenuController mc = new MenuController();

		mc.navigate(scene, user, menu_id);

	}

	
	/**
     * Create a Combobox for user selection
     * use during post retrieval for a user
     */
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

	/**
     * Switch to the post retrieval Panel
     */
	public void showRetrievePostPanel() {

		retrievePostPanel.setVisible(true);
		addPostPanel.setVisible(false);
		blankPanel.setVisible(false);
		importPostPanel.setVisible(false);
		chartHolderPanel.setVisible(false);

		headerLabel.setText("Retrieve Post(s)");

	}

	/**
     * Switch to the Add pot panel
     */
	public void showAddPostPanel() {

		addPostPanel.setVisible(true);
		retrievePostPanel.setVisible(false);
		blankPanel.setVisible(false);
		importPostPanel.setVisible(false);
		chartHolderPanel.setVisible(false);

		// Set the page header
		headerLabel.setText("Add Post");

	}
	
	/**
     * Switch to the Post Import Panel .
     */
	public void showImportPostPanel() {

		addPostPanel.setVisible(false);
		retrievePostPanel.setVisible(false);
		blankPanel.setVisible(false);
		importPostPanel.setVisible(true);
		chartHolderPanel.setVisible(false);

		headerLabel.setText("Bulk Import - VIP");

	}
	
	/**
     * Switch to the Chart Panel .
     */
	public void showChartHolderPanel() {

		addPostPanel.setVisible(false);
		retrievePostPanel.setVisible(false);
		blankPanel.setVisible(false);
		importPostPanel.setVisible(false);
		chartHolderPanel.setVisible(true);
		
		headerLabel.setText("Date Visualization of Post Shares");
		
		this.drawChart();

	}
	
	
	

	/**
     * Function to delete Post .
     */
	public void deletePost(ActionEvent event) {

		addPostPanel.setVisible(false);
		retrievePostPanel.setVisible(false);
		blankPanel.setVisible(true);
		importPostPanel.setVisible(false);
		chartHolderPanel.setVisible(false);

		headerLabel.setText("Delete Post");

		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Delete Post");
		dialog.setHeaderText("Please enter Post ID:");
		dialog.setContentText("postId:");

		Optional<String> result = dialog.showAndWait();
		result.ifPresent(postId -> {

			String error = Validator.isInt("Post Id", postId);
			if (! error.isEmpty()) {
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

	/**
     * Method to export post .
     */
	public void exportPost(ActionEvent event) {

		addPostPanel.setVisible(false);
		retrievePostPanel.setVisible(false);
		blankPanel.setVisible(true);
		importPostPanel.setVisible(false);
		chartHolderPanel.setVisible(false);

		headerLabel.setText("Export Post");

		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Export Post");
		dialog.setHeaderText("Please enter Post ID:");
		dialog.setContentText("postId:");

		Optional<String> result = dialog.showAndWait();
		result.ifPresent(postId -> {

			String error = Validator.isInt("Post Id", postId);
			if (!error.isEmpty()) {
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

	/**
     * Method to create Post .
     */
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
		if (!allErrors.isEmpty()) {

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

		if (model.insertPost(user.getUserId(), postId_n, postContent, author, likes_n, shares_n, "" )) {

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

		if (! error.isEmpty()) {

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
		if (!allErrors.isEmpty()) {

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

	
	
	public void importPost(ActionEvent event) {
		
		Scene scene = ((Node) event.getSource()).getScene();
		Stage stage = (Stage) scene.getWindow();
		
		Button importButton = (Button) event.getSource();
		
		importButton.setVisible(false);
	
		boolean shouldIgnoreImportError = ignoreImportError.isSelected();
		
		int lineNo = 0;
		int importedLine = 0;
		boolean hasError = false;
		
	    FileChooser fileChooser = new FileChooser();
	    fileChooser.setTitle("Open Resource File");
	    fileChooser.getExtensionFilters().addAll(
	            new ExtensionFilter("Text Files", "*.txt"),
	            new ExtensionFilter("CSV Files", "*.csv")
	    );

	    File selectedFile = fileChooser.showOpenDialog(stage);
	    
	    if (selectedFile != null) {
	        try (BufferedReader br = new BufferedReader(new FileReader(selectedFile))) {
	            String line;
	            Model model = new Model();
	            model.startTransaction();

	            while ((line = br.readLine()) != null) {
	            	lineNo++;
	            	
	            	// First row is header
	            	if(lineNo == 1 && firstRowHeaderField.isSelected()) {
	            		continue;
	            	}
	            	
	                String[] csvline = line.split(","); // Assuming CSV format

	                // Check if the CSV line has the expected number of elements
	                if (csvline.length < 6) {
	                	showImportReport("error",  "Invalid CSV format at line: " + lineNo);
	                    model.rollback();
	                    hasError = true;
	                    if(! shouldIgnoreImportError) {
	                    	break;
	                    }
	                    else {
	                    	continue;
	                    }
	                }

	                String postId = csvline[0].trim();
	                String postContent = csvline[1].trim();
	                String author = csvline[2].trim();
	                String likes = csvline[3].trim();
	                String shares = csvline[4].trim();
	                String createdAt = csvline[5].trim();
	                
	                //Sometimes there might be empty rows
	                if(postId.equals("") && postContent.equals("") && author.equals("") && likes.equals("") && shares.equals("") && createdAt.equals("")){
	                	continue;
	                }
	               
	                

	                String errors[] = new String[6];
	                errors[0] = Validator.isInt("Post Id", postId);
	                errors[1] = Validator.stringLength("Post Content", postContent, 10, 500000);
	                errors[2] = Validator.stringLength("Author", author, 2, 50);
	                errors[3] = Validator.isInt("Likes", likes);
	                errors[4] = Validator.isInt("Shares", shares);
	                errors[5] = Validator.stringLength("Created at", createdAt, 8, 30);

	                String allErrors = Validator.allError(errors);

	                // Check if there are errors
	                if (!allErrors.isEmpty()) {
	                	showImportReport("error",  "Error! at line: "+  lineNo + " \n " + allErrors);
	                    model.rollback();
	                    hasError = true;
	                    if(! shouldIgnoreImportError) {
	                    	break;
	                    }
	                    else {
	                    	continue;
	                    }
	                }

	                int postId_n = Integer.parseInt(postId);
	                int likes_n = Integer.parseInt(likes);
	                int shares_n = Integer.parseInt(shares);
	                
	                
	                // Check if post id already exists
	                if (model.postIdExist(postId_n)) {
	                	showImportReport("error",  "Post Id: '" + postId + "' already exists at line: "+  lineNo + " \n " + allErrors);
	                    model.rollback();
	                    hasError = true;
	                    if(! shouldIgnoreImportError) {
	                    	break;
	                    }
	                    else {
	                    	continue;
	                    }
	                }

	                // Insert the post data into the database
	                if (model.insertPost(user.getUserId(), postId_n, postContent, author, likes_n, shares_n, createdAt)) {
	                    
	                	importedLine++;
	                	showImportReport("success",  "Importing... ("+ importedLine + "post imported)");
	                
	                } else {
	                	showImportReport("error",  "Database Error. Row not saved at line: "+  lineNo);
	                    model.rollback();
	                    hasError = true;
	                    if(! shouldIgnoreImportError) {
	                    	break;
	                    }
	                    else {
	                    	continue;
	                    }
	                }
	            }
	            
	            if(importedLine > 0 && !hasError) {
	            	model.commit();
	            	showImportReport("complete",  "Import Completed ("+ importedLine + " post imported)");
	            }
	            else if(importedLine > 0 && shouldIgnoreImportError) {
	            	model.commit();
	            	showImportReport("complete",  "Import Completed ("+ importedLine + " post imported)");
	            }
	            
	            else if(importedLine == 0 && !hasError) {
	            	showImportReport("complete",  "No post imported");
	            }

	            // Close the connection
	            model.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	            showImportReport("error",  "Failed to read the file.");
	        }
	        
	    }
	    importButton.setVisible(true);
	}	
	
	
	private void showImportReport(String type, String message) {
		
		importReportText.setText("");
		
		message = "\n" + message;
		
		if(type == "error") {
			message = message + "\n" + " Rolled back, No row saved";
			importReportText.setStyle("-fx-fill: red; -fx-font-style: italic; -fx-font-size: 14;");
			importReportText.setText(message);
//			Alerts.show("error", "Import Error!", message, "");
		}
		else if(type == "success") {
			importReportText.setStyle("-fx-fill: blue; -fx-font-style: italic; -fx-font-size: 14;");
			importReportText.setText(message);
		}
		
		else if(type == "complete") {
			importReportText.setStyle("-fx-fill: green; -fx-font-style: normal; -fx-font-size: 16;");
			importReportText.setText(message);
			Alerts.show("info", "Import Completedr!", message, "");
		}
		
		
	}
	

	
	private  void drawChart() {
		
		//Clear the chart holder
		chartHolderPanel.getChildren().clear();
				
		Model model = new Model();
		
		int postShare_0_99 = model.getPostShare(0, 99);
		int postShare_100_999 = model.getPostShare(100, 999);
		int postShare_1000_plus = model.getPostShare(1000, -1);
		
		
		model.close();
		
		ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                new PieChart.Data("#Share between 0 and 99", postShare_0_99),
                new PieChart.Data("#Share between 100 and 999", postShare_100_999),
                new PieChart.Data("#Share equal and above 1000", postShare_1000_plus));
        final PieChart chart = new PieChart(pieChartData);
       
        chart.setTitle("Post #Share Distribution");
		
        chartHolderPanel.getChildren().add(chart);
		
	}	
	
	
	
	private void showTable(Post[] posts) {

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
