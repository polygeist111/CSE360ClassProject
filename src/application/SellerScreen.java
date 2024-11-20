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
import javafx.stage.Popup;
import javafx.util.converter.NumberStringConverter;

import java.util.*;
import java.time.*;
import java.text.DecimalFormat;

public class SellerScreen extends Screen {
	private static final NumberStringConverter priceConverter = new NumberStringConverter(new DecimalFormat("#0.00"));
	private static final NumberStringConverter intConverter = new NumberStringConverter("#");
	private int priceInt = 0;
	
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
		categoryLabel.setFont(regFont);
		ComboBox<String> categoryBox = new ComboBox<>();
		categoryBox.getItems().addAll(
				"Computer",
				"English Language",
				"Math",
				"Natural Science",
				"Other"
				);
		sellerGrid.add(categoryLabel,  0, 4);
		sellerGrid.add(categoryBox,  0, 5);
		
		
		//create "book condition" dropdown menu
		Label conditionLabel = new Label("Select Condition");
		conditionLabel.setFont(regFont);
		ComboBox<String> conditionBox = new ComboBox<>();
		conditionBox.getItems().addAll(
				"Used Like New",
				"Moderately Used",
				"Heavily Used"
				);
		sellerGrid.add(conditionLabel,  1, 4);
		sellerGrid.add(conditionBox,  1, 5);
		
		
		//create "original price" entry
		Label originalLabel = new Label("Original Price (/Unit)");
		originalLabel.setFont(regFont);
		TextField originalField = new TextField();
		TextFormatter<Number> priceFormatter = new TextFormatter<>(priceConverter);
		originalField.setTextFormatter(priceFormatter);
		sellerGrid.add(originalLabel, 0, 2);
		sellerGrid.add(originalField, 0, 3);
		
		//create published year label and entry
		Label quantityLabel = new Label("Quantity");		
		quantityLabel.setFont(regFont);
		TextField quantityField = new TextField();
		TextFormatter<Number> quantityFormatter = new TextFormatter<>(intConverter);
		quantityField.setTextFormatter(quantityFormatter);
		sellerGrid.add(quantityLabel, 1, 2);
		sellerGrid.add(quantityField, 1, 3);
		
