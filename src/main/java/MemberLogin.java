import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.sql.*;

public class MemberLogin {

    public static void start(Connection connection) {
        Stage window = new Stage();
        window.setTitle("Login");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);

        Label usernameLabel = new Label("Username:");
        GridPane.setConstraints(usernameLabel, 0, 0); // col 0, row 0
        TextField usernameInput = new TextField();
        usernameInput.setPromptText("username");
        GridPane.setConstraints(usernameInput, 1, 0); // col 1, row 0

        Label passwordLabel = new Label("Password:");
        GridPane.setConstraints(passwordLabel, 0, 1); // col 0, row 1
        PasswordField passwordInput = new PasswordField();
        passwordInput.setPromptText("password");
        GridPane.setConstraints(passwordInput, 1, 1); // col 1, row 1

        Button loginButton = new Button("Log In");
        GridPane.setConstraints(loginButton, 1, 2); // col 1, row 2
        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                try {
                    if (loginMember(connection, usernameInput.getText(), passwordInput.getText())) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Login Successful");
                        alert.setContentText("Welcome, " + usernameInput.getText() + "!");
                        alert.showAndWait();

                        Stage stage = (Stage) loginButton.getScene().getWindow();
                        stage.close();
                        MemberHomePage.start(connection, usernameInput.getText());
                    } else {
                        // could not find member or wrong inputs
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

    // check that member exists in table
    private static boolean loginMember(Connection connection, String username, String password) throws SQLException {
        String sql = "SELECT * FROM members WHERE username = ? AND password = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        }
    }
}
