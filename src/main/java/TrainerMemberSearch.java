import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.*;

public class TrainerMemberSearch {

    public static void start(Connection connection) {
        Stage window = new Stage();
        window.setTitle("Search Members");

        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);

        TextField searchField = new TextField();
        searchField.setPromptText("Enter Member's Name");
        Button searchButton = new Button("Search");

        // list of all members
        ListView<String> listView = new ListView<>();
        ObservableList<String> members = FXCollections.observableArrayList();

        searchButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                members.clear();
                String searchInput= searchField.getText().trim();

                // find the member using ILIKE (ignore cases and see word pattern incase of spelling error)
                String sql = "SELECT * FROM members WHERE first_name ILIKE ? OR last_name ILIKE ?";
                try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                    pstmt.setString(1, searchInput);
                    pstmt.setString(2, searchInput);
                    ResultSet resultSet = pstmt.executeQuery();

                    //add to members list
                    while (resultSet.next()) {
                        String memberDetails = resultSet.getString("first_name") + " " +  resultSet.getString("last_name") + " - " + resultSet.getString("email");
                        members.add(memberDetails);
                    }
                    resultSet.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

                listView.setItems(members);
                listView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        String selectedMember = listView.getSelectionModel().getSelectedItem();
                        if (selectedMember != null && !selectedMember.isEmpty()) {
                            // split the member description to get first and last name of member to search for
                            String[] parts = selectedMember.split(" - ")[0].split(" ");
                            String selectedFirstName = parts[0];
                            // check if user enters one name or both first and last
                            String selectedLastName = (parts.length > 1) ? parts[1] : "";

                            showMemberDetails(connection, selectedFirstName, selectedLastName);
                        }
                    }
                });
            }
        });


        layout.getChildren().addAll(searchField, searchButton, listView);

        Scene scene = new Scene(layout, 400, 300);
        window.setScene(scene);
        window.showAndWait();
    }

    private static void showMemberDetails(Connection connection, String firstName, String lastName) {
        String sql = "SELECT * FROM members WHERE first_name = ? AND last_name = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
                infoAlert.setTitle("Member Details");

                // display all member details
                String memberDetails = "Username: " + resultSet.getString("username") + "\n" +
                        "First Name: " + resultSet.getString("first_name") + "\n" +
                        "Last Name: " + resultSet.getString("last_name") + "\n" +
                        "Email: " + resultSet.getString("email") + "\n" +
                        "Weight: " + resultSet.getFloat("weight") + "\n" +
                        "Height: " + resultSet.getFloat("height") + "\n" +
                        "Fitness Goal: " + resultSet.getString("fitness_goal");
                infoAlert.setContentText(memberDetails);
                infoAlert.showAndWait();
            }
            resultSet.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
