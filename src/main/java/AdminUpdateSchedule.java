import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AdminUpdateSchedule {

    public static void start(Connection connection) {
        Stage window = new Stage();
        window.setTitle("Class Schedule Updating");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(10, 10, 10, 10));

        ComboBox<String> classComboBox = new ComboBox<>();
        classComboBox.setPromptText("Select a Class (for update)");
        fillClassComboBox(connection, classComboBox);
        GridPane.setConstraints(classComboBox, 0, 0, 2, 1);

        TextField classNameField = new TextField();
        classNameField.setPromptText("Class Name");
        GridPane.setConstraints(classNameField, 0, 1);

        TextField classTimeField = new TextField();
        classTimeField.setPromptText("Class Time (YYYY-MM-DD HH:MM)");
        classTimeField.setPrefWidth(200);
        GridPane.setConstraints(classTimeField, 0, 2);

        Button submitButton = new Button("Submit");
        GridPane.setConstraints(submitButton, 1, 3);

        classComboBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                String selectedClass = classComboBox.getValue();
                if (selectedClass != null && !selectedClass.isEmpty()) {
                    String[] parts = selectedClass.split(" - ");
                    classNameField.setText(parts[1]); // get class name

                    try (PreparedStatement pstmt = connection.prepareStatement("SELECT class_time FROM fitness_classes WHERE id = ?")) {
                        pstmt.setInt(1, Integer.parseInt(parts[0]));
                        ResultSet resultSet = pstmt.executeQuery();
                        if (resultSet.next()) {
                            LocalDateTime classTime = resultSet.getTimestamp("class_time").toLocalDateTime();
                            classTimeField.setText(classTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
                        }
                        resultSet.close();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });


        submitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                String className = classNameField.getText();
                LocalDateTime classTime = LocalDateTime.parse(classTimeField.getText(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                String selectedClass = classComboBox.getValue();
                try {
                    // adding a new class
                    if (selectedClass == null || selectedClass.isEmpty()) {
                        try (PreparedStatement pstmt = connection.prepareStatement("INSERT INTO fitness_classes (class_name, class_time) VALUES (?, ?)")) {
                            pstmt.setString(1, className);
                            pstmt.setTimestamp(2, java.sql.Timestamp.valueOf(classTime));
                            pstmt.executeUpdate();
                        }
                    } else {
                        // update existing class
                        String[] parts = selectedClass.split(" - ");
                        try (PreparedStatement pstmt = connection.prepareStatement("UPDATE fitness_classes SET class_name = ?, class_time = ? WHERE id = ?")) {
                            pstmt.setString(1, className);
                            pstmt.setTimestamp(2, java.sql.Timestamp.valueOf(classTime));
                            pstmt.setInt(3, Integer.parseInt(parts[0]));
                            pstmt.executeUpdate();
                        }
                    }
                    window.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });


        grid.getChildren().addAll(classComboBox, classNameField, classTimeField, submitButton);

        Scene scene = new Scene(grid, 400, 250);
        window.setScene(scene);
        window.showAndWait();
    }

    // get all the classes
    private static void fillClassComboBox(Connection connection, ComboBox<String> comboBox) {
        String sql = "SELECT id, class_name FROM fitness_classes";
        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet resultSet = pstmt.executeQuery()) {

            while (resultSet.next()) {
                // split by id - class name
                comboBox.getItems().add(resultSet.getInt("id") + " - " + resultSet.getString("class_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
