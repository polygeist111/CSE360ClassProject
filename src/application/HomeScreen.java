package application;

import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.image.*;
import javafx.scene.Scene; 
import javafx.scene.control.*;
import javafx.scene.layout.*; 
import javafx.scene.text.*; 
import javafx.scene.paint.*;
import javafx.scene.paint.Color;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.stage.Stage;

import java.util.*;

public class HomeScreen extends Screen{	
	
	HomeScreen () {
		assembleHeader();
		assembleContent();
		assembleFooter();
		screen = new Scene(root);
	};
	
	protected void assembleHeader() {
		HBox header = createHeader();
		header.getChildren().add(createSpacer());
		header.getChildren().add(createSignOutButton());
		root.add(header, 0, 0);
		root.add(createTitle("Home"), 0, 0);
	}
	
	protected void assembleContent() {
		GridPane content = createContentWindow();
		/*
		int iconDim = 120;
		
		//Assemble Buy Icon Button
		VBox buy = new VBox(10);
		buy.setAlignment(Pos.CENTER);
		Image buyImage = new Image("IconMedia/book.png");
		ImageView buyView = new ImageView(buyImage);
		buyView.setFitHeight(iconDim);
		buyView.setPreserveRatio(true);
		Button buyButton = new Button();
		buyButton.setPrefSize(iconDim, iconDim);
		buyButton.setGraphic(buyView);
		buyButton.setStyle("-fx-background-color: none; -fx-border-color: none;");
		buyButton.setOnAction(event -> {
			ViewController.goShopping();
		});
		Label buyLabel = new Label("Buy Books");
		buy.getChildren().add(buyButton);
		buy.getChildren().add(buyLabel);
		content.add(buy, 0, 0);
		
		//Assemble Sell Icon Button
		VBox sell = new VBox(10);
		sell.setAlignment(Pos.CENTER);
		Image sellImage = new Image("IconMedia/coins.png");
		ImageView sellView = new ImageView(sellImage);
		sellView.setFitHeight(iconDim);
		sellView.setPreserveRatio(true);
		Button sellButton = new Button();
		sellButton.setPrefSize(iconDim, iconDim);
		sellButton.setGraphic(sellView);
		sellButton.setStyle("-fx-background-color: none; -fx-border-color: none;");
		sellButton.setOnAction(event -> {
			ViewController.goSelling();
		});
		Label sellLabel = new Label("Sell Books");
		sell.getChildren().add(sellButton);
		sell.getChildren().add(sellLabel);
		content.add(sell, 1, 0);
		
		//Assemble Profile Icon Button
		VBox profile = new VBox(10);
		profile.setAlignment(Pos.CENTER);
		Image profileImage = new Image("IconMedia/user.png");
		ImageView profileView = new ImageView(profileImage);
		profileView.setFitHeight(iconDim);
		profileView.setPreserveRatio(true);
		Button profileButton = new Button();
		profileButton.setPrefSize(iconDim, iconDim);
		profileButton.setGraphic(buyView);
		profileButton.setStyle("-fx-background-color: none; -fx-border-color: none;");
		buyButton.setOnAction(event -> {
			ViewController.goShopping();
		});
		Label buyLabel = new Label("Buy Books");
		buy.getChildren().add(buyButton);
		buy.getChildren().add(buyLabel);
		content.add(buy, 0, 0);
		*/
		VBox buy = createIconButton("Buy Books", "book", "Buy");
		VBox sell = createIconButton("Sell Books", "coins", "Sell");
		VBox profile = createIconButton("View Profile", "user", "Profile");
		
		content.add(buy, 0, 0);
		content.add(sell, 1, 0);
		content.add(profile, 2, 0);
		
		content.setHgap(80);
		
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
