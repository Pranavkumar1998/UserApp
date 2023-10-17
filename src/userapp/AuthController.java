package userapp;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.Node;

public class AuthController {

	@FXML
	private TextField usernameField;

	@FXML
	private TextField firstNameField;

	@FXML
	private TextField lastNameField;

	@FXML
	private PasswordField passwordField;

	public void initialize() {
//        String javaVersion = System.getProperty("java.version");
//        String javafxVersion = System.getProperty("javafx.version");
//        label.setText("Hello, JavaFX " + javafxVersion + "\nRunning on Java " + javaVersion + ".");
	}

	public void showSignUp(ActionEvent event) {

		Scene scene = ((Node) event.getSource()).getScene();
		Stage stage = (Stage)scene.getWindow();

		Parent root = null;
		try {
			root = FXMLLoader.load(getClass().getResource("signup.fxml"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		stage.setTitle("Create an Account");
		stage.setScene(new Scene(root, scene.getWidth(), scene.getHeight()));
		stage.show();

	}

	public void showLogin(ActionEvent event) {

		Scene scene = ((Node) event.getSource()).getScene();
		Stage stage = (Stage)scene.getWindow();

		Parent root = null;
		try {
			root = FXMLLoader.load(getClass().getResource("login.fxml"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		stage.setTitle("Create an Account");
		stage.setScene(new Scene(root, scene.getWidth(), scene.getHeight()));
		stage.show();

	}

	
	
	public void handleUserLogin(ActionEvent event) {

		String username = usernameField.getText();
		String password = passwordField.getText();

		
		Model model = new Model();
		
		 User user =  model.getUser(username, password);		
		 if(user == null) {
			 	
			 Alerts.show("warning", "Login Error!",  "Wrong login credentials", "");
			 return;
		 }
		
		model.close();
		
		
				
		Scene scene = ((Node) event.getSource()).getScene();

		// Switch to Dashboard
		MenuController mc = new MenuController();
		mc.navigate(scene, user, "dashBoardMenu");

		
	}
	
	
	public void createAccount(ActionEvent event) {

		String username = usernameField.getText();
		String firstName = firstNameField.getText();
		String lastName = lastNameField.getText();
		String password = passwordField.getText();

		String errors[] = new String[4];

		errors[0] = Validator.stringLength("Username", username, 3, 20);
		errors[1] = Validator.stringLength("First Name", firstName, 3, 50);
		errors[2] = Validator.stringLength("Last Name", lastName, 3, 50);
		errors[3] = Validator.stringLength("Password", password, 6, 20);		
		
		String allErrors = Validator.allError(errors);
		
		// Check if there are errors
		if(allErrors != "") {
			
			Alerts.show("error", "Error!", allErrors, "");
			
			return;
		}
		
		Model model = new Model();
		
		// Check if username laready exist
		if(model.usernameExist(username)) {
			Alerts.show("error", "Error!", "Username '"+username+"' already exists.", "");
			model.close();
			return;
		}
		
		model.insertUser(username, firstName, lastName, password);
		
		// Close the connection
		model.close();

		// Clear form
		usernameField.setText("");
		firstNameField.setText("");
		lastNameField.setText("");
		passwordField.setText("");
		
		Alerts.show("info", "Account Created",  "Acount Created Successfully. You can now login", "");

		
	}

	

}
