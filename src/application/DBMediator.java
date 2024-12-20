package application;

import org.json.simple.*;
import java.sql.*;
import java.util.*;

public class DBMediator {
	private static Connection c = null;
	
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
	       //stmt.executeUpdate("DROP TABLE IF EXISTS books");
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
	       //stmt.executeUpdate("DROP TABLE IF EXISTS users");
	       sql = "CREATE TABLE IF NOT EXISTS users" +
                   "(userid INTEGER PRIMARY KEY     AUTOINCREMENT," +
                   " username           TEXT    NOT NULL, " + 
                   " password           TEXT    NOT NULL, " + 
                   " status           	TEXT    NOT NULL)"; 
	       //stmt.executeUpdate(sql);
	       createUser("admin", "admin");
	       createUser("reguser", "reguser");
	       
	       //create currentListings table
	       //stmt.executeUpdate("DROP TABLE IF EXISTS currentListings");
	       sql = "CREATE TABLE IF NOT EXISTS currentListings" +
                   "(listingid INTEGER PRIMARY KEY     AUTOINCREMENT," +
                   " sellerid              INT     NOT NULL, " + 
                   " bookid                INT     NOT NULL, " + 
                   " quantity              INT     NOT NULL, " + 
                   " FOREIGN KEY (sellerid) REFERENCES users (userid)," +
                   " FOREIGN KEY (bookid)   REFERENCES books (bookid))";
	       stmt.executeUpdate(sql);
	      
	       
	       


	       
	       //create executedListings table
	       //stmt.executeUpdate("DROP TABLE IF EXISTS executedListings");
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
	       int existingID = -1;
	       while ( rs.next() ) {
	    	  existingID = rs.getInt("bookid");
	          bookExists = true;
	          break;
	       }
	       rs.close();
	       //if yes, return without creating book
	       if (bookExists) {
	    	   System.out.println("Book already exists! No action needed");
	    	   stmt.close();
	    	   c.close();
	    	   return existingID;
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
	public static User createUser(String username, String password) {
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
	    	   return null;
	       //if not, create user
	       } else {
	    	   String status = "";
	    	   if (username == "admin") {
	    		   status = "Admin";
	    		   createListing(1, 2, "C", "Tucker Wood", 2024, "Computer", "Used Like New", 2676);
	    	       createListing(2, 5, "C++", "Tucker Wood", 2023, "Computer", "Used Like New", 3276);
	    	       createListing(1, 1, "C#", "Tucker Wood", 2017, "Computer", "Used Like New", 2999);
	    	       createListing(1, 3, "Java", "Tucker Wood", 2024, "Computer", "Moderately Used", 3000);
	    	       createListing(2, 2, "JavaScript", "Tucker Wood", 2021, "Computer", "Moderately Used", 5999);
	    	       createListing(1, 5, "Swift", "Tucker Wood", 2005, "Computer", "Heavily Used", 2347);
	    	       createListing(2, 1, "MaxMSP", "Tucker Wood", 2022, "Computer", "Heavily Used", 2000);
	    	       
	    	       createListing(3, 2, "Hamlet", "Tucker Wood", 2024, "English Language", "Used Like New", 2676);
	    	       createListing(2, 5, "Romeo and Juliet", "Tucker Wood", 2023, "English Language", "Used Like New", 3276);
	    	       createListing(1, 1, "Othello", "Tucker Wood", 2017, "English Language", "Used Like New", 2999);
	    	       createListing(1, 3, "Macbeth", "Tucker Wood", 2024, "English Language", "Moderately Used", 3000);
	    	       createListing(2, 2, "Midsummer Nights Dream", "Tucker Wood", 2021, "English Language", "Moderately Used", 5999);
	    	       createListing(6, 5, "King Lear", "Tucker Wood", 2005, "English Language", "Heavily Used", 2347);
	    	       createListing(1, 1, "Julius Caesar", "Tucker Wood", 2022, "English Language", "Heavily Used", 2000);
	    	       
	    	       createListing(2, 2, "Algebra", "Tucker Wood", 2024, "Math", "Used Like New", 2676);
	    	       createListing(2, 5, "Geometry", "Tucker Wood", 2023, "Math", "Used Like New", 3276);
	    	       createListing(2, 1, "Trig", "Tucker Wood", 2017, "Math", "Used Like New", 2999);
	    	       createListing(3, 3, "Calculus 3", "Tucker Wood", 2024, "Math", "Moderately Used", 3000);
	    	       createListing(1, 2, "Algebra", "Tucker Wood", 2021, "Math", "Moderately Used", 5999);
	    	       createListing(2, 5, "Discrete", "Tucker Wood", 2005, "Math", "Heavily Used", 2347);
	    	       createListing(4, 1, "Calculus", "Tucker Wood", 2022, "Math", "Heavily Used", 2000);
	    	       
	    	       createListing(1, 2, "Biology", "Tucker Wood", 2024, "Natural Science", "Used Like New", 2676);
	    	       createListing(3, 5, "Geology", "Tucker Wood", 2023, "Natural Science", "Used Like New", 3276);
	    	       createListing(2, 1, "Chem 1", "Tucker Wood", 2017, "Natural Science", "Used Like New", 2999);
	    	       createListing(5, 3, "Chem 2", "Tucker Wood", 2024, "Natural Science", "Moderately Used", 3000);
	    	       createListing(1, 2, "Glaceology", "Tucker Wood", 2021, "Natural Science", "Moderately Used", 5999);
	    	       createListing(2, 5, "Botany", "Tucker Wood", 2005, "Natural Science", "Heavily Used", 2347);
	    	       createListing(2, 1, "Marine Biology", "Tucker Wood", 2022, "Natural Science", "Heavily Used", 2000);
	    	       
	    	       createListing(4, 2, "History of Underwater Basket Weaving", "Tucker Wood", 2024, "Other", "Used Like New", 2676);
	    	       createListing(2, 5, "Politics of the American Southwest", "Tucker Wood", 2023, "Other", "Used Like New", 3276);
	    	       createListing(2, 1, "Understanding Wood", "Tucker Wood", 2017, "Other", "Used Like New", 2999);
	    	       createListing(4, 3, "Architectural Daylighting", "Tucker Wood", 2024, "Other", "Moderately Used", 3000);
	    	       createListing(2, 2, "The Odyssey", "Tucker Wood", 2021, "Other", "Moderately Used", 5999);
	    	       createListing(2, 5, "The Illiad", "Tucker Wood", 2005, "Other", "Heavily Used", 2347);
	    	       createListing(1, 1, "On Tyranny", "Tucker Wood", 2022, "Other", "Heavily Used", 2000);
	    	   } else {
	    		   status = "Standard";
	    	   }
	    	   String sql = "INSERT INTO users (username, password, status)" +
	    			   		"VALUES ('" + username + "', '" + password + "', '" + status + "')";
	    	   stmt.executeUpdate(sql);
	    	   stmt.close();
	    	   
	    	   c.close();
	    	   return authUser(username, password);
	       }
	    } catch ( Exception e ) {
	       System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	       System.exit(0);
	    }
	    System.out.println("Improper escape from createUser");
		return null;
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
	       int queryQuantity = rs.getInt("quantity");
	       int queryBookID = rs.getInt("bookid");
	       int querySellerID = rs.getInt("sellerid");
	       int queryListingID = rs.getInt("listingid");
	       rs.close();
	       //if this uses up every copy in listing, delete from currentListing
	       		//otherwise, decrement currentListing quantity accordingly
	       if (quantity == queryQuantity) {
	    	   //delete listing from currentListings
	    	   stmt.executeUpdate("DELETE FROM currentListings WHERE listingid = '" + listingID + "'");	    	   
	       } else  if (quantity <= queryQuantity){
	    	   //decrement listing quantity by proper amount
	    	   stmt.executeUpdate("UPDATE currentListings SET quantity = " + (queryQuantity - quantity) + " WHERE listingid = " + queryListingID);
	       } else {
	    	   //CODE: add handling for a change in quantity
	       }
	       //System.out.println("made it this far");
	       //create executedListing
	       String sql = "INSERT INTO executedListings (sellerid, buyerid, bookid, quantity, saleprice)" +
	    			 "VALUES (" + querySellerID + ", " + buyerID + ", " + queryBookID + ", " + quantity + ", " + salePrice + ")";
	       stmt.executeUpdate(sql);
	    	   
