import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.*;

public class MemberHomePage {
    public static void start(Connection connection, String username) {
        Stage window = new Stage();
        window.setTitle("Homepage");

        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);

        int memberId = getMemberId(connection, username);

        // all member functionalities
        Button updateProfileBtn = new Button("Update Profile");
        updateProfileBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                MemberUpdateProfile.start(connection,username);
            }
        });

        Button startDashboardBtn = new Button("Display Member Dashboard");
        startDashboardBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                MemberDashboard.start(connection, username);
            }
        });

        Button scheduleSessionBtn = new Button("Schedule Personal Training or Group Fitness Classes");
        scheduleSessionBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                MemberScheduleSession.start(connection, memberId);
            }
        });

        Button logoutBtn = new Button("Logout");
        logoutBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                window.close();
            }
        });

        layout.getChildren().addAll(updateProfileBtn, startDashboardBtn, scheduleSessionBtn, logoutBtn);

        Scene scene = new Scene(layout, 400, 300);
        window.setScene(scene);
        window.show();
    }

    // getting  member id to use as param
    private static int getMemberId(Connection connection, String username) {
        String sql = "SELECT id FROM members WHERE username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
