package application;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets; 
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene; 
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*; 
import javafx.scene.paint.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*; 
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.*;

public abstract class Screen {
	protected final Color gold = Color.web("0xFFC627", 1.0);
	protected final Color maroon = Color.web("0x8C1D40", 1.0);
	protected final Color bgGray = Color.web("0xE0E0E0", 1.0);
	protected final double width = 900;
	protected final double height = 600;
	protected final double vertMargin = 72;
	protected final Font regFont = Font.font("Verdana", FontWeight.NORMAL, 16);
	Scene screen;
	GridPane root = new GridPane();
	Screen() {
		//root.setAlignment(Pos.TOP_CENTER);
		root.setMinSize(width, height);
		//root.setPadding(new Insets(30, 30, 30, 30));
		//root.setVgap(30);
		//root.setHgap(30);		
		//RowConstraints row1 = new RowConstraints(vertMargin);
		//RowConstraints row2 = new RowConstraints();
		//RowConstraints row3 = new RowConstraints(vertMargin);

		root.setGridLinesVisible(true); //DELETE FOR PROD
	};
	
	//returns HBox object of gold header
	protected HBox createHeader() {
		System.out.println("Creating Header");
		
		HBox header = new HBox();
		BackgroundFill bg_fill = new BackgroundFill(gold, CornerRadii.EMPTY, Insets.EMPTY);
		Background bg = new Background(bg_fill);
		header.setBackground(bg);
		header.setAlignment(Pos.CENTER);
		header.setPrefWidth(width);
		header.setPrefHeight(vertMargin);
		header.setMaxHeight(vertMargin);
		header.setPadding(new Insets(0, 30, 0, 30));
		
		return header;
	}
	
	//returns formatted header title label from input string
	protected Label createTitle(String title) {
		System.out.println("Creating Title");
		
		Label headerTitle = new Label(title);
		headerTitle.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
		GridPane.setHalignment(headerTitle, HPos.CENTER);
		
		return headerTitle;
	}
	
	//returns empty spacer region
	protected Region createSpacer() {
		System.out.println("Creating Spacer");
		
		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		
		return spacer;
	}
	
	//returns footer HBox
	protected HBox createFooter() {
		System.out.println("Creating Footer");
		
		HBox footer = new HBox();
		BackgroundFill bg_fill = new BackgroundFill(gold, CornerRadii.EMPTY, Insets.EMPTY);
		Background bg = new Background(bg_fill);
		footer.setBackground(bg);
		footer.setAlignment(Pos.CENTER);
		footer.setPrefWidth(width);
		footer.setPrefHeight(vertMargin);
		
		return footer;
	}
	
	//returns body vbox
	protected GridPane createContentWindow() {
		System.out.println("Creating Content Window");

		GridPane content = new GridPane();
		BackgroundFill bg_fill = new BackgroundFill(bgGray, CornerRadii.EMPTY, Insets.EMPTY);
		Background bg = new Background(bg_fill);
		content.setBackground(bg);
		content.setPrefWidth(width - 50);
		content.setPrefHeight(height - 2 * vertMargin - 50);
		content.setAlignment(Pos.CENTER);
		GridPane.setMargin(content, new Insets(25, 25, 25, 25));
		content.setPadding(new Insets(20));
		//content.setPadding(new Insets(30, 30, 30, 30));
		
		return content;
	}
	
	//returns button that will return regular user to home screen
	protected Button createReturnHomeButton(String caller) {
		System.out.println("Creating Return Home Button");
		
		Button homeBut = new Button("Return Home");
		homeBut.setOnAction(event -> {
			ViewController.goHome(caller);
		});
	
        return homeBut;
	}
	
	//returns button that will return regular user from cart to buy screen
	protected Button createReturnShoppingButton(String caller) {
		System.out.println("Creating Return Shopping Button");
		
		Button shopBut = new Button("Return to Shopping");
		shopBut.setOnAction(event -> {
			ViewController.goShopping(caller);
		});
			
		
		
		return shopBut;
	}
	
	//returns button that will return any user to login page, signed out
	protected Button createSignOutButton(String caller) {
		System.out.println("Creating Sign Out Button");
		
		Button signoutBut = new Button("Sign Out");
		signoutBut.setOnAction(event -> {
			ViewController.signOut(caller);
		});
		
		
		return signoutBut;
	}
	
