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
                   "(listingid INTEGER PRIMARY KEY     NOT NULL UNIQUE," +
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
	public static int createUser(String username, String password) {
		c = null;
	    try {
	       Class.forName("org.sqlite.JDBC");
	       c = DriverManager.getConnection("jdbc:sqlite:src/DB/bookstore.db");
	       Statement stmt = c.createStatement();
	       //check if user exists
	       ResultSet rs = stmt.executeQuery("SELECT username FROM users WHERE username = '" + username + "';");
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
	    	   String sql = "INSERT INTO users (username, password, status)" +
	    			   		"VALUES ('" + username + "', '" + password + "', 'Standard')";
	    	   stmt.executeUpdate(sql);
	    	   stmt.close();
	    	   
	    	   c.close();
	    	   return 1;
	       }
	    } catch ( Exception e ) {
	       System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	       System.exit(0);
	    }
	    System.out.println("Opened database successfully");
		return 0;
	}
	
	
	
	//creates new book object
	public static int createBook() {
		c = null;
	    try {
	       Class.forName("org.sqlite.JDBC");
	       c = DriverManager.getConnection("jdbc:sqlite:src/DB/bookstore.db");
	       Statement stmt = c.createStatement();

	       
	       
	       c.close();
	    } catch ( Exception e ) {
	       System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	       System.exit(0);
	    }
	    System.out.println("Opened database successfully");
		return 0;
	}
	
	
	
	//creates new listing object
	public static int createListing() {
		c = null;
	    try {
	       Class.forName("org.sqlite.JDBC");
	       c = DriverManager.getConnection("jdbc:sqlite:src/DB/bookstore.db");
	       Statement stmt = c.createStatement();

	       
	       
	       c.close();
	    } catch ( Exception e ) {
	       System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	       System.exit(0);
	    }
	    System.out.println("Opened database successfully");
		return 0;
	}
	
	
	
	//executes listing (moves listing from currentListings to executedListings
	public static int executeListing() {
		c = null;
	    try {
	       Class.forName("org.sqlite.JDBC");
	       c = DriverManager.getConnection("jdbc:sqlite:src/DB/bookstore.db");
	       Statement stmt = c.createStatement();

	       
	       
	       c.close();
	    } catch ( Exception e ) {
	       System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	       System.exit(0);
	    }
	    System.out.println("Opened database successfully");
		return 0;
	}
}

/*
ResultSet rs2 = stmt.executeQuery("SELECT * FROM users");
while (rs2.next()) {
	   System.out.println("Username: " + rs.getString("username") + " Password: " + rs.getString("password"));
}
rs2.close();
*/