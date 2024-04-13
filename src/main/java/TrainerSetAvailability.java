import javafx.collections.*;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;

public class TrainerSetAvailability {

    public static void start(Connection connection, String username) {
        Stage window = new Stage();
        window.setTitle("Set Availability");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20, 20, 20, 20));
        layout.setAlignment(Pos.CENTER);

        // list of training sessions
        ListView<String> trainingSessionsList = new ListView<>();
        ObservableList<String> trainingSessions = getSessionsOrClasses(connection, "training_sessions", username);
        trainingSessionsList.setItems(trainingSessions);

        // list of fitness classes
        ListView<String> fitnessClassesList = new ListView<>();
        ObservableList<String> fitnessClasses = getSessionsOrClasses(connection, "fitness_classes", username);
        fitnessClassesList.setItems(fitnessClasses);

        trainingSessionsList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                setAvailability(connection, username, "training_sessions", trainingSessionsList.getSelectionModel().getSelectedItem());
                trainingSessionsList.setItems(getSessionsOrClasses(connection, "training_sessions", username));
            }
        });

        fitnessClassesList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                setAvailability(connection, username, "fitness_classes", fitnessClassesList.getSelectionModel().getSelectedItem());
                fitnessClassesList.setItems(getSessionsOrClasses(connection, "fitness_classes", username));
            }
        });


        layout.getChildren().addAll(new Label("Training Sessions:"), trainingSessionsList, new Label("Fitness Classes:"), fitnessClassesList);

        Scene scene = new Scene(layout, 400, 600);
        window.setScene(scene);
        window.showAndWait();
    }

    private static ObservableList<String> getSessionsOrClasses(Connection connection, String tableName, String username) {
        ObservableList<String> sessionsOrClasses = FXCollections.observableArrayList();
        String sql;

        // only show the sessions/classes that don't have a trainer assigned to them
        // so that the trainer can pick which class they are available for
        if (tableName.equals("training_sessions")) {
            sql = "SELECT * FROM training_sessions WHERE trainer_username IS NULL";
        } else {
            sql = "SELECT * FROM fitness_classes WHERE instructor_username IS NULL";
        }

        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet resultSet = pstmt.executeQuery()) {

            // adding to list
            while (resultSet.next()) {
                String sessionOrClass;
                if (tableName.equals("training_sessions")) {
                    String sessionName =resultSet.getString("session_name");
                    Timestamp sessionTime =resultSet.getTimestamp("session_time");
                    sessionOrClass = sessionName + " - " + sessionTime.toString();
                } else {
                    String className = resultSet.getString("class_name");
                    Timestamp classTime = resultSet.getTimestamp("class_time");
                    sessionOrClass = className + " - " + classTime.toString();
                }
                sessionsOrClasses.add(sessionOrClass);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return sessionsOrClasses;
    }

        private static void setAvailability(Connection connection, String username, String tableName, String selectedItem) {
        // split the class or session description to get the session or class name
        String[] parts = selectedItem.split(" - ");
        String name = parts[0];

        String trainerOrInstructor;
        String sessionOrClass;

        if (tableName.equals("training_sessions")) {
            trainerOrInstructor = "trainer_username";
            sessionOrClass = "session_name";
        } else {
            trainerOrInstructor = "instructor_username";
            sessionOrClass = "class_name";
        }

        // set that trainer to be the trainer/instructor fpr the class/session
        String sql = "UPDATE " + tableName + " SET " + trainerOrInstructor + " = ? WHERE " + sessionOrClass + " = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, name);
            pstmt.executeUpdate();

            Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
            infoAlert.setTitle("Availability Set");
            infoAlert.setContentText("You are now set as the " + (tableName.equals("training_sessions") ? "trainer" : "instructor") + " for " + name + "!");
            infoAlert.showAndWait();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
