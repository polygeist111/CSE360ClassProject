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
import javafx.util.converter.NumberStringConverter;

import java.util.*;
import java.text.DecimalFormat;

public class BuyerScreen extends Screen {
	//REMOVE both of these in final implementation
	
	BuyerScreen () {
		assembleHeader();
		assembleContent();
		assembleFooter();
		screen = new Scene(root);
	};

	protected void assembleHeader() {
		HBox header = createHeader();
		header.getChildren().add(createReturnHomeButton("Buy"));
		header.getChildren().add(createSpacer());
		header.getChildren().add(createSignOutButton("Buy"));
		root.add(header, 0, 0);
		root.add(createTitle("Selling"), 0, 0);
	}
	
	protected void assembleContent() {
		GridPane content = createContentWindow();
		
		
		//create grid holding seller objects
		GridPane buyerGrid = new GridPane();
		buyerGrid.setVgap(15);
		buyerGrid.setHgap(15);
		
		HBox categorySelect = new HBox();
		
		//create computer category button
		categorySelect.getChildren().add(createSpacer());
		Button computerCat = new Button("Computer");
		computerCat.setOnAction(event -> {
			System.out.println("Computer category selected");
		});
		categorySelect.getChildren().add(computerCat);
		
		//create english language category button
		categorySelect.getChildren().add(createSpacer());
		Button englishCat = new Button("English Language");
		englishCat.setOnAction(event -> {
			System.out.println("English Language category selected");
		});
		categorySelect.getChildren().add(englishCat);
		
		//create math category button
		categorySelect.getChildren().add(createSpacer());
		Button mathCat = new Button("Math");
		mathCat.setOnAction(event -> {
			System.out.println("Math category selected");
		});
		categorySelect.getChildren().add(mathCat);
		
		//create natural science category button
		categorySelect.getChildren().add(createSpacer());
		Button natsciCat = new Button("Natural Science");
		natsciCat.setOnAction(event -> {
			System.out.println("Natural Science category selected");
		});
		categorySelect.getChildren().add(natsciCat);
		
		//create other category button
		categorySelect.getChildren().add(createSpacer());
		Button otherCat = new Button("Other");
		otherCat.setOnAction(event -> {
			System.out.println("Other category selected");
		});
		categorySelect.getChildren().add(otherCat);
		
		categorySelect.getChildren().add(createSpacer());
		buyerGrid.add(categorySelect, 0, 0);
		
		
		GridPane browseColumns = new GridPane();
		browseColumns.setHgap(30);
		browseColumns.getChildren().add(createBookColumn(DBMediator.queryListings("currentlistings", "condition", "Used Like New"), "Used Like New: "));
		
		buyerGrid.add(browseColumns, 0, 1);
		content.add(buyerGrid, 0, 0);
		
		root.add(content, 0, 1);

	}
	
	protected void assembleFooter() {
		HBox footer = createFooter();
		
		footer.setPadding(new Insets(0, 0, 0, 15));
		footer.setSpacing(15);
		footer.setAlignment(Pos.CENTER_LEFT);
		
		Button addToCart = new Button("Add to Cart");
		addToCart.setOnAction(event -> {
			System.out.println("Adding selection to cart");
		});
		footer.getChildren().add(addToCart);
		
		footer.getChildren().add(createViewCartButton("Buy"));
		
		root.add(footer, 0, 2);
		
		Text credit = new Text("Made by CSE 360 Group 16");
		credit.setFont(Font.font("Verdana", FontWeight.NORMAL, 10));
		HBox creditFrame = new HBox(credit);
		creditFrame.setPadding(new Insets(5));
		creditFrame.setAlignment(Pos.BOTTOM_RIGHT);
		root.add(creditFrame, 0, 2);
	}
}
