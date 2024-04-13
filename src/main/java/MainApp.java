import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Health and Fitness Club Management System");

        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);

        Label headerLabel = new Label("Welcome to the Health and Fitness Club. Are you a:");
        headerLabel.setAlignment(Pos.CENTER);

        Button memberBtn = new Button("Member");
        memberBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                MemberView.start();
            }
        });

        Button trainerBtn = new Button("Trainer");
        trainerBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                TrainerView.start();
            }
        });

        Button adminBtn = new Button("Admin");
        adminBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                AdminView.start();
            }
        });

        Button exitBtn = new Button("Exit");
        exitBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                primaryStage.close();
            }
        });


        layout.getChildren().addAll(headerLabel, memberBtn, trainerBtn, adminBtn, exitBtn);
        Scene scene = new Scene(layout, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
