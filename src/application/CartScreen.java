package application;

import java.util.*;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
	//meant to reference currently selected HBox in ListView<HBox>
		//see BuyerScreen for complete ListView implementation
	private HBox selectedListing;
	private GridPane browseCart;
	
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
		
		
		ArrayList<String> colTitles = new ArrayList<>();
		colTitles.add("Title");
		colTitles.add("Price per Unit");
		colTitles.add("Quantity");
		colTitles.add("Price");
		ListView<HBox> cartColumn = createCartColumn(null, colTitles,  cartContents);
		cartGrid.add(cartColumn, 0, 1);
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
			cartContents.remove(Integer.parseInt(((Label) selectedListing.getChildren().get(3)).getText()));
			//removeFromCart.setDisable(true);
		});
		footer.getChildren().add(removeFromCart);
		
		clearCart = new Button("Add to Cart");
		clearCart.setDisable(true);
		clearCart.setOnAction(event -> {
			System.out.println("clearing cart");
			cartContents.clear();
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

}
