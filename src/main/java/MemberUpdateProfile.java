import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.sql.*;

public class MemberUpdateProfile {

    public static void start(Connection connection, String username) {
        Stage window = new Stage();
        window.setTitle("Update Profile");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setPadding(new Insets(20, 20, 20, 20));

        CheckBox firstNameCheckBox = new CheckBox("Update First Name");
        CheckBox lastNameCheckBox = new CheckBox("Update Last Name");
        CheckBox emailCheckBox = new CheckBox("Update Email");
        CheckBox fitnessGoalCheckBox = new CheckBox("Update Fitness Goal");
        CheckBox weightCheckBox = new CheckBox("Update Weight");
        CheckBox heightCheckBox = new CheckBox("Update Height");

        TextField firstNameInput = new TextField();
        firstNameInput.setPromptText("New First Name");
        TextField lastNameInput = new TextField();
        lastNameInput.setPromptText("New Last Name");
        TextField emailInput = new TextField();
        emailInput.setPromptText("New Email");
        TextField fitnessGoalInput = new TextField();
        fitnessGoalInput.setPromptText("New Fitness Goal");
        TextField weightInput = new TextField();
        weightInput.setPromptText("New Weight (kg)");
        TextField heightInput = new TextField();
        heightInput.setPromptText("New Height (cm)");

        grid.add(firstNameCheckBox, 0, 0);
        grid.add(firstNameInput, 1, 0);
        grid.add(lastNameCheckBox, 0, 1);
        grid.add(lastNameInput, 1, 1);
        grid.add(emailCheckBox, 0, 2);
        grid.add(emailInput, 1, 2);
        grid.add(fitnessGoalCheckBox, 0, 3);
        grid.add(fitnessGoalInput, 1, 3);
        grid.add(weightCheckBox, 0, 4);
        grid.add(weightInput, 1, 4);
        grid.add(heightCheckBox, 0, 5);
        grid.add(heightInput, 1, 5);

        Button updateButton = new Button("Update");
        updateButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                try {
                    String updateQuery = "UPDATE members SET ";
                    boolean needComma = false;

                    if (firstNameCheckBox.isSelected()) {
                        updateQuery += (needComma ? ", " : "") + "first_name = ?";
                        needComma = true;
                    }
                    if (lastNameCheckBox.isSelected()) {
                        updateQuery += (needComma ? ", " : "") + "last_name = ?";
                        needComma = true;
                    }
                    if (emailCheckBox.isSelected()) {
                        updateQuery += (needComma ? ", " : "") + "email = ?";
                        needComma = true;
                    }
                    if (fitnessGoalCheckBox.isSelected()) {
                        updateQuery += (needComma ? ", " : "") + "fitness_goal = ?";
                        needComma = true;
                    }
                    if (weightCheckBox.isSelected()) {
                        updateQuery += (needComma ? ", " : "") + "weight = ?";
                        needComma = true;
                    }
                    if (heightCheckBox.isSelected()) {
                        updateQuery += (needComma ? ", " : "") + "height = ?";
                    }
                    updateQuery += " WHERE username = ?";

                    PreparedStatement pstmt = connection.prepareStatement(updateQuery);
                    int index = 1;

                    if (firstNameCheckBox.isSelected()) {
                        pstmt.setString(index++, firstNameInput.getText());
                    }
                    if (lastNameCheckBox.isSelected()) {
                        pstmt.setString(index++, lastNameInput.getText());
                    }
                    if (emailCheckBox.isSelected()) {
                        pstmt.setString(index++, emailInput.getText());
                    }
                    if (fitnessGoalCheckBox.isSelected()) {
                        pstmt.setString(index++, fitnessGoalInput.getText());
                    }
                    if (weightCheckBox.isSelected()) {
                        pstmt.setFloat(index++, Float.parseFloat(weightInput.getText()));
                    }
                    if (heightCheckBox.isSelected()) {
                        pstmt.setFloat(index++, Float.parseFloat(heightInput.getText()));
                    }
                    pstmt.setString(index, username);

                    int rowsAffected = pstmt.executeUpdate();
                    pstmt.close();

                    if (rowsAffected > 0) {
                        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                        successAlert.setTitle("Success");
                        successAlert.setContentText("Profile updated successfully.");
                        successAlert.showAndWait();
                        window.close();
                    } else {
                        Alert failureAlert = new Alert(Alert.AlertType.ERROR);
                        failureAlert.setTitle("Failure");
                        failureAlert.setContentText("Failed to update the profile.");
                        failureAlert.showAndWait();
                    }
                } catch (SQLException | NumberFormatException ex) {
                    ex.printStackTrace();
                }
            }
        });

        grid.add(updateButton, 1, 6);

        Scene scene = new Scene(grid, 400, 400);
        window.setScene(scene);
        window.showAndWait();
    }
}
