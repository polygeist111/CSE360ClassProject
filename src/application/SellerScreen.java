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
		header.getChildren().add(createReturnHomeButton());
		header.getChildren().add(createSpacer());
		header.getChildren().add(createSignOutButton());
		root.add(header, 0, 0);
		root.add(createTitle("Selling"), 0, 0);
	}
	
	protected void assembleContent() {
		GridPane content = createContentWindow();
		
		
		//create grid holding seller objects
		GridPane sellerGrid = new GridPane();
		sellerGrid.setVgap(5);
		sellerGrid.setHgap(5);
		
		
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
		sellerGrid.add(categoryLabel,  0, 0);
		sellerGrid.add(categoryBox,  0, 1);
		
		
		//create "book condition" dropdown menu
		Label conditionLabel = new Label("Select Category");
		conditionLabel.setFont(Font.font("Verdana", FontWeight.NORMAL, 16));
		ComboBox<String> conditionBox = new ComboBox<>();
		conditionBox.getItems().addAll(
				"Heavily Used",
				"Moderately Used",
				"Used Like New"
				);
		sellerGrid.add(conditionLabel,  0, 2);
		sellerGrid.add(conditionBox,  0, 3);
		
		
		//create "original price" entry
		Label originalLabel = new Label("Original Price");
		originalLabel.setFont(Font.font("Verdana", FontWeight.NORMAL, 16));
		TextField originalField = new TextField();
		DecimalFormat format = new DecimalFormat("#0.00");
		TextFormatter<Number> textFormatter = new TextFormatter<>(
				new NumberStringConverter(format)
				);
		originalField.setTextFormatter(textFormatter);
		sellerGrid.add(originalLabel, 0, 4);
		sellerGrid.add(originalField, 0, 5);
		
		//create book title label and entry
		Label titleLabel = new Label("Title of Book");
		titleLabel.setFont(Font.font("Verdana", FontWeight.NORMAL, 16));
		TextField titleField = new TextField();
		sellerGrid.add(titleLabel, 0, 6);
		sellerGrid.add(titleField, 0, 7);
		
		
		//create book author label and entry
		Label authorLabel = new Label("Book Author");		
		authorLabel.setFont(Font.font("Verdana", FontWeight.NORMAL, 16));
		TextField authorField = new TextField();
		sellerGrid.add(authorLabel, 0, 8);
		sellerGrid.add(authorField, 0, 9);
		
		
		//create published year label and entry
		Label yearLabel = new Label("Published Year");		
		yearLabel.setFont(Font.font("Verdana", FontWeight.NORMAL, 16));
		TextField yearField = new TextField();
		sellerGrid.add(yearLabel, 0, 10);
		sellerGrid.add(yearField, 0, 11);
		
		
		//create calculate price label and button
		Button calculatePriceButton = new Button("Calculate Price");
		Label priceLabel = new Label("");
		sellerGrid.add(calculatePriceButton, 0, 12);
		sellerGrid.add(priceLabel, 1, 12);
		
		
		//create "list my book" label and button
		Button listBookButton = new Button("List My Book");
		sellerGrid.add(listBookButton, 2, 12);
		
		
		//define calculate price behavior
		calculatePriceButton.setOnAction(event -> {
			priceLabel.setText("Price calculated!");
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
