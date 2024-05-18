package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;
import javafx.scene.Node;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.ButtonBar; // Add this import statement

public class MgController {

	@FXML
	private Button add;

	@FXML
	private Button home;

	@FXML
	private Button members;

	@FXML
	private Button menu;

	@FXML
	private Button nothifications;

	@FXML
	private Button settings;

	@FXML
	private TableView<Members> MyTable;

	@FXML
	private TableColumn<Members, Integer> cid;

	@FXML
	private TableColumn<Members, String> nom;

	@FXML
	private TableColumn<Members, Integer> phone;

	@FXML
	private TableColumn<Members, String> prenom;

	@FXML
	private TableColumn<Members, String> status;

	@FXML
	private TextField search;

	@FXML
	private Button idclear;
	@FXML
	private Button idmodify;

	@FXML
	private TextField newfname;

	@FXML
	private TextField newlname;

	@FXML
	private TextField newphone;

	@FXML
	private TextField newduration;

	@FXML
	private TextField newamount;


	ObservableList<Members> data;
	FilteredList<Members> filteredData;

	@FXML
	public void switchToAddMember(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("AddMember.fxml"));
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	@FXML
	void switchToHome(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("dashboard.fxml"));
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	@FXML
	private void initialize() {

		data = FXCollections.observableArrayList();
		filteredData = new FilteredList<>(data, p -> true);
		cid.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
		nom.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNom()));
		prenom.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPrenom()));
		phone.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getNumPhone()).asObject());
		status.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));

		MyTable.setItems(filteredData);
		setupClearListButton();
		getAllMembers();
		setupSearchListener();
		MyTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			if (newSelection != null) {
				// Populate the member details fields with the selected member's information
				newfname.setText(newSelection.getNom());
				newlname.setText(newSelection.getPrenom());
				newphone.setText(String.valueOf(newSelection.getNumPhone()));
				newduration.setText(String.valueOf(newSelection.getDuree()));
				newamount.setText(String.valueOf(newSelection.getAmount()));
			}
		});
	}

	@FXML
	private void modify(ActionEvent event) {
		int selectedIndex = MyTable.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {
			// Get the selected member
			Members selectedMember = MyTable.getSelectionModel().getSelectedItem();

			// Update the member's information with the new values
			selectedMember.setNom(newfname.getText());
			selectedMember.setPrenom(newlname.getText());
			selectedMember.setNumPhone(Integer.parseInt(newphone.getText()));
			selectedMember.setDuree(Integer.parseInt(newduration.getText()));
			selectedMember.setAmount(Integer.parseInt(newamount.getText()));

			// Update the member's information in the database
			if (updateMemberInDatabase(selectedMember)) {
				// Refresh the table to reflect the changes
				MyTable.refresh();

				// Show a confirmation message
				showInfoAlert("Modification Successful", "Member information updated successfully.");
			} else {
				showErrorAlert("Modification Failed", "Failed to update member information.");
			}
		}
	}

	// Add this method to your controller to update member information in the
	// database
	private boolean updateMemberInDatabase(Members member) {
	    try {
	        Connection connection = MysqlConnection.getDBConnection();
	        String sql = "UPDATE members SET nom=?, prenom=?, gender=?, numPhone=?, duree=?, amount=?, status=? WHERE id=?";
	        try (PreparedStatement ps = connection.prepareStatement(sql)) {
	            ps.setString(1, member.getNom());
	            ps.setString(2, member.getPrenom());
	            ps.setString(3, member.getGender());
	            ps.setInt(4, member.getNumPhone());
	            ps.setInt(5, member.getDuree());
	            ps.setInt(6, member.getAmount());
	            ps.setString(7, member.getStatus()); // Make sure this is the correct column index
	            ps.setInt(8, member.getId());

	            int rowsUpdated = ps.executeUpdate();
	            return rowsUpdated > 0;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        System.out.println("Error updating member information in the database.");
	        return false;
	    }
	}


	// Add these methods to your controller to show alert dialogs
	private void showInfoAlert(String title, String content) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(content);
		alert.showAndWait();
	}

	private void showErrorAlert(String title, String content) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(content);
		alert.showAndWait();
	}

	private void setupClearListButton() {

		idclear.setOnAction(event -> {
			if (showClearConfirmationDialog()) {
				clearList();
				clearDatabase();
			}
		});
	}

	private boolean showClearConfirmationDialog() {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Confirmation");
		alert.setHeaderText("Attention !!");
		alert.setContentText("Are you sure you want to clear the list? This will delete all records from the database");

		ButtonType confirmButton = new ButtonType("Clear List");
		ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
		alert.getButtonTypes().setAll(confirmButton, cancelButton);

		Optional<ButtonType> result = alert.showAndWait();

		return result.orElse(cancelButton) == confirmButton;

	}
	
	@FXML
	void clear(ActionEvent event) {
	    if (showClearConfirmationDialog()) {
	        // Clear the table view
	        data.clear();

	        // Clear the database
	        clearDatabase();

	        showInfoAlert("Clear Successful", "Table cleared successfully.");
	    }
	}

	private void clearList() {
		data.clear();

	}

	private void clearDatabase() {
		try {
			Connection connection = MysqlConnection.getDBConnection();
			String sql = "DELETE FROM members";
			try (PreparedStatement ps = connection.prepareStatement(sql)) {
				ps.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Error while clearing the database.");
		}
	}

	public void getAllMembers() {
		data.clear();
		try {
			Connection connection = MysqlConnection.getDBConnection();
			String sql = "SELECT * FROM members";
			try (PreparedStatement ps = connection.prepareStatement(sql)) {
				ResultSet results = ps.executeQuery();
				while (results.next()) {
					int id = results.getInt("id");
					String nom = results.getString("nom");
					String prenom = results.getString("prenom");
					String gender = results.getString("gender");
					int numPhone = results.getInt("numPhone");
					int amount = results.getInt("amount");
					int duree = results.getInt("duree");
					int age = results.getInt("age");
					String status = results.getString("status");

					LocalDate endDate = null;
					java.sql.Date sqlEndDate = results.getDate("endDate");
					if (sqlEndDate != null) {
						endDate = sqlEndDate.toLocalDate();
					}

					data.add(new Members(id, nom, prenom, gender, status, numPhone, amount, duree, age, endDate));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Xampp makhdamch !!");
		}
	}

	private void setupSearchListener() {
		search.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(member -> {
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}
				String lowerCaseFilter = newValue.toLowerCase();

				if (startsWithCaseInsensitive(member.getNom(), lowerCaseFilter)
						|| startsWithCaseInsensitive(member.getPrenom(), lowerCaseFilter)
						|| startsWithCaseInsensitive(member.getStatus(), lowerCaseFilter)) {
					return true;
				}
				return false;
			});
		});
	}

	private boolean startsWithCaseInsensitive(String value, String prefix) {
		return value.toLowerCase().startsWith(prefix);
	}
	
	private boolean deleteMemberFromDatabase(Members member) {
	    try {
	        Connection connection = MysqlConnection.getDBConnection();
	        String sql = "DELETE FROM members WHERE id=?";
	        try (PreparedStatement ps = connection.prepareStatement(sql)) {
	            ps.setInt(1, member.getId());

	            int rowsDeleted = ps.executeUpdate();
	            if (rowsDeleted > 0) {
	                System.out.println("Member deleted successfully. Rows deleted: " + rowsDeleted);
	                return true;
	            } else {
	                System.out.println("No rows deleted. Member may not exist in the database.");
	                return false;
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        System.out.println("Error deleting member from the database.");
	        return false;
	    }
	}

	@FXML
	void delete(ActionEvent event) {
	    int selectedIndex = MyTable.getSelectionModel().getSelectedIndex();
	    if (selectedIndex >= 0) {
	        // Get the selected member
	        Members selectedMember = MyTable.getSelectionModel().getSelectedItem();

	        // Create a copy of the current list of items
	        ObservableList<Members> membersCopy = FXCollections.observableArrayList(MyTable.getItems());

	        // Remove the member from the copied list
	        membersCopy.remove(selectedMember);

	        // Set the modified list to the TableView
	        MyTable.setItems(membersCopy);

	        // Remove the member from the database
	        if (deleteMemberFromDatabase(selectedMember)) {
	            showInfoAlert("Deletion Successful", "Member deleted successfully.");
	        } else {
	            showErrorAlert("Deletion Failed", "Failed to delete member from the database.");
	            // Add this line to refresh the table view if the deletion from the database fails
	            getAllMembers();
	        }
	    } else {
	        showErrorAlert("No Selection", "Please select a member to delete.");
	    }
	}



}
