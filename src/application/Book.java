package application;

public class Book {
	private int bookID;
	private String title;
	private String author;
	private int publishedYear;
	private String category;
	private String condition;
	private int value; //in cents
	
	Book() {
		
	}
	
	//bookID
	public int getBookID() {
		return bookID;
	}
	
	public void setBookID(int idIn) {
		bookID = idIn;
	}
	
	//title
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String titleIn) {
		title = titleIn;
	}
	
	//author
	public String getAuthor() {
		return author;
	}
	
	public void setAuthor(String authorIn) {
		author = authorIn;
	}
	
	//published year
	public int getYear() {
		return publishedYear;
	}
	
	public void setYear(int yearIn) {
		publishedYear = yearIn;
	}
	
	//category
	public String getCategory() {
		return category;
	}
	
	public void setCategory(String categoryIn) {
		category = categoryIn;
	}
	
	//condition
	public String getCondition() {
		return condition;
	}
	
	public void setCondition(String conditionIn) {
		author = conditionIn;
	}
	
	//value (cents)
	public int getValue() {
		return value;
	}
	
	public void setValue(int valueIn) {
		value = valueIn;
	}
	
}
