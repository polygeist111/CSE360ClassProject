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

public class ViewController extends Application {
    private static Map<String, Screen> screens = new HashMap<String, Screen>();
    private static Stage stage;
    //store currently logged in user once authenticated
    public static User currentUser;
    
    @Override
    public void start(Stage stage) {
        //create canvas
        try {
            DBMediator.initDB();
            ViewController.stage = stage;
            screens.put("Login", new LoginScreen());
            screens.put("Home", new HomeScreen());
            screens.put("Profile", new ProfileScreen());
            
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
        if (clearScreen("Home", null) && screens.get("Home") != null) {
            stage.setScene(screens.get("Home").screen);
        } else {
            System.out.println("No home screen");
        }
    }
    
    //if user is signed in as general user and in home screen or cart screen, moves to buying screen
    public static void goShopping(Map<Integer, HBox> cartList) {
        System.out.println("going to buy screen");
        if (clearScreen("Buy", cartList) && screens.get("Buy") != null) {
            stage.setScene(screens.get("Buy").screen);
        } else {
            System.out.println("No buy screen");
        }
    }
    
    //if user is signed in as general user and in buying screen, moves to cart screen
    public static void goCart(Map<Integer, HBox> cartList) {
        System.out.println("going to cart");
        if (clearScreen("Cart", cartList) && screens.get("Cart") != null) {
            stage.setScene(screens.get("Cart").screen);
        } else {
            System.out.println("No cart screen");
        }
    }
    
    //if user is signed in as general user and in home screen, moves to selling screen
    public static void goSelling() {
        System.out.println("going to sell screen");
        if (clearScreen("Sell", null) && screens.get("Sell") != null) {
            stage.setScene(screens.get("Sell").screen);
        } else {
            System.out.println("No sell screen");
        }
    }
    
    //if user is signed in as general user and in home screen, moves to profile screen
    public static void goProfile() {
        System.out.println("going to profile screen");
        if (clearScreen("Profile", null) && screens.get("Profile") != null) {
            stage.setScene(screens.get("Profile").screen);
        } else {
            System.out.println("No profile screen");
        }
    }
    
    //if user is signed in at all, sign out and return them to login screen
    public static void signOut() {
        System.out.println("signing out");
        //CODE: add check for user authentication
        if (clearScreen("Login", null) && screens.get("Login") != null) {
            currentUser = null;
            stage.setScene(screens.get("Login").screen);
        } else {
            System.out.println("No login screen");
        }
    }
    
    //clear + regenerate exited screen
    public static boolean clearScreen(String caller, Map<Integer, HBox> cartList) {
        Screen newScreen = null;
        switch (caller) {
            case "Login":
                newScreen = new LoginScreen();
                break;
            case "Home":
                newScreen = new HomeScreen();
                break;
            case "Buy":
                if (cartList != null) {
                    newScreen = new BuyerScreen(cartList);
                } else {
                    newScreen = new BuyerScreen(null);
                }
                break;
            case "Cart":
                newScreen = new CartScreen(cartList);
                break;
            case "Sell":
                newScreen = new SellerScreen();
                break;
            case "Profile":
                newScreen = new ProfileScreen();
                break;
            case "Admin":
                //newScreen = new AdminScreen();
                break;
            default:
                System.out.println("Caller " + caller + " not found");
                return false;
        }
        if (newScreen != null) {
            newScreen.screen.getStylesheets().add("styles.css");
            screens.put(caller, newScreen);
        }
        return true;
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}