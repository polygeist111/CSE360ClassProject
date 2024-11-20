package application;

public class User {
	private int userID;
	private String status;
	private String username;
	private String password;
	
	User() {
		
	}
	
	//userID
	public int getUserID() {
		return userID;
	}
	
	public void setUserID(int userIDIn) {
		userID = userIDIn;
	}
	
	//status
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String statusIn) {
		status = statusIn;
	}
	
	//username
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String usernameIn) {
		username = usernameIn;
	}
	
	//password
	/*public void setPassword(String passwordIn) {
		password = passwordIn;
	}*/
}
