package application;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.util.converter.NumberStringConverter;

import java.util.*;
import java.util.regex.Pattern;


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
	protected Button createReturnHomeButton() {
		System.out.println("Creating Return Home Button");
		
		Button homeBut = new Button("Return Home");
		homeBut.setOnAction(event -> {
			ViewController.goHome();
		});
	
        return homeBut;
	}
	
	//returns button that will return any user to login page, signed out
	protected Button createSignOutButton() {
		System.out.println("Creating Sign Out Button");
		
		Button signoutBut = new Button("Sign Out");
		signoutBut.setOnAction(event -> {
			ViewController.signOut();
		});
		
		return signoutBut;
	}
	
	/*
	//returns button that will take regular user from buy screen to cart
	protected Button createViewCartButton() {
		System.out.println("Creating View Cart Button");
		
		Button viewCartBut = new Button("View Cart");
		viewCartBut.setOnAction(event -> {
			System.out.println("called scene change");
			//ViewController.goCart();
		});
		
		
		return viewCartBut;
	}*/
	
	/*
	//returns button that will return regular user from cart to buy screen
		protected Button createReturnShoppingButton() {
			System.out.println("Creating Return Shopping Button");
			
			Button shopBut = new Button("Return to Shopping");
			shopBut.setOnAction(event -> {
				ViewController.goShopping();
			});
				
			
			
			return shopBut;
		}
	*/
		
	protected void goToCart(Map<Integer, HBox> cartList) {
		ViewController.goCart(cartList);
	}
	
	protected void returnShopping(Map<Integer, HBox> cartList) {
		ViewController.goShopping(cartList);
	}
	
	//takes in label name, file name, event name and will return vbox containing icon button and label
	protected VBox createIconButton(String label, String fileName, String eventType) {
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
					ViewController.goShopping(null);
					break;
				case "Sell":
					ViewController.goSelling();
					break;
				case "Profile":
					ViewController.goProfile();
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
	protected ListView<HBox> createBookColumn(ArrayList<Object> booksIn, String colTitle, Map<Integer, HBox> cartMap) {
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
			System.out.println("op 1");
			System.out.println(booksIn.get(i));
			System.out.println("op 1");
			System.out.println(booksIn.get(i+1));

			Listing listing = (Listing) booksIn.get(i);
			Book book = (Book) booksIn.get(i + 1);
			int thisQuantity = listing.getQuantity();
			totalEntries += thisQuantity;
			HBox rowEntry = new HBox();
			rowEntry.getChildren().add(new Label(book.getTitle()));
			rowEntry.getChildren().add(createSpacer());
			rowEntry.getChildren().add(new Label("" + thisQuantity));
			
			Label hiddenListingID = new Label("" + listing.getListingID());
			hiddenListingID.setManaged(false);
			rowEntry.getChildren().add(hiddenListingID);
			
			Label hiddenCondition = new Label(book.getCondition());
			hiddenCondition.setManaged(false);
			rowEntry.getChildren().add(hiddenCondition);
			
			Label hiddenValue = new Label("" + book.getValue());
			hiddenValue.setManaged(false);
			rowEntry.getChildren().add(hiddenValue);
			
			//optional handling of cart status
			if (cartMap != null) {
				ArrayList<Integer> stillPresent = new ArrayList<>();
				if (cartMap.containsKey(listing.getListingID())) {
					rowEntry.getStyleClass().add("selectedListing");
					//stillPresent.add(listing.getListingID());
				}
				/*
				 //first attempt at handling concurrency issues with reloading page and missing cart products
				cartMap.forEach((key, value) -> {
					if (!stillPresent.contains(key) ) {
						System.out.println(key + "no longer present");
						Platform.runLater(() -> cartMap.remove(key) );
					}
				});*/
			}
			
			
			Tooltip tip = new Tooltip();
			
			//currently trying to hide listingID and Condition in hbox to be queried for cart selection
			tip.setAutoHide(false);
			tip.setShowDuration(new Duration(30000));
			
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
			
			resultItems.add(rowEntry);
		}
		
		Label entryLabel = new Label("" + totalEntries);
		entryLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
		header.getChildren().add(entryLabel);
		
		/*result.getSelectionModel().selectedItemProperty().addListener(
	        new ChangeListener<HBox>() {
	        public void changed(ObservableValue<? extends HBox> ov, HBox old_val, HBox new_val) {
	        	System.out.println("selection made");
	        	//ensure header cannot be clicked
	        	if (result.getSelectionModel().getSelectedIndex() == 0) {
	            	result.getSelectionModel().clearSelection();
	            }
	        }
	    });*/
		return result;
	}
	
	// Create cart ListView column
	protected ListView<GridPane> createCartColumn(Map<Integer, HBox> cartMap) {
		ObservableList<GridPane> resultItems = FXCollections.observableArrayList();
		ListView<GridPane> result = new ListView<GridPane>();
		result.setItems(resultItems);
		result.setPrefWidth(800);
				
		GridPane header = new GridPane();
		//header.setGridLinesVisible(true);
		ColumnConstraints col1 = new ColumnConstraints();
		col1.setPercentWidth(20);
		ColumnConstraints col2 = new ColumnConstraints();
		col2.setPercentWidth(20);
		ColumnConstraints col3 = new ColumnConstraints();
		col3.setPercentWidth(20);
		ColumnConstraints col4 = new ColumnConstraints();
		col4.setPercentWidth(20);
		ColumnConstraints col5 = new ColumnConstraints();
		col5.setPercentWidth(20);
		header.getColumnConstraints().addAll(col1, col2, col3, col4, col5);
		
		Label titleCol = new Label("Title");
		header.add(titleCol, 0, 0, 2, 1);
		header.add(createSpacer(), 1, 0);
		
		HBox ppuBox = new HBox(createSpacer());
		Label ppuCol = new Label("Price per Unit");
		ppuCol.setTextAlignment(TextAlignment.CENTER);
		ppuBox.getChildren().add(ppuCol);
		ppuBox.getChildren().add(createSpacer());
		header.add(ppuBox, 2, 0);
		
		HBox quantityBox = new HBox(createSpacer());
		Label quantityCol = new Label("Quantity");
		quantityCol.setTextAlignment(TextAlignment.CENTER);
		quantityBox.getChildren().add(quantityCol);
		quantityBox.getChildren().add(createSpacer());
		header.add(quantityBox, 3, 0);
		
		HBox priceBox = new HBox(createSpacer());
		Label priceCol = new Label("Price");
		priceCol.setTextAlignment(TextAlignment.CENTER);
		priceBox.getChildren().add(priceCol);
		priceBox.getChildren().add(createSpacer());
		header.add(priceBox, 4, 0);
		resultItems.add(header);
		
		for (Map.Entry<Integer, HBox> entry : cartMap.entrySet()) {
			//Book book = (Book) cartMap.;
			int listingID = entry.getKey();
			HBox listing = entry.getValue();
			
			GridPane bookRow = new GridPane();
			//header.setGridLinesVisible(true);
			ColumnConstraints column1 = new ColumnConstraints();
			column1.setPercentWidth(20);
			ColumnConstraints column2 = new ColumnConstraints();
			column2.setPercentWidth(20);
			ColumnConstraints column3 = new ColumnConstraints();
			column3.setPercentWidth(20);
			ColumnConstraints column4 = new ColumnConstraints();
			column4.setPercentWidth(20);
			ColumnConstraints column5 = new ColumnConstraints();
			column5.setPercentWidth(20);
			bookRow.getColumnConstraints().addAll(column1, column2, column3, column4, column5);
			
			int quantity = Integer.parseInt(((Label) listing.getChildren().get(2)).getText());
			
			Label bookTitle = new Label(((Label) listing.getChildren().get(0)).getText() + " (" + quantity + " available)");
			bookRow.add(bookTitle, 0, 0, 2, 1);
			
			Label hiddenID = new Label("" + listingID);
			hiddenID.setVisible(false);
			bookRow.add(hiddenID, 1, 0);
			
			HBox bookPPUBox = new HBox(createSpacer());
			int price = Integer.parseInt(((Label) listing.getChildren().get(5)).getText());
			double ppu = (double) price;
			ppu = ppu / 100.0;
			Label bookPPUCol = new Label(String.format("$%.2f", ppu));
			bookPPUCol.setTextAlignment(TextAlignment.CENTER);
			bookPPUBox.getChildren().add(bookPPUCol);
			bookPPUBox.getChildren().add(createSpacer());
			bookRow.add(bookPPUBox, 2, 0);
			/*
			HBox bookQuantityBox = new HBox(createSpacer());
			int quantity = Integer.parseInt(((Label) listing.getChildren().get(2)).getText());
			Label bookQuantityCol = new Label("1");
			bookQuantityCol.setTextAlignment(TextAlignment.CENTER);
			bookQuantityBox.getChildren().add(bookQuantityCol);
			bookQuantityBox.getChildren().add(createSpacer());
			bookRow.add(bookQuantityBox, 3, 0);
			*/
			
			HBox bookQuantityBox = new HBox(createSpacer());
			//Label bookQuantityCol = new Label("1");
			//bookQuantityCol.setTextAlignment(TextAlignment.CENTER);
			TextField bookQuantityCol = new TextField();
			bookQuantityCol.setText("1");			
			//modified from https://stackoverflow.com/questions/56446127/javafx-textfield-with-regex-for-zipcode
			String quantityRegEx = "^$|[1-" + quantity + "]";
			final Pattern pattern = Pattern.compile(quantityRegEx);
			TextFormatter<?> formatter = new TextFormatter<>(change -> {
			    if (pattern.matcher(change.getControlNewText()).matches()) {
			        return change; // allow this change to happen
			    } else {
			        return null; // prevent change
			    }
			});
			bookQuantityCol.focusedProperty().addListener((ov, oldV, newV) -> {
		       if (!newV) {
		          if (bookQuantityCol.getText().isEmpty()) {
		        	  bookQuantityCol.setText("1");
		          }
		          Label listingTotal = ((Label) ((HBox) bookRow.getChildren().get(4)).getChildren().get(1));
		          double quantitySelected = Double.parseDouble(((TextField) ((HBox) bookRow.getChildren().get(3)).getChildren().get(1)).getText());
		          double unitPrice = Double.parseDouble(((Label) ((HBox) bookRow.getChildren().get(2)).getChildren().get(1)).getText().substring(1)); //substring chops off $ sign
		          listingTotal.setText(String.format("$%.2f", (quantitySelected * unitPrice)));
		          //total list price                                  //quantity selected                              //unit price                                                                                         //cents to dollars
		          //((Label) bookRow.getChildren().get(4)).setText("" + (Double.parseDouble(bookQuantityCol.getText()) * Double.parseDouble(((Label) ((HBox) bookRow.getChildren().get(2)).getChildren().get(1)).getText()) / 100.0));
		       }
		    });
			bookQuantityCol.setTextFormatter(formatter);
			
			bookQuantityBox.getChildren().add(bookQuantityCol);
			bookQuantityBox.getChildren().add(createSpacer());
			bookRow.add(bookQuantityBox, 3, 0);
			
			HBox bookPriceBox = new HBox(createSpacer());
			//double doublePrice = ppu * Integer.parseInt(bookQuantityCol.getText());
			Label bookPriceCol = new Label(String.format("$%.2f", ppu));
			bookPriceCol.setTextAlignment(TextAlignment.CENTER);
			bookPriceBox.getChildren().add(bookPriceCol);
			bookPriceBox.getChildren().add(createSpacer());
			bookRow.add(bookPriceBox, 4, 0);
			
			Label maxQuantity = new Label("" + quantity);
			maxQuantity.setManaged(false);
			bookRow.add(maxQuantity, 5, 0);
			resultItems.add(bookRow);
		}
		
		
		return result;
		
	}
	
	
	protected abstract void assembleHeader();
	
	protected abstract void assembleContent();
	
	protected abstract void assembleFooter();
}
