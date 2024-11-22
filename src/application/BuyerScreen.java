package application;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
	private String category = "Other";
	private HBox categorySelect;
	private GridPane browseColumns;
	private Label currentSelection;
	
	//holds listingID of current selection for cart
	private int selectedListing = -1;
	
	BuyerScreen () {
		assembleHeader();
		assembleContent();
		toggleSelection();
		assembleFooter();
		screen = new Scene(root);
	};

	protected void assembleHeader() {
		HBox header = createHeader();
		header.getChildren().add(createReturnHomeButton("Buy"));
		header.getChildren().add(createSpacer());
		header.getChildren().add(createSignOutButton("Buy"));
		root.add(header, 0, 0);
		root.add(createTitle("Buying"), 0, 0);
	}
	
	protected void assembleContent() {
		GridPane content = createContentWindow();		
		
		//create grid holding seller objects
		GridPane buyerGrid = new GridPane();
		buyerGrid.setVgap(15);
		buyerGrid.setHgap(15);
		
		categorySelect = new HBox();
		
		//create computer category button
		categorySelect.getChildren().add(createSpacer());
		Button computerCat = new Button("Computer");
		computerCat.setOnAction(event -> {
			System.out.println("Computer category selected");
			category = "Computer";
			toggleSelection();
		});
		categorySelect.getChildren().add(computerCat);
		
		//create english language category button
		categorySelect.getChildren().add(createSpacer());
		Button englishCat = new Button("English Language");
		englishCat.setOnAction(event -> {
			System.out.println("English Language category selected");
			category = "English Language";
			toggleSelection();
		});
		categorySelect.getChildren().add(englishCat);
		
		//create math category button
		categorySelect.getChildren().add(createSpacer());
		Button mathCat = new Button("Math");
		mathCat.setOnAction(event -> {
			System.out.println("Math category selected");
			category = "Math";
			toggleSelection();
		});
		categorySelect.getChildren().add(mathCat);
		
		//create natural science category button
		categorySelect.getChildren().add(createSpacer());
		Button natsciCat = new Button("Natural Science");
		natsciCat.setOnAction(event -> {
			System.out.println("Natural Science category selected");
			category = "Natural Science";
			toggleSelection();
		});
		categorySelect.getChildren().add(natsciCat);
		
		//create other category button
		categorySelect.getChildren().add(createSpacer());
		Button otherCat = new Button("Other");
		otherCat.setOnAction(event -> {
			System.out.println("Other category selected");
			category = "Other";
			toggleSelection();
		});
		categorySelect.getChildren().add(otherCat);
		
		categorySelect.getChildren().add(createSpacer());
		buyerGrid.add(categorySelect, 0, 0);
		
		
		browseColumns = new GridPane();
		browseColumns.setHgap(30);
		browseCategory();
		
		buyerGrid.add(browseColumns, 0, 1);
		
		HBox deselBox = new HBox(createSpacer());
		Label deselectTip = new Label("Ctrl+Click to Deselect");
		deselectTip.setTextAlignment(TextAlignment.CENTER);
		deselBox.getChildren().add(deselectTip);
		deselBox.getChildren().add(createSpacer());
		currentSelection = new Label("");
		currentSelection.setTextAlignment(TextAlignment.CENTER);
		deselBox.getChildren().add(currentSelection);
		deselBox.getChildren().add(createSpacer());
		buyerGrid.add(deselBox, 0, 2);
		
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
	
	//adjusts button selection and updates db output
	private void toggleSelection() {
		for (Node child : categorySelect.getChildren()) {
			if (child instanceof Button) {
				if (((Button) child).getText() == category) {
					child.setStyle("-fx-background-color: #FFC627; -fx-text-fill: #090909;");
				} else {
					child.setStyle("-fx-background-color: #8C1D40; -fx-text-fill: #F6F6F6;");
				}
			}
		}
		browseCategory();
	}
	
	//queries db for category results and updates screen
	private void browseCategory() {
		ArrayList<ListView<HBox>> buyColumns = new ArrayList<>();
		ListView<HBox> ulnColumn = createBookColumn(DBMediator.queryListings("currentlistings", "Used Like New", category), "Used Like New: ");
		buyColumns.add(ulnColumn);
		browseColumns.add(ulnColumn, 0, 0);
		ListView<HBox> muColumn = createBookColumn(DBMediator.queryListings("currentlistings", "Moderately Used", category), "Moderately Used: ");
		buyColumns.add(muColumn);
		browseColumns.add(muColumn, 1, 0);
		ListView<HBox> huColumn = createBookColumn(DBMediator.queryListings("currentlistings", "Heavily Used", category), "Heavily Used: ");
		buyColumns.add(huColumn);
		browseColumns.add(huColumn, 2, 0);
		
		//special selection handling
		for (ListView<HBox> child : buyColumns) {
			child.getSelectionModel().selectedItemProperty().addListener(
				new ChangeListener<HBox>() {
			    	public void changed(ObservableValue<? extends HBox> ov, HBox old_val, HBox new_val) {
			        	System.out.println("selection made");
			        	//ensure header cannot be clicked
			        	if (child.getSelectionModel().getSelectedIndex() == 0) {
			        		Platform.runLater(() -> child.getSelectionModel().clearSelection() );
			            } else {
			            	for (ListView<HBox> child : buyColumns) {
			            		if (child.getSelectionModel().getSelectedItem() != new_val) {
			            			System.out.println("clearing selection");
			            			//child.getSelectionModel().clearSelection();
			            			//child.getSelectionModel().
			            			Platform.runLater(() -> child.getSelectionModel().clearSelection() );
			            		} else {
			            			System.out.println("Updating Selection Info");
			            			String title = "";
			            			String condition = "";
			            			int listingID = 0;
			            			for (Node hboxChild : new_val.getChildren()) {
			            				if (hboxChild instanceof GridPane) {
			            					for (Node gridChild : ((GridPane) hboxChild).getChildren()) {
			            						if (gridChild instanceof Label) {
			            							System.out.println(((Label) gridChild).getText());
			            						}
			            					}
			            				}
			            			}
			            			
			            			currentSelection.setText(title + " (" + condition + ")");
			            		}
			            	}
			            }
			        }
			    }
			);
		}
		
	}
}
