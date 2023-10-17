package userapp;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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

public class MenuController {

	public void navigate(Scene scene, User user, String fxid) {

		Scene newScene = null;
		// System.out.println(fxid);
		if (fxid.equals("dashBoardMenu")) {
			newScene = this.showDashBoard(scene, user);
		} else if (fxid.equals("editAccountMenu")) {
			newScene = this.showEditPage(scene, user);
		} else if (fxid.equals("postMenu")) {
			newScene = this.showPost(scene, user);
		} else if (fxid.equals("logoutMenu")) {
			newScene = this.showLogin(scene, user);
		} else if (fxid.equals("nonVipMenu")) {
			this.showSubscription(scene, user);
		}

		if(newScene != null && user != null && user.isVip()) {
			Hyperlink VipLink = (Hyperlink) newScene.lookup("#nonVipMenu");
			if (VipLink != null) {
				VipLink.setId("vipMenu");
				VipLink.setText("**VIP User**");
			}
		}
		
		
		
		
	}

	private Scene showEditPage(Scene scene, User user) {

		Stage stage = (Stage) scene.getWindow();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("edit_account.fxml"));

		Scene newScene = null;

		try {
			newScene = new Scene(loader.load(), scene.getWidth(), scene.getHeight());
			stage.setScene(newScene);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		DashBoardController controller = loader.getController();
		controller.initialize(user); // Pass the login user to the new scene

		controller.setEditPage(); // prefill the edit page

		stage.setTitle("Edit Profile");

		stage.show();
		
		return newScene;

	}

	private Scene showDashBoard(Scene scene, User user) {

		Stage stage = (Stage) scene.getWindow();

		FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboard.fxml"));

		Scene newScene = null;

		try {
			newScene = new Scene(loader.load(), scene.getWidth(), scene.getHeight());
			stage.setScene(newScene);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		DashBoardController controller = loader.getController();
		controller.initialize(user);
		controller.setWelcome();
		stage.setTitle("DashBoard");
		stage.show();
		
		return newScene;

	}

	private Scene showPost(Scene scene, User user) {

		Stage stage = (Stage) scene.getWindow();

		FXMLLoader loader = new FXMLLoader(getClass().getResource("post.fxml"));

		Scene newScene = null;

		try {
			newScene = new Scene(loader.load(), scene.getWidth(), scene.getHeight());
			stage.setScene(newScene);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		PostController controller = loader.getController();
		controller.initialize(user);
		stage.setTitle("Social Media Post");
		stage.show();
		
		return newScene;
		
	}
	
	
	private Scene showSubscription(Scene scene, User user) {

		Stage stage = (Stage) scene.getWindow();

		FXMLLoader loader = new FXMLLoader(getClass().getResource("become_vip.fxml"));

		Scene newScene = null;

		try {
			newScene = new Scene(loader.load(), scene.getWidth(), scene.getHeight());
			stage.setScene(newScene);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		DashBoardController controller = loader.getController();
		controller.initialize(user);
		
		stage.setTitle("Subscribe to become a VIP User");
		stage.show();
		
		return newScene;
		
	}
	
	

	private Scene showLogin(Scene scene, User user) {

		Stage stage = (Stage) scene.getWindow();

		FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));

		Scene newScene = null;

		try {
			newScene = new Scene(loader.load(), scene.getWidth(), scene.getHeight());
			stage.setScene(newScene);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// AuthController controller = loader.getController();
		stage.setTitle("Login to Account");
		stage.show();
		
		return newScene;

	}

}
