package application;

import java.util.*;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class CartScreen extends Screen {
	private Map<Integer, HBox> cartContents;
	private Button removeFromCart;
	private Button clearCart;
	private final double percentTax = 7.0;
	//meant to reference currently selected HBox in ListView<HBox>
		//see BuyerScreen for complete ListView implementation
	private ListView<GridPane> cartListings;
	private GridPane selectedListing;
	
	CartScreen (Map<Integer, HBox> cartList) {
		cartContents = cartList;
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
		root.add(createTitle("Cart"), 0, 0);
	}

	protected void assembleContent() {
		GridPane content = createContentWindow();
		
		// Cart view start
		//create grid holding Cart list and Total
		GridPane cartGrid = new GridPane();
		cartGrid.setVgap(15);
		cartGrid.setHgap(15);
		
		populateCart();
		cartGrid.add(cartListings, 0, 1);
		
		Label total = new Label("Total: ");
		cartGrid.add(total, 0, 2);
		
		content.add(cartGrid, 0, 0);
		// Cart view end
		
		root.add(content, 0, 1);
	}

	protected void assembleFooter() {
		HBox footer = createFooter();
		
		footer.setPadding(new Insets(0, 0, 0, 15));
		footer.setSpacing(15);
		footer.setAlignment(Pos.CENTER_LEFT);
		
		removeFromCart = new Button("Remove from Cart");
		removeFromCart.setDisable(true);
		removeFromCart.setOnAction(event -> {
			System.out.println("Removing selection from cart oncart page");
			//cartContents.remove(Integer.parseInt(((Label) selectedListing.getChildren().get(3)).getText()));
			cartContents.remove(Integer.parseInt(((Label) selectedListing.getChildren().get(1)).getText()));
			cartListings.getItems().remove(selectedListing);
			Platform.runLater(() -> cartListings.getSelectionModel().clearSelection() );
			selectedListing = null;
			//removeFromCart.setDisable(true);
		});
		footer.getChildren().add(removeFromCart);
		
		clearCart = new Button("Clear Cart");
		clearCart.setOnAction(event -> {
			System.out.println("clearing cart");
			cartContents.clear();
			selectedListing = null;
			int listingCount =  cartListings.getItems().size();
			for (int i = 1; i < listingCount; i++) {
				cartListings.getItems().remove(1);				
			}
			//clearCart.setDisable(true);
			removeFromCart.setDisable(true);
			
		});
		footer.getChildren().add(clearCart);
		
		Button cartButton = new Button("Return to Shopping");
		cartButton.setOnAction(event -> {
			cartContents.forEach((key, value) -> {
				System.out.println("ListingID: " + key + " Title: " + ((Label) value.getChildren().get(0)).getText());
			});
			returnShopping(cartContents);
		});
		footer.getChildren().add(cartButton);
		
		
		//root.add(footer, 0, 2);
		
		Text credit = new Text("Made by CSE 360 Group 16");
		credit.setFont(Font.font("Verdana", FontWeight.NORMAL, 10));
		HBox creditFrame = new HBox(credit);
		creditFrame.setPadding(new Insets(5));
		creditFrame.setAlignment(Pos.BOTTOM_RIGHT);
		
		footer.getChildren().add(createSpacer());
		footer.getChildren().add(creditFrame);
		
		root.add(footer, 0, 2);
	}
	
	
	protected void populateCart() {
		//special selection handling
		cartListings = createCartColumn(percentTax, cartContents);

		cartListings.getSelectionModel().selectedItemProperty().addListener(
			new ChangeListener<GridPane>() {
				public void changed(ObservableValue<? extends GridPane> ov, GridPane old_val, GridPane new_val) {
					System.out.println("selection made");
				    removeFromCart.setDisable(true);
				    //currentSelection.setText("No listing selected");
				    //ensure header cannot be clicked
				    if (cartListings.getSelectionModel().getSelectedIndex() == 0) {
				       	Platform.runLater(() -> cartListings.getSelectionModel().clearSelection() );
				       	selectedListing = null;
			        	removeFromCart.setDisable(true);
				    } else {
				        if (cartListings.getSelectionModel().getSelectedItem() != new_val) {
				        	System.out.println("clearing selection");
				        	selectedListing = null;
				        	removeFromCart.setDisable(true);
				           	//child.getSelectionModel().clearSelection();
				           	//child.getSelectionModel().
					        Platform.runLater(() -> cartListings.getSelectionModel().clearSelection() );
					    } else {
					        //extract listing ID, write in bottom-right text what the selection is
					        if (new_val != null ) {
					        	System.out.println("Updating Selection Info");
					           	//String title = ((Label) new_val.getChildren().get(0)).getText();
					            //String condition = ((Label) new_val.getChildren().get(4)).getText();
					           	//int listingID = Integer.parseInt(((Label) new_val.getChildren().get(3)).getText());
					          	//System.out.println(title + " " + condition + " " + listingID);
					           	//currentSelection.setText(title + " (" + condition + ")");
					          	//enable add or remove from cart as appropriate
					           	selectedListing = new_val;
					      
					           	/*if (new_val.getStyleClass().contains("selectedListing")) {
					           		removeFromCart.setDisable(false);
					           	} else {
				            		addToCart.setDisable(false);
				           		}*/		            				
					    	}
						}
					}
				    if (selectedListing != null) {
				    	removeFromCart.setDisable(false);
				    }
				}
			}
		);
	}
	
}
