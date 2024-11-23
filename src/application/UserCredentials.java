package application;

public class UserCredentials {
    private static String username = "admin";
    private static String password = "admin";
    
    public static String getUsername() {
        return username;
    }
    
    public static void setUsername(String newUsername) {
        username = newUsername;
    }
    
    public static String getPassword() {
        return password;
    }
    
    public static void setPassword(String newPassword) {
        password = newPassword;
    }
    
    public static boolean validateCredentials(String inputUsername, String inputPassword) {
        return inputUsername.equals(username) && inputPassword.equals(password);
    }
}