package application;

import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.*;
import java.util.ArrayList;
import java.sql.*;

public class ProfileScreen extends Screen {
    private ListView<String> listingsList;
    private ListView<String> purchasesList;
	private GridPane historyColumns;
    

    public ProfileScreen() {
        assembleHeader();
        assembleContent();
        assembleFooter();
        screen = new Scene(root);
        //loadUserActivity();
    }

    @Override
    protected void assembleHeader() {
        HBox header = createHeader();
        root.add(header, 0, 0);

        HBox titleBox = new HBox(10);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(0, 30, 0, 30));

        Label title = createTitle("User Profile");
        Region spacer = createSpacer();
        Button homeButton = createReturnHomeButton();
        Button signOutButton = createSignOutButton();

        titleBox.getChildren().addAll(title, spacer, homeButton, signOutButton);
        root.add(titleBox, 0, 0);
    }

    @Override
    protected void assembleContent() {
        GridPane content = createContentWindow();
        content.setVgap(20);

        VBox profileBox = new VBox(15);
        profileBox.setAlignment(Pos.TOP_CENTER);
        profileBox.setPadding(new Insets(20));

        HBox userInfo = new HBox();
        userInfo.setAlignment(Pos.CENTER_LEFT);

        HBox usernameBox = new HBox(10);
        Label usernameLabel = new Label("Username:");
        usernameLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        Label usernameValue = new Label(ViewController.currentUser != null ?
                ViewController.currentUser.getUsername() : "N/A");
        usernameBox.getChildren().addAll(usernameLabel, usernameValue);

        HBox statusBox = new HBox(10);
        Label statusLabel = new Label("Status:");
        statusLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        Label statusValue = new Label(ViewController.currentUser != null ?
                ViewController.currentUser.getStatus() : "N/A");
        statusBox.getChildren().addAll(statusLabel, statusValue);

        HBox idBox = new HBox(10);
        Label idLabel = new Label("User ID:");
        idLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        Label idValue = new Label(ViewController.currentUser != null ?
                String.valueOf(ViewController.currentUser.getUserID()) : "N/A");
        idBox.getChildren().addAll(idLabel, idValue);

        userInfo.getChildren().addAll(createSpacer(), usernameBox, createSpacer(), statusBox, createSpacer(), idBox, createSpacer());

        VBox buttonsBox = new VBox(10);
        buttonsBox.setAlignment(Pos.CENTER);
        buttonsBox.setPadding(new Insets(20, 0, 0, 0));

        Button changePasswordButton = new Button("Change Password");
        Button refreshButton = new Button("Refresh Activity");
        Button debugButton = new Button("Debug Listings");

        changePasswordButton.setMinWidth(150);
        refreshButton.setMinWidth(150);
        debugButton.setMinWidth(150);

        changePasswordButton.setOnAction(e -> showChangePasswordDialog());
        refreshButton.setOnAction(e -> loadUserActivity());
        debugButton.setOnAction(e -> {
            System.out.println("Current User ID: " + ViewController.currentUser.getUserID());
            ArrayList<Object> listings = DBMediator.queryCurrentListings("sellerid",
                    String.valueOf(ViewController.currentUser.getUserID()));
            if (listings != null) {
                System.out.println("Found " + listings.size() + " items");
                for (Object obj : listings) {
                    System.out.println(obj.getClass().getName() + ": " + obj.toString());
                }
            } else {
                System.out.println("No listings found");
            }
        });

        //buttonsBox.getChildren().addAll(changePasswordButton, refreshButton, debugButton);
        /*
        VBox activityBox = new VBox(10);
        activityBox.setAlignment(Pos.CENTER_LEFT);
        activityBox.setPadding(new Insets(20, 0, 0, 0));

        VBox listingsBox = new VBox(10);
        listingsBox.setPadding(new Insets(10));
        Label listingsTitle = new Label("My Listings");
        listingsTitle.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        listingsList = new ListView<>();
        listingsList.setPrefHeight(200);
        listingsBox.getChildren().addAll(listingsTitle, listingsList);

        VBox purchasesBox = new VBox(10);
        purchasesBox.setPadding(new Insets(10));
        Label purchasesTitle = new Label("Purchase History");
        purchasesTitle.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        purchasesList = new ListView<>();
        purchasesList.setPrefHeight(200);
        purchasesBox.getChildren().addAll(purchasesTitle, purchasesList);

        activityBox.getChildren().addAll(listingsBox, purchasesBox);

        profileBox.getChildren().addAll(userInfo, buttonsBox, activityBox);

        content.add(profileBox, 0, 0);*/
        
        historyColumns = new GridPane();
		historyColumns.setHgap(30);
		browseCategory();
		
		
		;
		content.add(userInfo, 0, 0);
		content.add(historyColumns, 0, 1);
        root.add(content, 0, 1);
    }

    @Override
    protected void assembleFooter() {
        root.add(createFooter(), 0, 2);
        Text credit = new Text("Made by CSE 360 Group 16");
        credit.setFont(Font.font("Verdana", FontWeight.NORMAL, 10));
        HBox creditFrame = new HBox(credit);
        creditFrame.setPadding(new Insets(5));
        creditFrame.setAlignment(Pos.BOTTOM_RIGHT);
        root.add(creditFrame, 0, 2);
    }

    private void loadUserActivity() {
        if (ViewController.currentUser == null) {
            System.out.println("No current user!");
            return;
        }

        listingsList.getItems().clear();
        purchasesList.getItems().clear();

        ArrayList<Object> listings = DBMediator.getProfileListings(ViewController.currentUser.getUserID());

        if (listings != null) {
            for (int i = 0; i < listings.size(); i += 2) {
                try {
                    Listing listing = (Listing) listings.get(i);
                    Book book = (Book) listings.get(i + 1);

                    String listingText = String.format("%s by %s - Quantity: %d - Value: $%.2f",
                            book.getTitle(),
                            book.getAuthor(),
                            listing.getQuantity(),
                            book.getValue() / 100.0);

                    listingsList.getItems().add(listingText);

                } catch (Exception e) {
                    System.out.println("Error processing listing at index " + i);
                    e.printStackTrace();
                }
            }
        }

        loadPurchases();
    }

    private void loadPurchases() {
        if (ViewController.currentUser == null) return;

        try (Connection c = DriverManager.getConnection("jdbc:sqlite:src/DB/bookstore.db");
             Statement stmt = c.createStatement()) {

            String sql = "SELECT el.*, b.title, b.author, u.username as seller " +
                    "FROM executedListings el " +
                    "JOIN books b ON el.bookid = b.bookid " +
                    "JOIN users u ON el.sellerid = u.userid " +
                    "WHERE el.buyerid = " + ViewController.currentUser.getUserID();

            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                String purchaseText = String.format("%s by %s - Quantity: %d - Price: $%.2f - Seller: %s",
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getInt("quantity"),
                        rs.getDouble("saleprice") / 100.0,
                        rs.getString("seller"));
                purchasesList.getItems().add(purchaseText);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showChangePasswordDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Change Password");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        PasswordField currentPassword = new PasswordField();
        PasswordField newPassword = new PasswordField();
        PasswordField confirmPassword = new PasswordField();

        grid.add(new Label("Current Password:"), 0, 0);
        grid.add(currentPassword, 1, 0);
        grid.add(new Label("New Password:"), 0, 1);
        grid.add(newPassword, 1, 1);
        grid.add(new Label("Confirm Password:"), 0, 2);
        grid.add(confirmPassword, 1, 2);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                if (!newPassword.getText().equals(confirmPassword.getText())) {
                    showAlert("Error", "New passwords don't match!");
                } else {
                    showAlert("Info", "Password change functionality to be implemented");
                }
            }
            return dialogButton;
        });

        dialog.showAndWait();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
  //queries db for category results and updates screen
  	private void browseCategory() {
  		int userID = ViewController.currentUser.getUserID();
  		ArrayList<ListView<HBox>> buyColumns = new ArrayList<>();
  		ListView<HBox> ulnColumn = createBookColumn(DBMediator.queryListingsForUser("executedlistings", "buyerid", userID), "Bought: ", null);
  		buyColumns.add(ulnColumn);
  		historyColumns.add(ulnColumn, 0, 0);
  		ListView<HBox> muColumn = createBookColumn(DBMediator.queryListingsForUser("executedlistings", "sellerid", userID), "Sold: ", null);
  		buyColumns.add(muColumn);
  		historyColumns.add(muColumn, 1, 0);
  		ListView<HBox> huColumn = createBookColumn(DBMediator.queryListingsForUser("currentlistings", "sellerid", userID), "Listed: ", null);
  		buyColumns.add(huColumn);
  		historyColumns.add(huColumn, 2, 0);
  		
  		//special selection handling
  		for (ListView<HBox> child : buyColumns) {
  			child.getSelectionModel().selectedItemProperty().addListener(
  				new ChangeListener<HBox>() {
  			    	public void changed(ObservableValue<? extends HBox> ov, HBox old_val, HBox new_val) {
  			    		Platform.runLater(() -> child.getSelectionModel().clearSelection() );
  			        }
  			    }
  			);
  		}
  		
  	}
}
