import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;

public class MemberView {
    static Connection connection = Main.getConnection();
    public static void start() {
        Stage window = new Stage();
        window.setTitle("Member Portal");

        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);

        Label headerLabel = new Label("Would you like to: ");
        headerLabel.setAlignment(Pos.CENTER);

        // new member
        Button registerBtn = new Button("Register");
        registerBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                MemberRegistration.start();
            }
        });

        // existing member
        Button loginBtn = new Button("Login");
        loginBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                MemberLogin.start(connection);
            }
        });

        Button backBtn = new Button("Back");
        backBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                window.close();
            }
        });

        layout.getChildren().addAll(headerLabel, registerBtn, loginBtn, backBtn);

        Scene scene = new Scene(layout, 300, 200);
        window.setScene(scene);
        window.showAndWait();
    }
}

