package application;

import org.json.simple.*;
import java.sql.*;

public class DBMediator {
	private String dbFolder = "DB/";
	
	private int userID = -1;
	private int listingID = -1;
	private int bookID = -1;
	
	//called once on app boot, loads all txt info into objects
	public static void dbInit() {
		Connection c = null;
	      
	      try {
	         Class.forName("org.sqlite.JDBC");
	         c = DriverManager.getConnection("jdbc:sqlite:src/DB/bookstore.db");
	      } catch ( Exception e ) {
	         System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	         System.exit(0);
	      }
	      System.out.println("Opened database successfully");
	}
	
	//creates new user object
	public static int createUser(String username, String password) {
		
		return 0;
	}
	
	//creates new book object
	public static int createBook() {
		
		return 0;
	}
	
	//creates new listing object
	public static int createListing() {
		
		return 0;
	}
	
	//executes listing (moves listing from currentListings to executedListings
	public static int executeListing() {
		
		return 0;
	}
}