	//returns button that will take regular user from buy screen to cart
	protected Button createViewCartButton(String caller) {
		System.out.println("Creating View Cart Button");
		
		Button signoutBut = new Button("View Cart");
		signoutBut.setOnAction(event -> {
			ViewController.goCart(caller);
		});
		
		
		return signoutBut;
	}
	
	//takes in label name, file name, event name and will return vbox containing icon button and label
	protected VBox createIconButton(String label, String fileName, String eventType, String caller) {
		int iconDim = 120;
		VBox box = new VBox(10);
		box.setAlignment(Pos.CENTER);
		Image boxImage = new Image("IconMedia/" + fileName + ".png");
		ImageView boxView = new ImageView(boxImage);
		boxView.setFitHeight(iconDim);
		boxView.setPreserveRatio(true);
		Button boxButton = new Button();
		boxButton.setPrefSize(iconDim, iconDim);
		boxButton.setGraphic(boxView);
		boxButton.setStyle("-fx-background-color: none; -fx-border-color: none;");
		boxButton.setOnAction(event -> {
			switch (eventType) {
				case "Buy":
					ViewController.goShopping(caller);
					break;
				case "Sell":
					ViewController.goSelling(caller);
					break;
				case "Profile":
					ViewController.goProfile(caller);
					break;
				default:
					System.out.println("Not a recognized event type");
			}
		});
		Label boxLabel = new Label(label);
		box.getChildren().add(boxButton);
		box.getChildren().add(boxLabel);
		
		return box;
	}
	
	//converts arraylist of listings into browsable book column
	protected ListView<HBox> createBookColumn(ArrayList<Object> booksIn, String colTitle) {
		ObservableList<HBox> resultItems = FXCollections.observableArrayList();
		ListView<HBox> result = new ListView<HBox>();
		result.setItems(resultItems);
		
		HBox header = new HBox();
		Label title = new Label(colTitle);
		title.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
		header.getChildren().add(title);
		resultItems.add(header);
		
		int totalEntries = 0;
		for (int i = 0; i < booksIn.size(); i += 2) {
			Listing listing = (Listing) booksIn.get(i);
			Book book = (Book) booksIn.get(i + 1);
			int thisQuantity = listing.getQuantity();
			totalEntries += thisQuantity;
			HBox rowEntry = new HBox();
			rowEntry.getChildren().add(new Label(book.getTitle()));
			rowEntry.getChildren().add(createSpacer());
			rowEntry.getChildren().add(new Label("" + thisQuantity));
			Tooltip tip = new Tooltip();
			tip.setAutoHide(false);
			tip.setShowDuration(new Duration(10000));
			
			//create tooltip view
			GridPane tipPane = new GridPane();
			tipPane.setHgap(8);
			tipPane.add(new Label("Title:"), 0, 0);
			tipPane.add(new Label(book.getTitle()), 1, 0);
			tipPane.add(new Label("Author:"), 0, 1);
			tipPane.add(new Label(book.getAuthor()), 1, 1);
			tipPane.add(new Label("Condition:"), 0, 2);
			tipPane.add(new Label(book.getCondition()), 1, 2);
			
			tipPane.add(new Label("Category:"), 2, 0);
			tipPane.add(new Label(book.getCategory()), 3, 0);
			tipPane.add(new Label("Pub. Year:"), 2, 1);
			tipPane.add(new Label("" + book.getYear()), 3, 1);
			tipPane.add(new Label("Quantity:"), 2, 2);
			tipPane.add(new Label("" + listing.getQuantity()), 3, 2);
			double cents = (double) book.getValue();
			cents /= 100.0;
			tipPane.add(new Label("Price:"), 0, 3);
			tipPane.add(new Label(String.format("$%.2f", cents)), 1, 3);
			
			tip.setGraphic(tipPane);
			
			Tooltip.install(rowEntry, tip);
			
			if ((i / 2) % 2 == 0) {
				rowEntry.setStyle("--fx-background-color: #FF0000");
			} else {
				rowEntry.setStyle("--fx-background-color: #0000FF");
			}
			
			resultItems.add(rowEntry);
		}
		
		Label entryLabel = new Label("" + totalEntries);
		entryLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
		header.getChildren().add(entryLabel);
		return result;
		//return null;
	}
	
	protected abstract void assembleHeader();
	
	protected abstract void assembleContent();
	
	protected abstract void assembleFooter();
}
