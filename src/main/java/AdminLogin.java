import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.sql.*;

public class AdminLogin {
    // same as MemberLogin
    public static void start(Connection connection) {
        Stage window = new Stage();
        window.setTitle("Admin Login");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);

        Label usernameLabel = new Label("Username:");
        GridPane.setConstraints(usernameLabel, 0, 0);
        TextField usernameInput = new TextField();
        usernameInput.setPromptText("username");
        GridPane.setConstraints(usernameInput, 1, 0);

        Label passwordLabel = new Label("Password:");
        GridPane.setConstraints(passwordLabel, 0, 1);
        PasswordField passwordInput = new PasswordField();
        passwordInput.setPromptText("password");
        GridPane.setConstraints(passwordInput, 1, 1);

        Button loginButton = new Button("Log In");
        GridPane.setConstraints(loginButton, 1, 2);
        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                try {
                    if (loginAdmin(connection, usernameInput.getText(), passwordInput.getText())) {
                        window.close();
                        AdminHomePage.start(connection, usernameInput.getText()); // Navigate to the Admin HomePage
                    } else {
                        // could not find admin or wrong inputs
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Login Failed");
                        alert.setContentText("Invalid username or password. Please try again.");
                        alert.showAndWait();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        grid.getChildren().addAll(usernameLabel, usernameInput, passwordLabel, passwordInput, loginButton);

        Scene scene = new Scene(grid, 300, 200);
        window.setScene(scene);
        window.show();
    }

    // check that admin exists in table
    private static boolean loginAdmin(Connection connection, String username, String password) throws SQLException {
        String sql = "SELECT * FROM admins WHERE username = ? AND password = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                return resultSet.next();
            }
        }
    }
}
