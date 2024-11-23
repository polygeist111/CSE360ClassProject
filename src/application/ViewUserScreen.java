package application;

import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ViewUserScreen extends Screen {
    private TableView<UserData> userTable;
    
    public ViewUserScreen() {
        assembleHeader();
        assembleContent();
        assembleFooter();
        screen = new Scene(root);
    }

    @Override
    protected void assembleHeader() {
        HBox header = createHeader();
        root.add(header, 0, 0);
        
        // Add navigation and title
        HBox titleBox = new HBox(10);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(0, 30, 0, 30));
        
        Label title = createTitle("User Management");
        Region spacer = createSpacer();
        Button homeButton = createReturnHomeButton();
        Button signOutButton = createSignOutButton();
        
        titleBox.getChildren().addAll(title, spacer, homeButton, signOutButton);
        root.add(titleBox, 0, 0);
    }

    @Override
    protected void assembleContent() {
        GridPane content = createContentWindow();
        
        // Create the user table
        userTable = new TableView<>();
        
        // Define columns
        TableColumn<UserData, String> usernameCol = new TableColumn<>("Username");
        usernameCol.setCellValueFactory(data -> data.getValue().usernameProperty());
        usernameCol.setPrefWidth(150);
        
        TableColumn<UserData, String> roleCol = new TableColumn<>("Role");
        roleCol.setCellValueFactory(data -> data.getValue().roleProperty());
        roleCol.setPrefWidth(100);
        
        TableColumn<UserData, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(data -> data.getValue().statusProperty());
        statusCol.setPrefWidth(100);
        
        userTable.getColumns().addAll(usernameCol, roleCol, statusCol);
        userTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        // Create buttons for user management
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10));
        
        Button addButton = new Button("Add User");
        Button editButton = new Button("Edit User");
        Button deleteButton = new Button("Delete User");
        
        buttonBox.getChildren().addAll(addButton, editButton, deleteButton);
        
        // Add action handlers
        addButton.setOnAction(e -> showAddUserDialog());
        editButton.setOnAction(e -> {
            UserData selected = userTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                showEditUserDialog(selected);
            }
        });
        deleteButton.setOnAction(e -> {
            UserData selected = userTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                showDeleteConfirmation(selected);
            }
        });
        
        // Add search functionality
        TextField searchField = new TextField();
        searchField.setPromptText("Search users...");
        searchField.setPrefWidth(200);
        
        VBox contentBox = new VBox(10);
        contentBox.setAlignment(Pos.TOP_CENTER);
        contentBox.setPadding(new Insets(20));
        contentBox.getChildren().addAll(searchField, userTable, buttonBox);
        
        content.add(contentBox, 0, 0);
        root.add(content, 0, 1);
        
        // Load sample data
        loadSampleData();
    }

    @Override
    protected void assembleFooter() {
        root.add(createFooter(), 0, 2);
    }
    
    private void showAddUserDialog() {
        Dialog<UserData> dialog = new Dialog<>();
        dialog.setTitle("Add New User");
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        
        TextField username = new TextField();
        PasswordField password = new PasswordField();
        ComboBox<String> role = new ComboBox<>(FXCollections.observableArrayList("Admin", "User"));
        
        grid.add(new Label("Username:"), 0, 0);
        grid.add(username, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(password, 1, 1);
        grid.add(new Label("Role:"), 0, 2);
        grid.add(role, 1, 2);
        
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        dialog.showAndWait().ifPresent(result -> {
            if (username.getText().length() > 0 && password.getText().length() > 0 && role.getValue() != null) {
                UserData newUser = new UserData(username.getText(), role.getValue(), "Active");
                userTable.getItems().add(newUser);
            }
        });
    }
    
    private void showEditUserDialog(UserData user) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Edit User");
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        
        TextField username = new TextField(user.getUsername());
        ComboBox<String> role = new ComboBox<>(FXCollections.observableArrayList("Admin", "User"));
        role.setValue(user.getRole());
        ComboBox<String> status = new ComboBox<>(FXCollections.observableArrayList("Active", "Inactive"));
        status.setValue(user.getStatus());
        
        grid.add(new Label("Username:"), 0, 0);
        grid.add(username, 1, 0);
        grid.add(new Label("Role:"), 0, 1);
        grid.add(role, 1, 1);
        grid.add(new Label("Status:"), 0, 2);
        grid.add(status, 1, 2);
        
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        dialog.showAndWait();
    }
    
    private void showDeleteConfirmation(UserData user) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete User");
        alert.setHeaderText("Are you sure you want to delete this user?");
        alert.setContentText("Username: " + user.getUsername());
        
        alert.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                userTable.getItems().remove(user);
            }
        });
    }
    
    private void loadSampleData() {
        ObservableList<UserData> data = FXCollections.observableArrayList(
            new UserData("admin", "Admin", "Active"),
            new UserData("user1", "User", "Active"),
            new UserData("user2", "User", "Inactive")
        );
        userTable.setItems(data);
    }
}