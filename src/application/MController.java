package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class MController {

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
	private TableColumn<Members, String> DateEnd;

	@FXML
	private TextField search;

	@FXML
	private Button add;

	@FXML
	private Button home;

	@FXML
	private Button members;

	@FXML
	private Button menu;

	@FXML
	private Button notifications;

	@FXML
	private Button settings;

	ObservableList<Members> data;

	FilteredList<Members> filteredData;

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
	void switchToAdd(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("AddMember.fxml"));
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
		DateEnd.setCellValueFactory(
				cellData -> new SimpleObjectProperty<>(cellData.getValue().getEndDate()).asString());

		MyTable.setItems(filteredData);
		setupSearchListener();

		getAllMembers();
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
}