		//create book title label and entry
		Label titleLabel = new Label("Title of Book");
		titleLabel.setFont(regFont);
		TextField titleField = new TextField();
		sellerGrid.add(titleLabel, 0, 0);
		sellerGrid.add(titleField, 0, 1);
		
		
		//create book author label and entry
		Label authorLabel = new Label("Book Author");		
		authorLabel.setFont(regFont);
		TextField authorField = new TextField();
		sellerGrid.add(authorLabel, 1, 0);
		sellerGrid.add(authorField, 1, 1);
		
		
		//create published year label and entry
		Label yearLabel = new Label("Published Year");		
		yearLabel.setFont(regFont);
		TextField yearField = new TextField();
		yearField.setMaxWidth(165);
		TextFormatter<Number> yearFormatter = new TextFormatter<>(intConverter);
		yearField.setTextFormatter(yearFormatter);
		sellerGrid.add(yearLabel, 2, 0);
		sellerGrid.add(yearField, 2, 1);
		
		
		//create calculated price label
		Label priceLabel = new Label("Enter book details to see listing price");
		priceLabel.setFont(Font.font("Verdana", FontWeight.NORMAL, 12));
		sellerGrid.add(priceLabel, 2, 2, 1, 4);
		sellerGrid.setHalignment(priceLabel, HPos.CENTER);
		sellerGrid.setValignment(priceLabel, VPos.CENTER);

		
		HBox listingButtons = new HBox();
		//create calculate price button
		Button calculatePriceButton = new Button("Calculate Price");
		listingButtons.getChildren().add(createSpacer());
		listingButtons.getChildren().add(calculatePriceButton);
	
		
		//create "list my book" button
		Button listBookButton = new Button("List My Book");
		listBookButton.setDisable(true);
		listingButtons.getChildren().add(createSpacer());
		listingButtons.getChildren().add(listBookButton);
		listingButtons.getChildren().add(createSpacer());
		sellerGrid.add(listingButtons, 0, 6, 3, 1);
		
		
		//define calculate price behavior
		calculatePriceButton.setOnAction(event -> {
			
			String feedback = "";
			
			//title
			if (titleField.getText().isEmpty()) {
				if (!feedback.isBlank()) {
					feedback += "\n";
				}
				feedback += "Enter the title";
				titleField.setStyle("-fx-border-color: red");
			} else {
				titleField.setStyle("-fx-border-color: none");
			}
			
			//author
			if (authorField.getText().isEmpty()) {
				if (!feedback.isBlank()) {
					feedback += "\n";
				}
				feedback += "Enter the author's name";
				authorField.setStyle("-fx-border-color: red");
			} else {
				authorField.setStyle("-fx-border-color: none");
			}
			
			//year
			if (yearField.getText().isEmpty()) {
				if (!feedback.isBlank()) {
					feedback += "\n";
				}
				feedback += "Enter the year the book was published";
				yearField.setStyle("-fx-border-color: red");
			} else {
				yearField.setStyle("-fx-border-color: none");
			}
			
			//original price
			if (originalField.getText().isEmpty()) {
				if (!feedback.isBlank()) {
					feedback += "\n";
				}
				feedback += "Enter the original price";
				originalField.setStyle("-fx-border-color: red");
			} else {
				originalField.setStyle("-fx-border-color: none");
			}
			
			//quantity
			if (quantityField.getText().isEmpty()) {
				if (!feedback.isBlank()) {
					feedback += "\n";
				}
				feedback += "Enter the number of copies being listed";
				quantityField.setStyle("-fx-border-color: red");
			} else {
				quantityField.setStyle("-fx-border-color: none");
			}
			
			//category
			if (categoryBox.getValue() == null) {
				if (!feedback.isBlank()) {
					feedback += "\n";
				}
				feedback += "Select a category";
				categoryBox.setStyle("-fx-border-color: red");
			} else {
				categoryBox.setStyle("-fx-border-color: none");
			}
			
			//condition
			if (conditionBox.getValue() == null) {
				if (!feedback.isBlank()) {
					feedback += "\n";
				}
				feedback += "Select a condition";
				conditionBox.setStyle("-fx-border-color: red");
			} else {
				conditionBox.setStyle("-fx-border-color: none");
			}
			
			
			
			//valid inputs
			if (feedback.isEmpty()) {
				listBookButton.setDisable(false);
				priceLabel.setFont(regFont);
				double price = priceConverter.fromString(originalField.getText()).doubleValue();
				//adjust price double by condition
				switch (conditionBox.getValue()) {
					case "Used Like New":
						price *= .7;
						break;
					case "Moderately Used":
						price *= .5;
						break;
					case "Heavily Used":
						price *= .3;
						break;
					default: 
						System.out.println("ERROR: failed to select condition");
						price = 0.0;
				}
				//adjust price double by age
				int yearsSince = YearMonth.now().getYear() - Integer.parseInt(yearField.getText());
				for (int i = 0; i < yearsSince; i++) {
					price *= .98;
				}
				feedback += "Your per-unit price: $%.2f";
				feedback = String.format(feedback, price);
				//convert to cents
				price *= 100;
				priceInt = ((Double) price).intValue();
				
			}
			priceLabel.setText(feedback);
		});
		
		//not visible until book is listed
		Button listAnother = new Button("List Another Book");
		listAnother.setOnAction(event -> {
			ViewController.goSelling("Sell");
		});
		
		//define list book behavior
		listBookButton.setOnAction(event -> {
			DBMediator.createListing(ViewController.currentUser.getUserID(), Integer.parseInt(quantityField.getText()), titleField.getText(), authorField.getText(), Integer.parseInt(yearField.getText()), categoryBox.getValue(), conditionBox.getValue(), priceInt);
			/*Popup completionAlert = new Popup();
			Label completionLabel = new Label(Integer.parseInt(quantityField.getText()) + " copies of your book " + titleField.getText() + " have been listed");
			completionAlert.getContent().add(completionLabel);*/
			sellerGrid.setVisible(false);
			sellerGrid.setManaged(false);
			content.setVgap(20);
			content.setAlignment(Pos.CENTER);
			Label completionLabel = new Label(Integer.parseInt(quantityField.getText()) + " copies of your book " + titleField.getText() + " have been listed\n\nYou can list another book, or sign out or return to the homescreen\nwith the buttons in the top toolbar");
			completionLabel.setTextAlignment(TextAlignment.CENTER);
			HBox completionLabelBox = new HBox(createSpacer());
			completionLabelBox.getChildren().add(completionLabel);
			completionLabelBox.getChildren().add(createSpacer());
			HBox completionButtonBox = new HBox(createSpacer());
			completionButtonBox.getChildren().add(listAnother);
			completionButtonBox.getChildren().add(createSpacer());
			content.add(completionLabelBox, 0, 0);
			content.add(completionButtonBox, 0, 1);
			
			/*
			 HBox completionButtonBox = new HBox(createSpacer());
			completionButtonBox.getChildren().add(listAnother);
			completionButtonBox.getChildren().add(createSpacer());
			VBox completionBox = new VBox();
			completionBox.getChildren().add(completionLabelBox);
			completionBox.getChildren().add(completionButtonBox);
			content.add(completionBox, 0, 0);
			 */
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
