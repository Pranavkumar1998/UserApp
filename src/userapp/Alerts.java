package userapp;

import javafx.fxml.FXML;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;

public class Alerts {

    
    public static void show(String type, String title, String header, String content) {    	
    
    	Alert alert;
    	if(type == "info") {
    		alert = new Alert(AlertType.INFORMATION);
    	}
    	else if(type == "warning") {
    		alert = new Alert(AlertType.WARNING);
    	}
    	else if(type == "error") {
    		alert = new Alert(AlertType.ERROR);
    	}
    	else {
    		alert = new Alert(AlertType.NONE);
    	}
    	if(title != "") {
    		alert.setTitle(title);
    	}
    	if(header != "") {
    		alert.setHeaderText(header);
    	}
    	if(content != "") {
    		alert.setContentText(content);
    	}
        
        // Show the alert and wait for the user's response
        alert.showAndWait().ifPresent(response -> {
               // System.out.println("OK button clicked");
        });
    	

    }
         
    
    
}