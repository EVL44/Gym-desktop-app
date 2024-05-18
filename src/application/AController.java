package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AController {

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
	private TextField txtprenom;

	@FXML
	private TextField txtnom;

	@FXML
	private TextField txtnumberphone;

	@FXML
	private TextField txtgender;

	@FXML
	private TextField txtage;

	@FXML
	private TextField txtduration;

	@FXML
	private TextField txtamount;

	@FXML
	public void switchToMembers(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("MembersDesign.fxml"));
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
	void switchToNotif(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("notifications.fxml"));
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	@FXML
	void initialize() {

	}

	@FXML
	void ajouter(ActionEvent event) {
		try {
			String amountText = txtamount.getText().replaceAll("[^0-9]", "");
			String nom = txtnom.getText();
			String prenom = txtprenom.getText();
			int age = Integer.valueOf(txtage.getText());
			String gender = txtgender.getText();
			int phone = Integer.valueOf(txtnumberphone.getText());
			;
			int duree = Integer.valueOf(txtduration.getText());
			int amount = Integer.valueOf(amountText);

			Members m = new Members(nom, prenom, gender, phone, amount, duree, age);

			txtnom.setText("");
			txtprenom.setText("");
			txtage.setText("");
			txtgender.setText("");
			txtnumberphone.setText("");
			txtduration.setText("");
			txtamount.setText("");

			insert(m);
			showAlert(AlertType.INFORMATION, "Success", "Member added successfully.");

		} catch (NumberFormatException e) {
			e.printStackTrace();

			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Invalid Input");
			alert.setContentText("Please enter a valid integer for Age or Duration or Amount.");
			alert.showAndWait();
		}
	}

	private void showAlert(AlertType alertType, String title, String contentText) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(contentText);
		alert.showAndWait();
	}

	public static void insert(Members m) {
		try {
			Connection connection = MysqlConnection.getDBConnection();
			String sql = "INSERT INTO `members` (`id`, `nom`, `prenom`, `gender`, `numPhone`, `amount`, `duree`, `age`, `status`, `endDate`, `startDate`) VALUES (NULL, ?, ?, ?, ?, ?, ?, ?, '', NULL, current_timestamp());";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, m.nom);
			ps.setString(2, m.prenom);
			ps.setString(3, m.gender);
			ps.setInt(4, m.numPhone);
			ps.setInt(5, m.amount);
			ps.setInt(6, m.duree);
			ps.setInt(7, m.age);

			ps.execute();

			DatabaseHandler.updateEndDateForAllMembers();
			DatabaseHandler.updateMemberStatus();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
