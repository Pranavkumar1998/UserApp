package userapp;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;

/**
 * This class is the controller for the authentication screen of the JavaFX application.
 */
public class AuthController {

    @FXML
    private TextField usernameField;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private PasswordField passwordField;

    /**
     * Initialize the controller.
     */
    public void initialize() {
        // Initialization code can be added here if needed.
    }

    /**
     * Switch to the sign-up screen when the "Sign Up" button is clicked.
     *
     * @param event The action event triggered by the button click.
     */
    public void showSignUp(ActionEvent event) {
        Scene scene = ((Node) event.getSource()).getScene();
        Stage stage = (Stage) scene.getWindow();

        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("signup.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        stage.setTitle("Create an Account");
        stage.setScene(new Scene(root, scene.getWidth(), scene.getHeight()));
        stage.show();
    }

    /**
     * Switch to the login screen when the "Login" button is clicked.
     *
     * @param event The action event triggered by the button click.
     */
    public void showLogin(ActionEvent event) {
        Scene scene = ((Node) event.getSource()).getScene();
        Stage stage = (Stage) scene.getWindow();

        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("login.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        stage.setTitle("Create an Account");
        stage.setScene(new Scene(root, scene.getWidth(), scene.getHeight()));
        stage.show();
    }

    /**
     * Handle user login when the "Login" button is clicked.
     *
     * @param event The action event triggered by the button click.
     */
    public void handleUserLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        Model model = new Model();
        User user = model.getUser(username, password);

        if (user == null) {
            Alerts.show("warning", "Login Error!", "Wrong login credentials", "");
            return;
        }

        model.close();

        Scene scene = ((Node) event.getSource()).getScene();

        // Switch to Dashboard
        MenuController mc = new MenuController();
        mc.navigate(scene, user, "dashBoardMenu");
    }

    /**
     * Create a user account when the "Create Account" button is clicked.
     *
     * @param event The action event triggered by the button click.
     */
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
        if (!allErrors.isEmpty()) {
            Alerts.show("error", "Error!", allErrors, "");
            return;
        }

        Model model = new Model();

        // Check if username already exists
        if (model.usernameExist(username)) {
            Alerts.show("error", "Error!", "Username '" + username + "' already exists.", "");
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

        Alerts.show("info", "Account Created", "Account Created Successfully. You can now login", "");
        
    
    }
    
}
