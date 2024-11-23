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
import javafx.scene.text.TextAlignment;

public class CartScreen extends Screen {
	private Map<Integer, HBox> cartContents;
	private Button removeFromCart;
	private Button clearCart;
	private final double percentTax = 7.0;
	//meant to reference currently selected HBox in ListView<HBox>
		//see BuyerScreen for complete ListView implementation
	private ListView<GridPane> cartListings;
	private GridPane selectedListing;
	private Label total;
	private Label tax;
	private GridPane cartGrid;
	private GridPane content;
	private double taxModifier = 1.07;

	
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
		content = createContentWindow();
		
		// Cart view start
		//create grid holding Cart list and Total
		cartGrid = new GridPane();
		cartGrid.setVgap(15);
		cartGrid.setHgap(15);
		
		populateCart();
		cartGrid.add(cartListings, 0, 1);
		
		tax = new Label("Tax: ");
		HBox taxBox = new HBox(createSpacer());
		taxBox.getChildren().add(tax);
		cartGrid.add(taxBox, 0, 2);
		
		total = new Label("Total: ");
		HBox totalBox = new HBox(createSpacer());
		totalBox.getChildren().add(total);
		cartGrid.add(totalBox, 0, 3);
		
		double totalPrice = 0;
        for (int i = 1; i < cartListings.getItems().size(); i++) {
        	//System.out.println(totalPrice);
        	//totalPrice += Double.parseDouble(t1.substring(1));
        	totalPrice += Double.parseDouble(((Label) ((HBox) ((GridPane) cartListings.getItems().get(i)).getChildren().get(4)).getChildren().get(1)).getText().substring(1));
   		}
        tax.setText(String.format("Tax: $%.2f", totalPrice * taxModifier - totalPrice));
        total.setText(String.format("Grand Total: $%.2f", totalPrice * taxModifier));
		
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
			double totalPrice = 0;
	        for (int i = 1; i < cartListings.getItems().size(); i++) {
	        	//System.out.println(totalPrice);
	        	//totalPrice += Double.parseDouble(t1.substring(1));
	        	totalPrice += Double.parseDouble(((Label) ((HBox) ((GridPane) cartListings.getItems().get(i)).getChildren().get(4)).getChildren().get(1)).getText().substring(1));
	   		}
	        tax.setText(String.format("Tax: $%.2f", totalPrice * taxModifier - totalPrice));
	        total.setText(String.format("Grand Total: $%.2f", totalPrice * taxModifier));
			//removeFromCart.setDisable(true);
		});
		footer.getChildren().add(removeFromCart);
		
		clearCart = new Button("Clear Cart");
		clearCart.setOnAction(event -> {
			clearCart();
		});
		footer.getChildren().add(clearCart);
		
		Button checkoutButton = new Button("Check Out");
		checkoutButton.setOnAction(event -> {
			//create confirmation screen
			cartGrid.setVisible(false);
			cartGrid.setManaged(false);
			content.setVgap(5);
			content.setAlignment(Pos.CENTER);
			
			//handle db processing of listings
			int listingCount = cartListings.getItems().size();
			int totalQuantity = 0;
			for (int i = 1; i < listingCount; i++) {
				//db processing
				int listingID = Integer.parseInt(((Label) ((GridPane) cartListings.getItems().get(i)).getChildren().get(1)).getText());
				String title =	((Label) ((HBox) ((GridPane) cartListings.getItems().get(0)).getChildren().get(3)).getChildren().get(1)).getText();
				int quantity =	Integer.parseInt(((TextField) ((HBox) ((GridPane) cartListings.getItems().get(i)).getChildren().get(3)).getChildren().get(1)).getText());
				Double baseValue =	Double.parseDouble(((Label) ((HBox) ((GridPane) cartListings.getItems().get(i)).getChildren().get(4)).getChildren().get(1)).getText().substring(1));
				baseValue *= 100;
				baseValue *= taxModifier;
				int cents = baseValue.intValue();
				totalQuantity += quantity;
				DBMediator.executeListing(listingID, ViewController.currentUser.getUserID(), quantity, cents);		
				//confirm page
				Label completionLabel = new Label(String.format("%s copies of %s have been purchased at $%.2f each", quantity, title, baseValue / 100));
				completionLabel.setTextAlignment(TextAlignment.CENTER);
				HBox completionLabelBox = new HBox(createSpacer());
				completionLabelBox.getChildren().add(completionLabel);
				completionLabelBox.getChildren().add(createSpacer());
				content.add(completionLabelBox, 0, i - 1);
			}
			Label totalLabel = new Label("Your order includes " + totalQuantity + " books for a total price of " + total.getText().substring(13) + "\n\nYou can continue shopping, or sign out or return to the homescreen\nwith the buttons in the top toolbar");
			totalLabel.setTextAlignment(TextAlignment.CENTER);
			HBox totalLabelBox = new HBox(createSpacer());
			totalLabelBox.getChildren().add(totalLabel);
			totalLabelBox.getChildren().add(createSpacer());
			content.add(totalLabelBox, 0, cartListings.getItems().size() - 1);
			
			Button shoppingButton = new Button("Return to Shopping");
			shoppingButton.setOnAction(event1 -> {
				cartContents.forEach((key, value) -> {
					System.out.println("ListingID: " + key + " Title: " + ((Label) value.getChildren().get(0)).getText());
				});
				returnShopping(cartContents);
			});
			HBox shoppingButtonBox = new HBox(createSpacer());
			shoppingButtonBox.getChildren().add(shoppingButton);
			shoppingButtonBox.getChildren().add(createSpacer());
			content.add(shoppingButtonBox, 0, cartListings.getItems().size());
			
			//fix footer
			footer.getChildren().clear();
			Text credit = new Text("Made by CSE 360 Group 16");
			credit.setFont(Font.font("Verdana", FontWeight.NORMAL, 10));
			HBox creditFrame = new HBox(credit);
			creditFrame.setPadding(new Insets(5));
			creditFrame.setAlignment(Pos.BOTTOM_RIGHT);
			footer.getChildren().add(createSpacer());
			footer.getChildren().add(creditFrame);
			
			
			clearCart();
		});
		footer.getChildren().add(checkoutButton);
		
		footer.getChildren().add(createSpacer());
		Button shoppingButton = new Button("Return to Shopping");
		shoppingButton.setOnAction(event -> {
			cartContents.forEach((key, value) -> {
				System.out.println("ListingID: " + key + " Title: " + ((Label) value.getChildren().get(0)).getText());
			});
			returnShopping(cartContents);
		});
		footer.getChildren().add(shoppingButton);
		
		
		//root.add(footer, 0, 2);
		
		Text credit = new Text("Made by CSE 360 Group 16");
		credit.setFont(Font.font("Verdana", FontWeight.NORMAL, 10));
		HBox creditFrame = new HBox(credit);
		creditFrame.setPadding(new Insets(5));
		creditFrame.setAlignment(Pos.BOTTOM_RIGHT);
		
		//footer.getChildren().add(createSpacer());
		footer.getChildren().add(creditFrame);
		
		root.add(footer, 0, 2);
	}
	
	
	protected void populateCart() {
		//special selection handling
		cartListings = createCartColumn(cartContents);
		
		//update total price
        for (int i = 1; i < cartListings.getItems().size(); i++) {
        	Label rowPrice = ((Label) ((HBox) ((GridPane) cartListings.getItems().get(i)).getChildren().get(4)).getChildren().get(1));
        	rowPrice.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                    //System.out.println("Label Text Changed");
                    double totalPrice = 0;
                    for (int i = 1; i < cartListings.getItems().size(); i++) {
                    	//System.out.println(totalPrice);
                    	//totalPrice += Double.parseDouble(t1.substring(1));
                    	totalPrice += Double.parseDouble(((Label) ((HBox) ((GridPane) cartListings.getItems().get(i)).getChildren().get(4)).getChildren().get(1)).getText().substring(1));
               		}
                    tax.setText(String.format("Tax: $%.2f", totalPrice * taxModifier - totalPrice));
                    total.setText(String.format("Grand Total: $%.2f", totalPrice * taxModifier));
                }
            }); 
   		}
        //handle selection cases
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
	
	//clears cart (Called on button and checkout finish)
	protected void clearCart() {
		System.out.println("clearing cart");
		cartContents.clear();
		selectedListing = null;
		int listingCount =  cartListings.getItems().size();
		for (int i = 1; i < listingCount; i++) {
			cartListings.getItems().remove(1);				
		}
		//clearCart.setDisable(true);
		removeFromCart.setDisable(true);
		tax.setText(String.format("Tax: $%.2f", 0.0));
        total.setText(String.format("Grand Total: $%.2f", 0.0));
	}
}
