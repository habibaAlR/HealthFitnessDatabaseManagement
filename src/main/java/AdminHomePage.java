import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.Connection;

public class AdminHomePage {
    public static void start(Connection connection, String username) {
        Stage window = new Stage();
        window.setTitle("Admin Homepage");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20, 20, 20, 20));
        layout.setAlignment(Pos.CENTER);

        // all admin functionalities
        Button roomBookingBtn = new Button("Manage Room Bookings");
        roomBookingBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                RoomBookingManagement.start(connection);
            }
        });

        Button equipMaintenanceBtn = new Button("Monitor Equipment Maintenance");
        equipMaintenanceBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                AdminEquipMaintenance.start(connection);
            }
        });

        Button updateClassScheduleBtn = new Button("Update Class Schedule");
        updateClassScheduleBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                AdminUpdateSchedule.start(connection);
            }
        });

        Button paymentBtn = new Button("Manage Payments");
        paymentBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                AdminManagePayments.start(connection);
            }
        });

        Button logoutBtn = new Button("Logout");
        logoutBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                window.close();
            }
        });

        layout.getChildren().addAll(roomBookingBtn, equipMaintenanceBtn, updateClassScheduleBtn, paymentBtn, logoutBtn);

        Scene scene = new Scene(layout, 400, 300);
        window.setScene(scene);
        window.show();
    }
}
