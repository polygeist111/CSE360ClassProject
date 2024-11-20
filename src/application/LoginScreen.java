package application;

import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.Scene; 
import javafx.scene.control.*;
import javafx.scene.layout.*; 
import javafx.scene.text.*; 
import javafx.scene.paint.*;
import javafx.scene.paint.Color;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.Event;
import javafx.stage.Stage;

import java.util.*;

public class LoginScreen extends Screen{	
	
	LoginScreen () {
		assembleHeader();
		assembleContent();
		assembleFooter();
		screen = new Scene(root);
	};

	protected void assembleHeader() {
		HBox header = createHeader();
		root.add(header, 0, 0);
		root.add(createTitle("Log In"), 0, 0);
	}
	
	protected void assembleContent() {
		GridPane content = createContentWindow();
		
		//create grid holding login objects
		GridPane loginGrid = new GridPane();
		loginGrid.setVgap(5);
		loginGrid.setHgap(5);
		
		//create username label and entry
		Label userLabel = new Label("Username:");
		userLabel.setFont(regFont);
		TextField userField = new TextField();
		loginGrid.add(userLabel, 0, 0);
		loginGrid.add(userField, 1, 0);
		
		//create password label and entry
		Label passLabel = new Label("Password:");		
		passLabel.setFont(regFont);
		PasswordField passField = new PasswordField();
		loginGrid.add(passLabel, 0, 1);
		loginGrid.add(passField, 1, 1);

		//create user feedback label
		Label loginFeedback = new Label("Enter credentials to proceed");
		loginFeedback.setFont(Font.font("Verdana", FontWeight.NORMAL, 12));
		loginFeedback.setManaged(false);
		loginGrid.add(loginFeedback, 0, 2, 2, 1);
		
		//create "log in" submit button
		Button loginButton = new Button("Log In");
		loginGrid.add(loginButton, 0, 3);
		
		//define log in behavior
		//CODE: edit heavily once database is up
		//ping db for auth, more precisely validate submitted credentials, prevent against sql injection, etc.
		loginButton.setOnAction(event -> {
			String userIn = userField.getText().toString();
			String passIn = passField.getText().toString();
			//System.out.println(userIn + " " + passIn);
			
			ViewController.currentUser = DBMediator.authUser(userIn, passIn);
			if (ViewController.currentUser != null) {
				System.out.println("Successful Login");
				userField.setStyle("-fx-border-color: green");
				passField.setStyle("-fx-border-color: green");
				//ViewController.User = <ping db for current user object>;
				ViewController.goHome("Login");
				return;
			}
			
			String feedback = "";
			
			if (userField.getText().isEmpty()) {
				feedback += "Enter an email";
				userField.setStyle("-fx-border-color: red");
			} else {
				userField.setStyle("-fx-border-color: none");
			}
			
			if (passField.getText().isEmpty()) {
				if (!feedback.isBlank()) {
					feedback += "\n";
				}
				feedback += "Enter a password";
				passField.setStyle("-fx-border-color: red");
			} else {
				passField.setStyle("-fx-border-color: none");
			}
			
			if (feedback.isBlank()) {
				feedback += "Username or password is incorrect";
				userField.setStyle("-fx-border-color: red");
				passField.setStyle("-fx-border-color: red");
				
			}
			
			loginFeedback.setText(feedback);
			loginFeedback.setManaged(true);
			
			System.out.println("Login Failed:");
			System.out.println(feedback);
			
		});
		

		content.add(loginGrid, 0, 0);
		
		root.add(content, 0, 1);

	}
	
	protected void assembleFooter() {
		root.add(createFooter(), 0, 2);
		Text credit = new Text("Made by CSE 360 Group 16");
		credit.setFont(Font.font("Verdana", FontWeight.NORMAL, 10));
		HBox creditFrame = new HBox(credit);
		creditFrame.setPadding(new Insets(5));
		creditFrame.setAlignment(Pos.BOTTOM_RIGHT);
		root.add(creditFrame, 0, 2);
	}
}