	       stmt.close();
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
	
	//function to populate buy page columns
	public static ArrayList<Object> queryListings(String table, String condition, String category) {
		ArrayList<Object> results = new ArrayList<>();
		c = null;
	    try {
	       Class.forName("org.sqlite.JDBC");
	       c = DriverManager.getConnection("jdbc:sqlite:src/DB/bookstore.db");
	       Statement stmt = c.createStatement();
	       	       
	       String sql = "SELECT " + table + ".* FROM " + table + ", books WHERE " + table + ".bookid = books.bookid AND books.condition ='" + condition + "' AND books.category = '" + category + "'";
	       //String sql = "SELECT " + table + ".* FROM " + table + ", books";// WHERE " + table + ".bookid = books.bookid";
	       System.out.println(sql);
	       
	       ResultSet rs3 = stmt.executeQuery(sql);
	       while(rs3.next()) {
				System.out.println("listing found ool");
	       }
	       rs3.close();
	       stmt.close();
	       
	       stmt = c.createStatement();
	       ResultSet rs = stmt.executeQuery(sql);
	       while ( rs.next() ) {
	    	  System.out.println("listing found il");	    	   
	          switch (table) {
				case "currentlistings":
					Listing thisListing = new Listing();
					thisListing.setListingID(rs.getInt("listingid"));
					thisListing.setSellerID(rs.getInt("sellerid"));
					thisListing.setBookID(rs.getInt("bookid"));
					thisListing.setQuantity(rs.getInt("quantity"));
					results.add(thisListing);
					break;
				case "executedlistings":
					System.out.println("entering executedListing");
					ExecutedSale thisSale = new ExecutedSale();
					thisSale.setListingID(rs.getInt("listingid"));
					thisSale.setSellerID(rs.getInt("sellerid"));
					thisSale.setBookID(rs.getInt("bookid"));
					thisSale.setQuantity(rs.getInt("quantity"));
					thisSale.setBuyerID(rs.getInt("buyerid"));
					thisSale.setSalePrice(rs.getInt("saleprice"));
					results.add(thisSale);
					break;
				default:
					System.out.println("Invalid Column Query");
					return null;
	    	  }
	       }
	       rs.close();
	       
	       //find and interleave books with associated listings (separated because only one query can run/be accessed at a time)
	       int size = results.size();
	       for (int i = 0; i < size; i++) {
	    	   switch (table) {
	    	   		case "currentlistings":
	    	   			ResultSet rs2 = stmt.executeQuery("SELECT * FROM books WHERE bookid = '" + ((Listing) results.get(2 * i)).getBookID() + "'");
	    	   			Book thisBook = new Book();
	    	   			thisBook.setTitle(rs2.getString("title"));
	    	   			thisBook.setAuthor(rs2.getString("author"));
	    	   			thisBook.setYear(rs2.getInt("pubyear"));
	    	   			thisBook.setValue(rs.getInt("value"));
	    	   			thisBook.setCategory(rs.getString("category"));
	    	   			thisBook.setCondition(rs.getString("condition"));
	    	   			results.add(2 * i + 1, thisBook);
	    	   			rs2.close();
	        	  		break;
	        	  	case "executedlistings":
	        	  		break;
	        	  	default:
						System.out.println("Invalid Column Query");
						return null;
	    	   }
	       }

	       c.close();
	       System.out.println(results.size());
	       return results;
	    } catch ( Exception e ) {
	       System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	       System.exit(0);
	    }
	    System.out.println("Improper escape from queryColumn");
		return null;
	}
	
