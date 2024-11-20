package application;

import org.json.simple.*;
import java.sql.*;

public class DBMediator {
	private static Connection c = null;
	
	private static int userID = -1;
	private static int listingID = -1;
	private static int bookID = -1;

	
	//called once on app boot, connects to db
	//sample code modified from https://www.tutorialspoint.com/sqlite/sqlite_java.html
	public static void initDB() {
		c = null;
	    try {
	       Class.forName("org.sqlite.JDBC");
	       c = DriverManager.getConnection("jdbc:sqlite:src/DB/bookstore.db");
	       Statement stmt = c.createStatement();
	       
	       //create recordCounts table
	       /*String sql = "CREATE TABLE IF NOT EXISTS recordCounts" +
	                        "(bookcount      INT     NOT NULL, " + 
	                        " usercount      INT     NOT NULL, " + 
	                        " listingcount   INT     NOT NULL)";
	       stmt.executeUpdate(sql);*/
	       
	       //create books table
	       stmt.executeUpdate("DROP TABLE IF EXISTS books");
	       String sql = "CREATE TABLE IF NOT EXISTS books" +
                   "(bookid INTEGER PRIMARY KEY     AUTOINCREMENT," +
                   " title              TEXT    NOT NULL, " + 
                   " author             TEXT    NOT NULL, " + 
                   " pubyear           	INT     NOT NULL, " +
                   " category           INT     NOT NULL, " +
                   " condition          INT     NOT NULL, " +
                   " value              INT     NOT NULL)"; 
	       stmt.executeUpdate(sql);
	       
	       //create users table
	       stmt.executeUpdate("DROP TABLE IF EXISTS users");
	       sql = "CREATE TABLE IF NOT EXISTS users" +
                   "(userid INTEGER PRIMARY KEY     AUTOINCREMENT," +
                   " username           TEXT    NOT NULL, " + 
                   " password           TEXT    NOT NULL, " + 
                   " status           	TEXT    NOT NULL)"; 
	       stmt.executeUpdate(sql);
	       createUser("admin", "admin");
	       
	       //create currentListings table
	       stmt.executeUpdate("DROP TABLE IF EXISTS currentListings");
	       sql = "CREATE TABLE IF NOT EXISTS currentListings" +
                   "(listingid INTEGER PRIMARY KEY     AUTOINCREMENT," +
                   " sellerid              INT     NOT NULL, " + 
                   " bookid                INT     NOT NULL, " + 
                   " quantity              INT     NOT NULL, " + 
                   " FOREIGN KEY (sellerid) REFERENCES users (userid)," +
                   " FOREIGN KEY (bookid)   REFERENCES books (bookid))";
	       stmt.executeUpdate(sql);
	       
	       //create executedListings table
	       stmt.executeUpdate("DROP TABLE IF EXISTS executedListings");
	       sql = "CREATE TABLE IF NOT EXISTS executedListings" +
                   "(listingid INTEGER PRIMARY KEY     AUTOINCREMENT," +
                   " sellerid              INT     NOT NULL, " + 
                   " buyerid               INT     NOT NULL, " + 
                   " bookid                INT     NOT NULL, " + 
                   " quantity              INT     NOT NULL, " + 
                   " saleprice             INT     NOT NULL, " + 
                   " FOREIGN KEY (sellerid) REFERENCES users (userid)," +
                   " FOREIGN KEY (buyerid)  REFERENCES users (userid)," +
                   " FOREIGN KEY (bookid)   REFERENCES books (bookid))";
	       stmt.executeUpdate(sql);
	       stmt.close();
	       c.close();
	    } catch ( Exception e ) {
	       System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	       System.exit(0);
	    }
	    System.out.println("Opened database successfully");
	}
	
	
	
	//creates new user object
	public static int createBook(String title, String author, int pubYear, String category, String condition, int value) {
		c = null;
	    try {
	       Class.forName("org.sqlite.JDBC");
	       c = DriverManager.getConnection("jdbc:sqlite:src/DB/bookstore.db");
	       Statement stmt = c.createStatement();
	       //check if user exists
	       ResultSet rs = stmt.executeQuery("SELECT * FROM books WHERE "
	       									+ "    title     = '" + title     + "'"
	       									+ "AND author    = '" + author    + "'"
	       									+ "AND pubyear   = '" + pubYear   + "'"
	       									+ "AND category  = '" + category  + "'"
	       									+ "AND condition = '" + condition + "'"
	       									+ "AND value     = '" + value     + "'");
	       boolean bookExists = false;
	       while ( rs.next() ) {
	          bookExists = true;
	          break;
	       }
	       rs.close();
	       //if yes, return without creating book
	       if (bookExists) {
	    	   System.out.println("Book already exists! No action needed");
	    	   stmt.close();
	    	   c.close();
	    	   return 0;
	       //if not, create book
	       } else {
	    	   //create book
	    	   String sql = "INSERT INTO books (title, author, pubyear, category, condition, value)" +
	    			   		"VALUES ('" + title + "', '" + author + "', " + pubYear + ", '" + category + "', '" + condition + "', " + value + ")";
	    	   stmt.executeUpdate(sql);
	    	   
	    	   //get get bookID
	    	   ResultSet rs2 = stmt.executeQuery("SELECT MAX(bookid) as bookid FROM books");
	    	   int bookID = rs2.getInt("bookid");
	    	  
	    	   rs2.close();
	    	   stmt.close();
	    	   c.close();
	    	   return bookID;
	       }
	    } catch ( Exception e ) {
	       System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	       System.exit(0);
	    }
	    System.out.println("Improper escape from createBook");
		return 0;
	}
	
	
	
