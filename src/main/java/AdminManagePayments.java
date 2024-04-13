import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.sql.*;

public class AdminManagePayments {

    public static void start(Connection connection) {
        Stage window = new Stage();
        window.setTitle("Manage Payments");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);

        // all members in a combo box
        ComboBox<String> memberComboBox = new ComboBox<>();
        memberComboBox.setPromptText("Select a Member");
        ObservableList<String> members = FXCollections.observableArrayList();
        // make full name from first and last
        String sqlMember = "SELECT id, CONCAT(first_name, ' ', last_name) AS member_name FROM members";
        try (PreparedStatement pstmt = connection.prepareStatement(sqlMember)) {
            ResultSet resultSet = pstmt.executeQuery();
            while (resultSet.next()) {
                members.add(resultSet.getInt("id") + " - " + resultSet.getString("member_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        memberComboBox.setItems(members);

        Label owedAmountLabel = new Label("Owed Amount: $0.00");
        TextField amountField = new TextField();
        amountField.setPromptText("Payment Amount");
        amountField.setDisable(true);
        Button payButton = new Button("Process Payment");
        payButton.setDisable(true);

        memberComboBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                String selectedMember = memberComboBox.getValue();
                if (selectedMember != null) {
                    String[] parts = selectedMember.split(" - ");
                    int memberId = Integer.parseInt(parts[0]);

                    // get owed amount for that member selected
                    double owedAmount = calculateOwedAmount(connection, memberId);
                    owedAmountLabel.setText("Owed Amount: $" + String.format("%.2f", owedAmount));

                    amountField.setDisable(owedAmount <= 0);
                    payButton.setDisable(owedAmount <= 0);
                }
            }
        });


        payButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                TextInputDialog cardNumber = new TextInputDialog();
                cardNumber.setTitle("Card Number");
                cardNumber.setHeaderText("Payment Processing");
                cardNumber.setContentText("Please enter your card number:");
                cardNumber.showAndWait();

                TextInputDialog pin = new TextInputDialog();
                pin.setTitle("PIN");
                pin.setHeaderText("Payment Processing");
                pin.setContentText("Please enter your PIN:");
                pin.showAndWait();

                String selectedMember = memberComboBox.getValue();
                if (selectedMember != null) {
                    String[] parts = selectedMember.split(" - ");
                    int memberId = Integer.parseInt(parts[0]);
                    double paymentAmount = Double.parseDouble(amountField.getText());

                    // remove amount from owed amount and update
                    String sqlUpdate = "UPDATE members SET balance = balance - ? WHERE id = ?";
                    try (PreparedStatement pstmt = connection.prepareStatement(sqlUpdate)) {
                        pstmt.setDouble(1, paymentAmount);
                        pstmt.setInt(2, memberId);
                        pstmt.executeUpdate();

                        new Alert(Alert.AlertType.INFORMATION, "Payment processed successfully for $" + String.format("%.2f", paymentAmount)).showAndWait();
                        window.close();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        GridPane.setConstraints(memberComboBox, 0, 0);
        GridPane.setConstraints(owedAmountLabel, 0, 1);
        GridPane.setConstraints(amountField, 0, 2);
        GridPane.setConstraints(payButton, 0, 3);

        grid.getChildren().addAll(memberComboBox, owedAmountLabel, amountField, payButton);

        Scene scene = new Scene(grid, 400, 300);
        window.setScene(scene);
        window.showAndWait();
    }

    // get balance form  members table
    private static double calculateOwedAmount(Connection connection, int memberId) {
        String sql = "SELECT balance FROM members WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, memberId);
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                return resultSet.getDouble("balance");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
