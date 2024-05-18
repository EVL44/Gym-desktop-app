package application;

import java.io.IOException;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class NController {

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
    private VBox notificationsVBox;
    

    @FXML
    void switchToHome(ActionEvent event) throws IOException {
		 Parent root = FXMLLoader.load(getClass().getResource("dashboard.fxml"));
		 Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		 Scene scene = new Scene(root);
		 stage.setScene(scene);
		 stage.show();
	}

    @FXML
	 public void switchToMembers(ActionEvent event) throws IOException {
		 Parent root = FXMLLoader.load(getClass().getResource("MembersDesign.fxml"));
		 Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		 Scene scene = new Scene(root);
		 stage.setScene(scene);
		 stage.show();
	 }
    
    @FXML
    void switchToAdd(ActionEvent event) throws IOException {
		 Parent root = FXMLLoader.load(getClass().getResource("AddMember.fxml"));
		 Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		 Scene scene = new Scene(root);
		 stage.setScene(scene);
		 stage.show();
    }
    
    
    @FXML
    void initialize() {
        // Called when the FXML file is loaded
        populateNotifications();
    }
    
    private void populateNotifications() {
        List<Members> notPaidMembers = DatabaseHandler.getNotPaidMembers();

        for (Members member : notPaidMembers) {
        	
            String notificationMessage = String.format("%s %s Has not paid yet", member.getNom(), member.getPrenom());
            Label memberLabel = createNotificationLabel(notificationMessage);
            notificationsVBox.getChildren().add(memberLabel);
        }
    }
    
    
    private Label createNotificationLabel(String message) {
        Label notificationLabel = new Label(message);
        notificationLabel.setStyle(
        		"-fx-background-color: #292929;" +
                        "-fx-padding: 20px 288px;" +            
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-border-radius: 20px;"
        );
        return notificationLabel;
    }

}
