package application;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class UserData {
    private final StringProperty username;
    private final StringProperty role;
    private final StringProperty status;
    
    public UserData(String username, String role, String status) {
        this.username = new SimpleStringProperty(username);
        this.role = new SimpleStringProperty(role);
        this.status = new SimpleStringProperty(status);
    }
    
    public String getUsername() { return username.get(); }
    public String getRole() { return role.get(); }
    public String getStatus() { return status.get(); }
    
    public StringProperty usernameProperty() { return username; }
    public StringProperty roleProperty() { return role; }
    public StringProperty statusProperty() { return status; }
    
    public void setStatus(String status) { this.status.set(status); }
    public void setRole(String role) { this.role.set(role); }
}