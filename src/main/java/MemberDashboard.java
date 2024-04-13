import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.*;

public class MemberDashboard {

    public static void start(Connection connection, String username) {
        Stage window = new Stage();
        window.setTitle(username + "'s Dashboard");

        VBox layout = new VBox(10);
        layout.setAlignment(Pos.TOP_CENTER);

        // displays all workout routines from the table

        String sql = "SELECT * FROM exercise_routines";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String routineDetails = "Routine Details:\n";

                if (resultSet.getBoolean("push_ups")) {
                    routineDetails += "Push-ups: " + resultSet.getInt("push_ups_amount") + "\n";
                }
                if (resultSet.getBoolean("jumping_jacks")) {
                    routineDetails += "Jumping Jacks: " + resultSet.getInt("jumping_jacks_amount") + "\n";
                }
                if (resultSet.getBoolean("sit_ups")) {
                    routineDetails += "Sit-ups: " + resultSet.getInt("sit_ups_amount") + "\n";
                }
                if (resultSet.getBoolean("squats")) {
                    routineDetails += "Squats: " + resultSet.getInt("squats_amount") + "\n";
                }
                if (resultSet.getBoolean("lunges")) {
                    routineDetails += "Lunges: " + resultSet.getInt("lunges_amount") + "\n";
                }

                Label detailsLabel = new Label(routineDetails);
                layout.getChildren().addAll(detailsLabel, new Label("----------"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(layout);
        scrollPane.setFitToWidth(true);

        Scene scene = new Scene(scrollPane, 400, 300);
        window.setScene(scene);
        window.showAndWait();
    }
}
