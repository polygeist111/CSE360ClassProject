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

public class SellerScreen extends Screen {
	//REMOVE both of these in final implementation
	
	SellerScreen () {
		assembleHeader();
		assembleContent();
		assembleFooter();
		screen = new Scene(root);
	};

	protected void assembleHeader() {
		HBox header = createHeader();
		header.getChildren().add(createReturnHomeButton("Sell"));
		header.getChildren().add(createSpacer());
		header.getChildren().add(createSignOutButton("Sell"));
		root.add(header, 0, 0);
		root.add(createTitle("Selling"), 0, 0);
	}
	
	protected void assembleContent() {
		GridPane content = createContentWindow();
		
		
		//create grid holding seller objects
		GridPane sellerGrid = new GridPane();
		sellerGrid.setVgap(15);
		sellerGrid.setHgap(15);
		
		
		//create "select category" dropdown menu
		Label categoryLabel = new Label("Select Category");
		categoryLabel.setFont(Font.font("Verdana", FontWeight.NORMAL, 16));
		ComboBox<String> categoryBox = new ComboBox<>();
		categoryBox.getItems().addAll(
				"Computer",
				"English",
				"Language",
				"Math",
				"Natural Science",
				"Other"
				);
		sellerGrid.add(categoryLabel,  0, 2);
		sellerGrid.add(categoryBox,  0, 3);
		
		
		//create "book condition" dropdown menu
		Label conditionLabel = new Label("Select Category");
		conditionLabel.setFont(Font.font("Verdana", FontWeight.NORMAL, 16));
		ComboBox<String> conditionBox = new ComboBox<>();
		conditionBox.getItems().addAll(
				"Heavily Used",
				"Moderately Used",
				"Used Like New"
				);
		sellerGrid.add(conditionLabel,  1, 2);
		sellerGrid.add(conditionBox,  1, 3);
		
		
		//create "original price" entry
		Label originalLabel = new Label("Original Price");
		originalLabel.setFont(Font.font("Verdana", FontWeight.NORMAL, 16));
		TextField originalField = new TextField();
		DecimalFormat format = new DecimalFormat("#0.00");
		TextFormatter<Number> textFormatter = new TextFormatter<>(
				new NumberStringConverter(format)
				);
		originalField.setTextFormatter(textFormatter);
		sellerGrid.add(originalLabel, 2, 0);
		sellerGrid.add(originalField, 2, 1);
		
		//create book title label and entry
		Label titleLabel = new Label("Title of Book");
		titleLabel.setFont(Font.font("Verdana", FontWeight.NORMAL, 16));
		TextField titleField = new TextField();
		sellerGrid.add(titleLabel, 0, 0);
		sellerGrid.add(titleField, 0, 1);
		
		
		//create book author label and entry
		Label authorLabel = new Label("Book Author");		
		authorLabel.setFont(Font.font("Verdana", FontWeight.NORMAL, 16));
		TextField authorField = new TextField();
		sellerGrid.add(authorLabel, 1, 0);
		sellerGrid.add(authorField, 1, 1);
		
		
		//create published year label and entry
		Label yearLabel = new Label("Published Year");		
		yearLabel.setFont(Font.font("Verdana", FontWeight.NORMAL, 16));
		TextField yearField = new TextField();
		sellerGrid.add(yearLabel, 3, 0);
		sellerGrid.add(yearField, 3, 1);
		
		
		//create calculated price label
		Label priceLabel = new Label("Enter book details to see listing price");
		sellerGrid.add(priceLabel, 2, 2, 2, 2);
		sellerGrid.setHalignment(priceLabel, HPos.CENTER);
		sellerGrid.setValignment(priceLabel, VPos.CENTER);

		
		HBox listingButtons = new HBox();
		//create calculate price button
		Button calculatePriceButton = new Button("Calculate Price");
		listingButtons.getChildren().add(createSpacer());
		listingButtons.getChildren().add(calculatePriceButton);
		//sellerGrid.add(calculatePriceButton, 0, 6, 2, 2);
	
		
		//create "list my book" button
		Button listBookButton = new Button("List My Book");
		listingButtons.getChildren().add(createSpacer());
		listingButtons.getChildren().add(listBookButton);
		//sellerGrid.add(listBookButton, 2, 6, 2, 2);
		listingButtons.getChildren().add(createSpacer());
		sellerGrid.add(listingButtons, 0, 6, 4, 1);
		
		
		//define calculate price behavior
		calculatePriceButton.setOnAction(event -> {
			priceLabel.setText("Your price: $xx.xx");
		});
		

		content.add(sellerGrid, 0, 0);
		
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
