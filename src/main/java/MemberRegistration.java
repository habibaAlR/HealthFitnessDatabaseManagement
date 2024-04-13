import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.sql.*;

public class MemberRegistration {
    public static void start() {
        Stage window = new Stage();
        window.setTitle("Member Registration");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);

        Label headerLabel = new Label("Please enter your data!");
        headerLabel.setAlignment(Pos.CENTER);

        // ask for a username, first name, last name, password, email
        Label userNameLabel = new Label("Username:");
        GridPane.setConstraints(userNameLabel, 0, 0); // col 0, row 0
        TextField userNameInput = new TextField();
        GridPane.setConstraints(userNameInput, 1, 0); // col 1, row 0

        Label nameLabel = new Label("First Name:");
        GridPane.setConstraints(nameLabel, 0, 1); // col 0, row 1
        TextField nameInput = new TextField();
        GridPane.setConstraints(nameInput, 1, 1); // col 1, row 1

        Label lastNameLabel = new Label("Last Name:");
        GridPane.setConstraints(lastNameLabel, 0, 2); // col 0, row 2
        TextField lastNameInput = new TextField();
        GridPane.setConstraints(lastNameInput, 1, 2); // col 1, row 2

        Label passwordLabel = new Label("Password:");
        GridPane.setConstraints(passwordLabel, 0, 3); // col 0, row 3
        TextField passwordInput = new TextField();
        GridPane.setConstraints(passwordInput, 1, 3); // col 1, row 3

        Label emailLabel = new Label("Email:");
        GridPane.setConstraints(emailLabel, 0, 4); // col 0, row 4
        TextField emailInput = new TextField();
        GridPane.setConstraints(emailInput, 1, 4); // col 1, row 4

        Button registerButton = new Button("Register");
        GridPane.setConstraints(registerButton, 1, 8); // col 1, row 8
        registerButton.setOnAction(e -> {
            try {
                String username = userNameInput.getText();
                String password = passwordInput.getText();
                String firstName = nameInput.getText();
                String lastName = lastNameInput.getText();
                String email = emailInput.getText();

                Connection connection = Main.getConnection();

                registerMember(connection, username, password, firstName, lastName, email);

                // registration successful
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Registration Successful");
                alert.setContentText("You have been registered successfully!");
                Stage stage = (Stage) registerButton.getScene().getWindow();
                stage.close();

                alert.getButtonTypes().setAll(ButtonType.OK);
                alert.showAndWait();
                MemberLogin.start(connection);

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        grid.getChildren().addAll(userNameLabel, userNameInput, nameLabel, nameInput, lastNameLabel, lastNameInput, passwordLabel, passwordInput, emailLabel,
                emailInput, registerButton);

        Scene scene = new Scene(grid, 300, 300);
        window.setScene(scene);
        window.showAndWait();
    }

    // inserting new member into members table
    public static void registerMember(Connection connection, String username, String password, String firstName, String lastName, String email) throws SQLException {
        String sql = "INSERT INTO members (username, password, first_name, last_name, email) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, firstName);
            pstmt.setString(4, lastName);
            pstmt.setString(5, email);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
