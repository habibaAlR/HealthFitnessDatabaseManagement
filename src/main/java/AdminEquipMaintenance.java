import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.sql.*;

public class AdminEquipMaintenance {

    public static void start(Connection connection) {
        Stage window = new Stage();
        window.setTitle("Equipment Maintenance Monitoring");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(20,20,20,20));

        // list of all equipment
        ListView<String> equipmentListView = new ListView<>();
        ObservableList<String> equipment = FXCollections.observableArrayList();
        fillEquipmentList(connection, equipment);
        equipmentListView.setItems(equipment);
        GridPane.setConstraints(equipmentListView, 0, 0, 3, 1);

        TextField equipNameField = new TextField();
        equipNameField.setPromptText("Enter equipment name");
        GridPane.setConstraints(equipNameField, 0, 1);

        TextField amountField = new TextField();
        amountField.setPromptText("Enter amount");
        GridPane.setConstraints(amountField, 1, 1);

        // allow user to update, add or delete equipment
        Button updateBtn = new Button("Update Amount");
        Button insertBtn = new Button("Add Equipment");
        Button deleteBtn = new Button("Delete Equipment");
        GridPane.setConstraints(updateBtn, 2, 1);
        GridPane.setConstraints(insertBtn, 2, 2);
        GridPane.setConstraints(deleteBtn, 2, 3);

        updateBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                String selectedEquip = equipmentListView.getSelectionModel().getSelectedItem();
                if (selectedEquip != null && !selectedEquip.isEmpty() && !amountField.getText().isEmpty()) {
                    String equipName = selectedEquip.split(" - ")[0];
                    int newAmount = Integer.parseInt(amountField.getText());
                    String sqlUpdate = "UPDATE equipment SET amount = ? WHERE equip_name = ?";
                    try (PreparedStatement pstmt = connection.prepareStatement(sqlUpdate)) {
                        pstmt.setInt(1, newAmount);
                        pstmt.setString(2, equipName);
                        pstmt.executeUpdate();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    fillEquipmentList(connection, equipment);
                }
            }
        });

        insertBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                String equipName = equipNameField.getText();
                if (!equipName.isEmpty() && !amountField.getText().isEmpty()) {
                    int amount = Integer.parseInt(amountField.getText());
                    String sqlInsert = "INSERT INTO equipment (equip_name, amount) VALUES (?, ?)";
                    try (PreparedStatement pstmt = connection.prepareStatement(sqlInsert)) {
                        pstmt.setString(1, equipName);
                        pstmt.setInt(2, amount);
                        pstmt.executeUpdate();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    fillEquipmentList(connection, equipment);
                }
            }
        });

        deleteBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                String selectedEquip = equipmentListView.getSelectionModel().getSelectedItem();
                if (selectedEquip != null && !selectedEquip.isEmpty()) {
                    String equipName = selectedEquip.split(" - ")[0];
                    String sqlDelete = "DELETE FROM equipment WHERE equip_name = ?";
                    try (PreparedStatement pstmt = connection.prepareStatement(sqlDelete)) {
                        pstmt.setString(1, equipName);
                        pstmt.executeUpdate();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    fillEquipmentList(connection, equipment);
                }
            }
        });

        grid.getChildren().addAll(equipmentListView, equipNameField, amountField, updateBtn, insertBtn, deleteBtn);

        Scene scene = new Scene(grid, 600, 400);
        window.setScene(scene);
        window.showAndWait();
    }

    // fill list with changes
    private static void fillEquipmentList(Connection connection, ObservableList<String> equipment) {
        equipment.clear();
        String sql = "SELECT equip_name, amount FROM equipment";
        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet resultSet = pstmt.executeQuery()) {
            while (resultSet.next()) {
                equipment.add(resultSet.getString("equip_name") + " - Amount: " + resultSet.getInt("amount"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