	//generic function to return all entries in table WHERE column = searchTerm
	public static ArrayList<Object> queryCurrentListings(String column, String searchTerm) {
		ArrayList<Object> results = new ArrayList<>();
		Connection c = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:src/DB/bookstore.db");
			stmt = c.createStatement();
	
			// Modified query to properly join tables and get book information
			String sql = "SELECT cl.*, b.* FROM currentListings cl " +
						"INNER JOIN books b ON cl.bookid = b.bookid ";
			
			if (column != null && searchTerm != null) {
				sql += "WHERE cl." + column + " = '" + searchTerm + "'";
			}
			
			System.out.println("Executing query: " + sql);
			rs = stmt.executeQuery(sql);
			
			while (rs.next()) {
				// Create and populate Listing object
				Listing thisListing = new Listing();
				thisListing.setListingID(rs.getInt("listingid"));
				thisListing.setSellerID(rs.getInt("sellerid"));
				thisListing.setBookID(rs.getInt("bookid"));
				thisListing.setQuantity(rs.getInt("quantity"));
				
				// Create and populate Book object
				Book thisBook = new Book();
				thisBook.setBookID(rs.getInt("bookid"));
				thisBook.setTitle(rs.getString("title"));
				thisBook.setAuthor(rs.getString("author"));
				thisBook.setYear(rs.getInt("pubyear"));
				thisBook.setValue(rs.getInt("value"));
				thisBook.setCategory(rs.getString("category"));
				thisBook.setCondition(rs.getString("condition"));
				
				System.out.println("Found listing: " + thisListing.getListingID() + 
								 " for book: " + thisBook.getTitle());
				
				results.add(thisListing);
				results.add(thisBook);
			}
			
			return results;
			
		} catch (Exception e) {
			System.out.println("Error in queryCurrentListings:");
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (rs != null) rs.close();
				if (stmt != null) stmt.close();
				if (c != null) c.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}


