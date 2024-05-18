package application;

import java.util.logging.Logger;
import java.util.logging.Level;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LController {

	 @FXML
	    private Button btnok;

	    @FXML
	    private PasswordField pass;

	    @FXML
	    private TextField name;
	    
        Connection con;
        PreparedStatement pst;
        ResultSet rs;

	    @FXML
	    void logIn(ActionEvent event) throws IOException {
	     
	    	String uname = name.getText(); 
	    	String password = pass.getText(); 
	    	
	    	if (uname.equals("") || password.equals("")) {
	    		
	    			JOptionPane.showMessageDialog(null,"Enter the Username Or Password !!");
	    			
			}else {
				try {
					Class.forName("com.mysql.cj.jdbc.Driver");
					con= DriverManager.getConnection("jdbc:mysql://localhost/kasabagym", "root","");
					pst=con.prepareStatement("select * from admin where username=? and password=? ");
					pst.setString(1, uname);
					pst.setString(2, password);
					rs =pst.executeQuery();
					if (rs.next()) {
						
						JOptionPane.showMessageDialog(null,"Login Succes");
						
							 Parent root = FXMLLoader.load(getClass().getResource("dashboard.fxml"));
							 Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
							 Scene scene = new Scene(root);
							 stage.setScene(scene);
							 stage.show();
						

					}else {
						JOptionPane.showMessageDialog(null,"Login Failed");
						name.setText("");
						pass.setText("");
						name.requestFocus();
						
					}
				}catch (ClassNotFoundException | SQLException ex) {
				    ex.printStackTrace();  
				    Logger.getLogger(LController.class.getName()).log(Level.SEVERE, "An exception occurred", ex);
				}

			}
	    }

}
