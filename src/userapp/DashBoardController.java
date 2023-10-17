package userapp;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Hyperlink;
import javafx.scene.Node;

public class DashBoardController {

	@FXML
	private TextField usernameField;

	@FXML
	private TextField firstNameField;

	@FXML
	private TextField lastNameField;

	@FXML
	private PasswordField passwordField;

	@FXML
	private PasswordField oldPasswordField;

	@FXML
	private Text welcomeText;

	@FXML
	private Label headerLabel;

	@FXML
	private GridPane updatePasswordPanel, updateUserPanel;

	@FXML
	private HBox subscribeHBox1, subscribeHBox2, subscribeHBox3;

	private User user;

	public void initialize(User user) {

		this.user = user;

	}

	// Set the welcome message
	public void setWelcome() {

		String welcome = "Welcome " + user.getFirstName() + " " + user.getLastName();
		welcomeText.setText(welcome);
	}

	public void setEditPage() {
		usernameField.setText(user.getUsername());
		firstNameField.setText(user.getFirstName());
		lastNameField.setText(user.getLastName());
		showUpdateUserPanel();
	}

	public void navigateScene(ActionEvent event) {

		Hyperlink hyperlink = (Hyperlink) event.getSource();
		String menu_id = hyperlink.getId();
		// System.out.println(menu_id);

		Scene scene = ((Node) event.getSource()).getScene();

		MenuController mc = new MenuController();

		mc.navigate(scene, user, menu_id);

	}

	public void showUpdateUserPanel() {

		try {
			headerLabel.setText("Update Account");
			updatePasswordPanel.setVisible(false);
			updateUserPanel.setVisible(true);
		} catch (Exception ex) {
		}

	}

	public void showUpdatePasswordPanel() {

		try {
			headerLabel.setText("Update Passsword");
			updatePasswordPanel.setVisible(true);
			updateUserPanel.setVisible(false);
		} catch (Exception ex) {
		}

	}

	public void updateAccount(ActionEvent event) {

		String username = usernameField.getText();
		String firstName = firstNameField.getText();
		String lastName = lastNameField.getText();

		String errors[] = new String[3];

		errors[0] = Validator.stringLength("Username", username, 3, 20);
		errors[1] = Validator.stringLength("First Name", firstName, 3, 50);
		errors[2] = Validator.stringLength("Last Name", lastName, 3, 50);

		String allErrors = Validator.allError(errors);

		// Check if there are errors
		if (allErrors != "") {

			Alerts.show("error", "Error!", allErrors, "");

			return;
		}

		Model model = new Model();

		// Check if username laready exist
		if (model.usernameExist(user.getUserId(), username)) {
			Alerts.show("error", "Error!", "Username '" + username + "' already exists.", "");
			model.close();
			return;
		}

		if (model.updateUser(user.getUserId(), username, firstName, lastName)) {
			// Update the user object
			this.user = model.getUser(username);
			Alerts.show("info", "Account Updated", "Acount Updated Successfully.", "");
		} else {
			Alerts.show("warning", "Error", "Profile not updated.", "");
		}
		// Close the connection
		model.close();

	}

	public void updatePassword(ActionEvent event) {

		String password = passwordField.getText();
		String oldPassword = oldPasswordField.getText();

		String errors[] = new String[2];

		errors[0] = Validator.stringLength("Old Password", oldPassword, 1, 20);
		errors[1] = Validator.stringLength("New Password", password, 6, 50);

		String allErrors = Validator.allError(errors);
		// Check if there are errors
		if (allErrors != "") {
			Alerts.show("error", "Error!", allErrors, "");
			return;
		}

		Model model = new Model();

		// Check if Old password is correct
		if (model.getUser(user.getUsername(), oldPassword) == null) {

			Alerts.show("warning", "Error!", "Old Password is incorrect", "");
			return;
		}

		if (model.updatePassword(user.getUserId(), password)) {
			Alerts.show("info", "Account Updated", "Password Updated Successfully.", "");
			passwordField.setText("");
			oldPasswordField.setText("");
		} else {
			Alerts.show("warning", "Error", "Password not updated.", "");
		}
		// Close the connection
		model.close();

	}

	// Become VIP USer
	public void subscribe() {

		Model model = new Model();

		model.updateUserVip(user.getUserId(), 1);
		Alerts.show("info", "Accoun Update", "Your are now a VIP Member.", "To see effect log out and log in again");
		model.close();

		subscribeHBox1.setVisible(false);
		subscribeHBox2.setVisible(false);
		subscribeHBox3.setVisible(true);
		
		// Make the user a vip user
		user.setVip(true);

	}

}