	//creates new book object
	public static int createUser(String username, String password) {
		c = null;
	    try {
	       Class.forName("org.sqlite.JDBC");
	       c = DriverManager.getConnection("jdbc:sqlite:src/DB/bookstore.db");
	       Statement stmt = c.createStatement();
	       //check if user exists
	       ResultSet rs = stmt.executeQuery("SELECT * FROM users WHERE username ='" + username + "'");
	       boolean nameExists = false;
	       while ( rs.next() ) {
	          nameExists = true;
	          break;
	       }
	       rs.close();
	       //if yes, error out and ask for new username
	       if (nameExists) {
	    	   System.out.println("Username is taken");
	    	   stmt.close();
	    	   c.close();
	    	   return 0;
	       //if not, create user
	       } else {
	    	   String status = "";
	    	   if (username == "admin") {
	    		   status = "Admin";
	    	   } else {
	    		   status = "Standard";
	    	   }
	    	   String sql = "INSERT INTO users (username, password, status)" +
	    			   		"VALUES ('" + username + "', '" + password + "', '" + status + "')";
	    	   stmt.executeUpdate(sql);
	    	   stmt.close();
	    	   
	    	   c.close();
	    	   return 1;
	       }
	    } catch ( Exception e ) {
	       System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	       System.exit(0);
	    }
	    System.out.println("Improper escape from createUser");
		return 0;
	}

	
	
	//creates new listing object
	public static int createListing(int sellerID, int quantity, String title, String author, int pubYear, String category, String condition, int value) {
		
		//first, create book to list
		int bookID = createBook(title, author, pubYear, category, condition, value);
		
		//then, attach to listing
		c = null;
	    try {
	       Class.forName("org.sqlite.JDBC");
	       c = DriverManager.getConnection("jdbc:sqlite:src/DB/bookstore.db");
	       Statement stmt = c.createStatement();
	 
	       String sql = "INSERT INTO currentListings (sellerid, bookid, quantity)" +
	    			   	"VALUES (" + sellerID + ", " + bookID + ", " + quantity + ")";
	       stmt.executeUpdate(sql);
	       stmt.close();
	       c.close();
	       return 1;
	    } catch ( Exception e ) {
	       System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	       System.exit(0);
	    }
	    System.out.println("Improper escape from createListing");
		return 0;
	}
	
	
	
	//executes listing (moves books from current to executed listings
	public static int executeListing(int listingID, int buyerID, int quantity, int salePrice) {
		c = null;
	    try {
	       Class.forName("org.sqlite.JDBC");
	       c = DriverManager.getConnection("jdbc:sqlite:src/DB/bookstore.db");
	       Statement stmt = c.createStatement();
	       
	       //get listing from currentListings
	       ResultSet rs = stmt.executeQuery("SELECT * FROM currentListings WHERE listingid = '" + listingID + "'");
	       
	       //if this uses up every copy in listing, delete from currentListing
	       		//otherwise, decrement currentListing quantity accordingly
	       if (quantity == rs.getInt("quantity")) {
	    	   //delete listing from currentListings
	    	   stmt.executeUpdate("DELETE FROM currentListings WHERE listingid = '" + listingID + "'");	    	   
	       } else {
	    	   //decrement listing quantity by proper amount
	    	   stmt.executeUpdate("UPDATE currentListings SET quantity = " + (rs.getInt("quantity") - quantity) + " WHERE listingid = " + rs.getInt("listingid"));
	       }
	       
	       //create executedListing
	    	   String sql = "INSERT INTO executedListings (sellerid, buyerid, bookid, quantity, saleprice)" +
	    			   "VALUES (" + rs.getInt("sellerid") + ", " + buyerID + ", " + rs.getInt("bookid") + ", " + quantity + ", " + salePrice + ")";
	    	   stmt.executeUpdate(sql);
	    	   
	    	   stmt.close();
	    	   rs.close();
	    	   c.close();
	    	   return 1;
	       
	    } catch ( Exception e ) {
	       System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	       System.exit(0);
	    }
	    System.out.println("Improper escape from executeListing");
		return 0;
	}
	
	
	
	//called on signin
	public static User authUser(String username, String password) {
		c = null;
	    try {
	       Class.forName("org.sqlite.JDBC");
	       c = DriverManager.getConnection("jdbc:sqlite:src/DB/bookstore.db");
	       Statement stmt = c.createStatement();
	       //check if user and pass match. Returns user object if yes, null if no
	       ResultSet rs = stmt.executeQuery("SELECT * FROM users WHERE username ='" + username + "' AND password = '" + password + "'");
	       boolean nameExists = false;
	       User thisUser = null;
	       while ( rs.next() ) {
	          thisUser = new User();
	          thisUser.setUserID(rs.getInt("userid"));
	          thisUser.setUsername(username);
	          thisUser.setStatus(rs.getString("status"));
	          break;
	       }
	       rs.close();
	       stmt.close();
	       c.close();
	       return thisUser;
	    } catch ( Exception e ) {
	       System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	       System.exit(0);
	    }
	    System.out.println("Improper escape from authUser");
		return null;
	}
	
}

/*
ResultSet rs2 = stmt.executeQuery("SELECT * FROM users");
while (rs2.next()) {
	   System.out.println("Username: " + rs.getString("username") + " Password: " + rs.getString("password"));
}
rs2.close();
*/