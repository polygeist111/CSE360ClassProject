package application;

import javafx.application.Application;
import javafx.geometry.Insets; 
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene; 
import javafx.scene.control.*;
import javafx.scene.layout.*; 
import javafx.scene.text.*; 
import javafx.scene.paint.*;
import javafx.scene.paint.Color;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.stage.Stage;

import java.util.*;

public class ViewController extends Application{
	private static Map<String, Screen> screens = new HashMap<String, Screen>();
	private static Stage stage;
	//private static User currentUser;
		//store currently logged in user once authenticated
	
	@Override
	public void start(Stage stage) {
		//create canvas
		try {/*
			final Scene loginScreen = new Scene(new VBox());
			//home page
			final Scene userProfileScreen = new Scene(new VBox());
			//buy page
			//cart page
			//sell page
			//admin page
			loginScreen.setRoot(new LoginBuilder(() -> primaryStage.setScene(userProfileScreen)).build());
			loginScreen.setRoot(new ProfileBuilder(() -> primaryStage.setScene(userProfileScreen)).build());
			primaryStage.setScene(loginScreen);
			primaryStage.show();
			*/
			ViewController.stage = stage;
			//Scene loginScreen = new LoginScreen().screen;
			screens.put("Login", new LoginScreen());
			screens.put("Home", new HomeScreen());
			//stage.setScene(loginScreen);
			
			//apply styles to all screens
			for (Screen screen : screens.values()) {
				screen.screen.getStylesheets().add("styles.css");
			}
			
			stage.setScene(screens.get("Login").screen);
			stage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	//if user is signed in as general user, moves to home screen
	public static void goHome() {
		System.out.println("going home");
		
		//CODE: add check for general user authentication
		if (screens.get("Home") != null) {
			stage.setScene(screens.get("Home").screen);
		}
	}
	
	//if user is signed in as general user and in home screen or cart screen, moves to buying screen
	public static void goShopping() {
		System.out.println("going to buy screen");
	}
	
	//if user is signed in as general user and in buying screen, moves to cart screen
	public static void goCart() {
		System.out.println("going to cart");
	}
	
	//if user is signed in as general user and in home screen, moves to selling screen
	public static void goSelling() {
		System.out.println("going to sell screen");
	}
	
	//if user is signed in as general user and in home screen, moves to profile screen
	public static void goProfile() {
		System.out.println("going to profile screen");
	}
	
	//if user is signed in at all, sign out and return them to login screen
	public static void signOut() {
		System.out.println("signing out");
		
		//CODE: add check for user authentication
		if (screens.get("Login") != null) {
			stage.setScene(screens.get("Login").screen);
		}
	}

	
	public static void main(String[] args) {
		launch(args);
	}

}
