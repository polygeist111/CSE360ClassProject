package application;

public class ExecutedSale extends Listing {
	private int buyerID;
	private int salePrice; //in cents
	
	ExecutedSale() {
		
	}
	
	//buyerID
	public int getBuyerID() {
		return buyerID;
	}
	
	public void setBuyerID(int buyerIDIn) {
		buyerID = buyerIDIn;
	}
	
	//sale price
	public int getSalePrice() {
		return salePrice;
	}
	
	public void setListingID(int salePriceIn) {
		salePrice = salePriceIn;
	}
}
