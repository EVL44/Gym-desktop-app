package application;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class DControler implements Initializable {

	@FXML
	private AreaChart<?, ?> MembersChart;

	@FXML
	private Label Revenue;

	@FXML
	private AreaChart<?, ?> RevenueChart;

	@FXML
	private Button btnClose;

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

	@FXML
	private Label MembersT;

	@FXML
	public void ActionClose(ActionEvent event) {
		javafx.application.Platform.exit();
	}

	@FXML
	public void switchToMembers(ActionEvent event) throws IOException {

		Parent root = FXMLLoader.load(getClass().getResource("MembersDesign.fxml"));
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

	public void initialize(URL url, ResourceBundle resourceBundle) {

		updateTotalMembersAndRevenue();
		memberChart();
		revenueChart();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void memberChart() {

		MembersChart.getData().clear();

		String sql = "SELECT startDate, count(*) FROM members WHERE status = 'paid' GROUP BY startDate ORDER BY TIMESTAMP(startDate) ASC LIMIT 10";

		Connection connect = MysqlConnection.getDBConnection();

		XYChart.Series chart = new XYChart.Series();

		try {
			PreparedStatement prepare = connect.prepareStatement(sql);
			ResultSet result = prepare.executeQuery();

			while (result.next()) {
				chart.getData().add(new XYChart.Data(result.getString(1), result.getDouble(2)));
			}

			MembersChart.getData().add(chart);
			MembersChart.lookup(".chart-plot-background").setStyle("-fx-background-color: transparent;");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void revenueChart() {

		RevenueChart.getData().clear();

		String sql = "SELECT startDate, SUM(amount) AS TotalPaid " + "FROM members WHERE status = 'paid' "
				+ "GROUP BY startDate ORDER BY TIMESTAMP(startDate) ASC";

		Connection connect = MysqlConnection.getDBConnection();

		XYChart.Series chart = new XYChart.Series();

		try {
			PreparedStatement prepare = connect.prepareStatement(sql);
			ResultSet result = prepare.executeQuery();

			double runningTotal = 0;

			while (result.next()) {
				runningTotal += result.getDouble("TotalPaid");
				chart.getData().add(new XYChart.Data(result.getString("startDate"), runningTotal));
			}

			RevenueChart.getData().add(chart);
			RevenueChart.lookup(".chart-plot-background").setStyle("-fx-background-color: transparent;");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/*
	 * @SuppressWarnings({ "rawtypes", "unchecked" }) private void initMemberChart()
	 * { // Initialize MembersChart XYChart.Series series = new XYChart.Series();
	 * series.getData().add(new XYChart.Data("0", 0)); series.getData().add(new
	 * XYChart.Data("1", 5)); series.getData().add(new XYChart.Data("2", 6));
	 * series.getData().add(new XYChart.Data("3", 4));
	 * 
	 * MembersChart.getData().addAll(series);
	 * MembersChart.lookup(".chart-plot-background").
	 * setStyle("-fx-background-color: transparent;"); }
	 * 
	 * @SuppressWarnings({ "unchecked", "rawtypes" }) private void
	 * initRevenueChart() { // Initialize RevenueChart XYChart.Series series = new
	 * XYChart.Series(); series.getData().add(new XYChart.Data("0", 0));
	 * series.getData().add(new XYChart.Data("1", 30)); series.getData().add(new
	 * XYChart.Data("2", 35)); series.getData().add(new XYChart.Data("3", 25));
	 * RevenueChart.getData().addAll(series);
	 * 
	 * RevenueChart.lookup(".chart-plot-background").
	 * setStyle("-fx-background-color: transparent;"); }
	 */

	@SuppressWarnings("unused")
	private void updateTotalMembersAndRevenue() {
		int totalMembers = DatabaseHandler.getTotalMembers();
		int totalRevenue = DatabaseHandler.getTotalRevenue();

		MembersT.setText(String.valueOf(totalMembers));
		Revenue.setText(String.valueOf(totalRevenue));
	}
}