	public static ArrayList<Object> getProfileListings(int userId) {
		ArrayList<Object> results = new ArrayList<>();
		Connection c = null;
	    Statement stmt = null;
	    ResultSet rs = null;
	    
	    try {
	        Class.forName("org.sqlite.JDBC");
	        c = DriverManager.getConnection("jdbc:sqlite:src/DB/bookstore.db");
	        stmt = c.createStatement();
	
	        // Query with explicitly named columns
	        String sql = "SELECT " +
	                    "cl.listingid, " +
	                    "cl.sellerid, " +
	                    "cl.quantity, " +
	                    "b.bookid, " +
	                    "b.title, " +
	                    "b.author, " +
	                    "b.pubyear, " +
	                    "b.category, " +
	                    "b.condition, " +
	                    "b.value " +
	                    "FROM currentListings cl " +
	                    "INNER JOIN books b ON cl.bookid = b.bookid " +
	                    "WHERE cl.sellerid = " + userId;
	        
	        System.out.println("Executing profile listings query: " + sql);
	        rs = stmt.executeQuery(sql);
	        
	        while (rs.next()) {
	            try {
	                Listing thisListing = new Listing();
	                thisListing.setListingID(rs.getInt("listingid"));
	                thisListing.setSellerID(rs.getInt("sellerid"));
	                thisListing.setBookID(rs.getInt("bookid"));
	                thisListing.setQuantity(rs.getInt("quantity"));
	                
	                Book thisBook = new Book();
	                thisBook.setBookID(rs.getInt("bookid"));
	                thisBook.setTitle(rs.getString("title"));
	                thisBook.setAuthor(rs.getString("author"));
	                thisBook.setYear(rs.getInt("pubyear"));
	                thisBook.setValue(rs.getInt("value"));
	                thisBook.setCategory(rs.getString("category"));
	                thisBook.setCondition(rs.getString("condition"));
	                
	                System.out.println("Found: " + thisBook.getTitle() + " - Quantity: " + thisListing.getQuantity());
	                
	                results.add(thisListing);
	                results.add(thisBook);
	            } catch (Exception e) {
	                System.out.println("Error processing row: " + e.getMessage());
	                e.printStackTrace();
	            }
	        }
	        
	        return results;
	        
	    } catch (Exception e) {
	        System.out.println("Database error: " + e.getMessage());
	        e.printStackTrace();
	        return null;
	    } finally {
	        try {
	            if (rs != null) rs.close();
	            if (stmt != null) stmt.close();
	            if (c != null) c.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	}
	
	
	//query function to return user's listing history
	public static ArrayList<Object> queryListingsForUser(String table, String idtype, int userid) {
		ArrayList<Object> results = new ArrayList<>();
		c = null;
	    try {
	       Class.forName("org.sqlite.JDBC");
	       c = DriverManager.getConnection("jdbc:sqlite:src/DB/bookstore.db");
	       Statement stmt = c.createStatement();
	       	       
	       String sql = "SELECT " + table + ".* FROM " + table + ", books WHERE " + table + ".bookid = books.bookid AND " + table + "." + idtype + " = '" + userid + "'";
	       //String sql = "SELECT " + table + ".* FROM " + table + ", books";// WHERE " + table + ".bookid = books.bookid";
	       System.out.println(sql);
	       
	       ResultSet rs3 = stmt.executeQuery(sql);
	       while(rs3.next()) {
				System.out.println("listing found ool");
	       }
	       rs3.close();
	       stmt.close();
	       
	       stmt = c.createStatement();
	       ResultSet rs = stmt.executeQuery(sql);
	       while ( rs.next() ) {
	    	  System.out.println("listing found il");	    	   
	          switch (table) {
				case "currentlistings":
					Listing thisListing = new Listing();
					thisListing.setListingID(rs.getInt("listingid"));
					thisListing.setSellerID(rs.getInt("sellerid"));
					thisListing.setBookID(rs.getInt("bookid"));
					thisListing.setQuantity(rs.getInt("quantity"));
					results.add(thisListing);
					break;
				case "executedlistings":
					System.out.println("entering executedListing");
					ExecutedSale thisSale = new ExecutedSale();
					thisSale.setListingID(rs.getInt("listingid"));
					thisSale.setSellerID(rs.getInt("sellerid"));
					thisSale.setBookID(rs.getInt("bookid"));
					thisSale.setQuantity(rs.getInt("quantity"));
					thisSale.setBuyerID(rs.getInt("buyerid"));
					thisSale.setSalePrice(rs.getInt("saleprice"));
					results.add(thisSale);
					break;
				default:
					System.out.println("Invalid Column Query");
					return null;
	    	  }
	       }
	       rs.close();
	       
	       //find and interleave books with associated listings (separated because only one query can run/be accessed at a time)
	       int size = results.size();
	       for (int i = 0; i < size; i++) {
	    	   Book thisBook = new Book();
	    	   switch (table) {
	    	   		case "currentlistings":
	    	   			ResultSet rs2 = stmt.executeQuery("SELECT * FROM books WHERE bookid = '" + ((Listing) results.get(2 * i)).getBookID() + "'");
	    	   			thisBook.setTitle(rs2.getString("title"));
	    	   			thisBook.setAuthor(rs2.getString("author"));
	    	   			thisBook.setYear(rs2.getInt("pubyear"));
	    	   			thisBook.setValue(rs2.getInt("value"));
	    	   			thisBook.setCategory(rs2.getString("category"));
	    	   			thisBook.setCondition(rs2.getString("condition"));
	    	   			results.add(2 * i + 1, thisBook);
	    	   			rs2.close();
	        	  		break;
	        	  	case "executedlistings":
	        	  		ResultSet rs4 = stmt.executeQuery("SELECT * FROM books WHERE bookid = '" + ((Listing) results.get(2 * i)).getBookID() + "'");
	    	   			thisBook.setTitle(rs4.getString("title"));
	    	   			thisBook.setAuthor(rs4.getString("author"));
	    	   			thisBook.setYear(rs4.getInt("pubyear"));
	    	   			thisBook.setValue(rs4.getInt("value"));
	    	   			thisBook.setCategory(rs4.getString("category"));
	    	   			thisBook.setCondition(rs4.getString("condition"));
	    	   			results.add(2 * i + 1, thisBook);
	    	   			rs4.close();
	        	  		break;
	        	  	default:
						System.out.println("Invalid Column Query");
						return null;
	    	   }
	       }

	       c.close();
	       System.out.println(results.size());
	       return results;
	    } catch ( Exception e ) {
	       System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	       System.exit(0);
	    }
	    System.out.println("Improper escape from queryColumn");
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