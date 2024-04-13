import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

public class RoomBookingManagement {

    public static void start(Connection connection) {
        Stage window = new Stage();
        window.setTitle("Room Booking Management");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));

        // admin can add a new room, or assign a room for a class
        ComboBox<String> optionsComboBox = new ComboBox<>();
        optionsComboBox.getItems().addAll("Add a New Room", "Find a Room for a Class");
        optionsComboBox.setPromptText("Select an Option");
        GridPane.setConstraints(optionsComboBox, 0, 0, 2, 1);

        optionsComboBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                String selectedOption = optionsComboBox.getValue();
                if ("Add a New Room".equals(selectedOption)) {
                    AddRoom(connection);
                } else if ("Find a Room for a Class".equals(selectedOption)) {
                    FindRoom(connection);
                }
            }
        });


        grid.getChildren().add(optionsComboBox);

        Scene scene = new Scene(grid, 400, 300);
        window.setScene(scene);
        window.showAndWait();
    }

    private static void AddRoom(Connection connection) {
        Stage addRoomStage = new Stage();
        addRoomStage.setTitle("Add New Room");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));

        TextField roomNumberField = new TextField();
        roomNumberField.setPromptText("Enter Room Number");
        GridPane.setConstraints(roomNumberField, 0, 0);

        TextField capacityField = new TextField();
        capacityField.setPromptText("Enter Capacity");
        GridPane.setConstraints(capacityField, 0, 1);

        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("Select Date");
        GridPane.setConstraints(datePicker, 0, 2);

        ComboBox<String> startTimeComboBox = new ComboBox<>();
        startTimeComboBox.setPromptText("Select Start Time");
        fillTimeComboBox(startTimeComboBox);
        GridPane.setConstraints(startTimeComboBox, 0, 3);

        ComboBox<String> endTimeComboBox = new ComboBox<>();
        endTimeComboBox.setPromptText("Select End Time");
        fillTimeComboBox(endTimeComboBox);
        GridPane.setConstraints(endTimeComboBox, 1, 3);

        Button addButton = new Button("Add Room");
        GridPane.setConstraints(addButton, 1, 4);

        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                try {
                    int roomNumber = Integer.parseInt(roomNumberField.getText());
                    int capacity = Integer.parseInt(capacityField.getText());
                    LocalDate date = datePicker.getValue();
                    LocalTime startTime = LocalTime.parse(startTimeComboBox.getValue());
                    LocalTime endTime = LocalTime.parse(endTimeComboBox.getValue());

                    Timestamp startTimestamp = Timestamp.from(date.atTime(startTime).atZone(ZoneId.systemDefault()).toInstant());
                    Timestamp endTimestamp = Timestamp.from(date.atTime(endTime).atZone(ZoneId.systemDefault()).toInstant());

                    String sqlInsert = "INSERT INTO room_availability (room_number, start_time, end_time, capacity, booked) VALUES (?, ?, ?, ?, FALSE)";
                    try (PreparedStatement pstmt = connection.prepareStatement(sqlInsert)) {
                        pstmt.setInt(1, roomNumber);
                        pstmt.setTimestamp(2, startTimestamp);
                        pstmt.setTimestamp(3, endTimestamp);
                        pstmt.setInt(4, capacity);
                        pstmt.executeUpdate();
                        new Alert(Alert.AlertType.INFORMATION, "Room added successfully with availability.").showAndWait();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    addRoomStage.close();
                } catch (NumberFormatException ex) {
                    ex.printStackTrace();
                }
            }
        });


        grid.getChildren().addAll(roomNumberField, capacityField, datePicker, startTimeComboBox, endTimeComboBox, addButton);

        Scene scene = new Scene(grid, 400, 300);
        addRoomStage.setScene(scene);
        addRoomStage.showAndWait();
    }

    // adding a time entry every 30 min
    private static void fillTimeComboBox(ComboBox<String> comboBox) {
        for (int hour = 0; hour < 24; hour++) {
            comboBox.getItems().add(String.format("%02d:00", hour));
            comboBox.getItems().add(String.format("%02d:30", hour));
        }
    }

    private static void FindRoom(Connection connection) {
        Stage findRoomStage = new Stage();
        findRoomStage.setTitle("Find a Room for a Class");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));

        ComboBox<String> classComboBox = new ComboBox<>();
        classComboBox.setPromptText("Select a Class");
        fillClassComboBox(connection, classComboBox);
        GridPane.setConstraints(classComboBox, 0, 0);

        ListView<String> roomsListView = new ListView<>();
        GridPane.setConstraints(roomsListView, 0, 1, 2, 1);

        Button showRoomsBtn = new Button("Show Available Rooms");
        GridPane.setConstraints(showRoomsBtn, 1, 0);
        showRoomsBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                String selectedClassDetails = classComboBox.getValue();
                if (selectedClassDetails != null) {
                    fillAvailableRooms(connection, roomsListView, selectedClassDetails);
                }
            }
        });

        roomsListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                String selectedRoomInfo = roomsListView.getSelectionModel().getSelectedItem();
                if (selectedRoomInfo != null && !selectedRoomInfo.isEmpty()) {
                    assignRoomToClass(connection, selectedRoomInfo, classComboBox.getValue());
                    findRoomStage.close();
                }
            }
        });

        grid.getChildren().addAll(classComboBox, showRoomsBtn, roomsListView);

        Scene scene = new Scene(grid, 400, 300);
        findRoomStage.setScene(scene);
        findRoomStage.showAndWait();
    }

    // display the classes in a combo box
    private static void fillClassComboBox(Connection connection, ComboBox<String> comboBox) {
        String sql = "SELECT id, class_name, class_time FROM fitness_classes WHERE room_number IS NULL";
        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet resultSet = pstmt.executeQuery()) {

            while (resultSet.next()) {
                String classDetails = resultSet.getInt("id") + " - " + resultSet.getString("class_name") + " - " + resultSet.getTimestamp("class_time");
                comboBox.getItems().add(classDetails);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // find rooms available from an hour before the class starts to an hour after the class starts
    private static void fillAvailableRooms(Connection connection, ListView<String> listView, String classDetails) {
        // get the timestamp
        String[] parts = classDetails.split(" - ");
        Timestamp classTime = Timestamp.valueOf(parts[2]);

        // get 1 hour before and 1 after
        Timestamp oneHourBefore = new Timestamp(classTime.getTime() - 3600 * 1000);
        Timestamp oneHourAfter = new Timestamp(classTime.getTime() + 3600 * 1000);

        String sql = "SELECT * FROM room_availability WHERE start_time <= ? AND end_time >= ? AND booked = FALSE";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setTimestamp(1, oneHourBefore);
            pstmt.setTimestamp(2, oneHourAfter);
            ResultSet resultSet = pstmt.executeQuery();

            // show all available rooms and add to list
            ObservableList<String> availabilities = FXCollections.observableArrayList();
            while (resultSet.next()) {
                String availabilityInfo = "Room " + resultSet.getInt("room_number") + " - " +
                        "Available from " + resultSet.getTimestamp("start_time") +
                        " to " + resultSet.getTimestamp("end_time");
                availabilities.add(availabilityInfo);
            }
            listView.setItems(availabilities);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // when admin picks a room, assign it to the class
    private static void assignRoomToClass(Connection connection, String selectedRoomInfo, String classDetails) {
        String[] roomParts = selectedRoomInfo.split(" - ");
        int roomNumber = Integer.parseInt(roomParts[0].split(" ")[1]);

        String[] classParts = classDetails.split(" - ");
        int classId = Integer.parseInt(classParts[0]);
        Timestamp classTime = Timestamp.valueOf(classParts[2]);

        try {
            String sqlUpdate = "UPDATE fitness_classes SET room_number = ? WHERE id = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(sqlUpdate)) {
                pstmt.setInt(1, roomNumber);
                pstmt.setInt(2, classId);
                pstmt.executeUpdate();


                String sqlUpdateAvail = "UPDATE room_availability SET booked = TRUE WHERE room_number = ? AND start_time <= ? AND end_time >= ?";
                try (PreparedStatement pstmtAvail = connection.prepareStatement(sqlUpdateAvail)) {
                    pstmtAvail.setInt(1, roomNumber);
                    pstmtAvail.setTimestamp(2, classTime);
                    pstmtAvail.setTimestamp(3, classTime);
                    pstmtAvail.executeUpdate();

                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Room Booking Successful");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("Room " + roomNumber + " has been successfully booked for " + classParts[1] + ".");
                    successAlert.showAndWait();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
