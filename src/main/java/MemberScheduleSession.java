import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.*;

public class MemberScheduleSession {

    public static void start(Connection connection, int memberId) {
        Stage window = new Stage();
        window.setTitle("Schedule Session");

        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);

        // asks member for training session or fitness class in radio buttons
        Label instructionLabel = new Label("Choose the type of session you want to schedule:");
        RadioButton trainingSessionBtn = new RadioButton("Training Session");
        RadioButton fitnessClassBtn = new RadioButton("Group Fitness Class");
        Button submitBtn = new Button("Show Available Sessions");

        ToggleGroup toggleGroup = new ToggleGroup();
        trainingSessionBtn.setToggleGroup(toggleGroup);
        fitnessClassBtn.setToggleGroup(toggleGroup);

        layout.getChildren().addAll(instructionLabel, trainingSessionBtn, fitnessClassBtn, submitBtn);

        submitBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (trainingSessionBtn.isSelected()) {
                    showTrainingSessions(connection, window, memberId);
                } else if (fitnessClassBtn.isSelected()) {
                    showFitnessClasses(connection, window, memberId);
                }
            }});


        Scene scene = new Scene(layout, 400, 200);
        window.setScene(scene);
        window.showAndWait();
    }

    private static void showTrainingSessions(Connection connection, Stage parentStage, int memberId) {
        ObservableList<String> sessions = FXCollections.observableArrayList();
        // gets sessions from database and adds them to the list to display to user
        String sql = "SELECT id, session_name, trainer_username, session_time, price FROM training_sessions WHERE trainer_username IS NOT NULL";
        try (PreparedStatement pstmt = connection.prepareStatement(sql); ResultSet resultSet = pstmt.executeQuery()) {
            while (resultSet.next()) {
                sessions.add(String.format("%d - %s, Trainer: %s, Time: %s, Price: $%.2f",
                        resultSet.getInt("id"), resultSet.getString("session_name"), resultSet.getString("trainer_username"),
                        resultSet.getTimestamp("session_time").toString(), resultSet.getDouble("price")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        showSessions(connection, sessions, parentStage, "Available Training Sessions", memberId, "training");
    }

    private static void showFitnessClasses(Connection connection, Stage parentStage, int memberId) {
        ObservableList<String> classes = FXCollections.observableArrayList();
        // gets classes from database and adds them to the list to display to user
        String sql = "SELECT id, class_name, instructor_username, class_time, price FROM fitness_classes WHERE instructor_username IS NOT NULL";
        try (PreparedStatement pstmt = connection.prepareStatement(sql); ResultSet resultSet = pstmt.executeQuery()) {
            while (resultSet.next()) {
                classes.add(String.format("%d - %s, Instructor: %s, Time: %s, Price: $%.2f",
                        resultSet.getInt("id"), resultSet.getString("class_name"), resultSet.getString("instructor_username"),
                        resultSet.getTimestamp("class_time").toString(), resultSet.getDouble("price")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        showSessions(connection, classes, parentStage, "Available Group Fitness Classes", memberId, "fitness");
    }

    private static void showSessions(Connection connection, ObservableList<String> sessions, Stage parentStage, String title, int memberId, String type) {
        ListView<String> listView = new ListView<>();
        listView.setItems(sessions);
        listView.setPrefHeight(300);
        listView.setPrefWidth(600);

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.initOwner(parentStage);
        dialog.getDialogPane().setContent(listView);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);

        // when member picks a class, it gets added to registered classes with the member_id and the session_or_class_id
        okButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String selectedSession = listView.getSelectionModel().getSelectedItem();
                if (selectedSession != null) {
                    try {
                        int sessionOrClassId = Integer.parseInt(selectedSession.split(" - ")[0]);
                        // split string at each $ to get session price
                        double price = Double.parseDouble(selectedSession.split("\\$")[1]);

                        try (PreparedStatement insertpstmt = connection.prepareStatement(
                                "INSERT INTO registered_classes (member_id, session_or_class_id, type, charge) VALUES (?, ?, ?, ?)")) {
                            insertpstmt.setInt(1, memberId);
                            insertpstmt.setInt(2, sessionOrClassId);
                            insertpstmt.setString(3, type);
                            insertpstmt.setDouble(4, price);
                            insertpstmt.executeUpdate();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        // update how much the member owes after registering
                        try (PreparedStatement updatepstmt = connection.prepareStatement(
                                "UPDATE members SET balance = balance + ? WHERE id = ?")) {
                            updatepstmt.setDouble(1, price);
                            updatepstmt.setInt(2, memberId);
                            updatepstmt.executeUpdate();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        dialog.showAndWait();
    }
}
