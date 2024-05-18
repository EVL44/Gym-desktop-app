module gym {
	requires javafx.controls;
	requires javafx.graphics;
	requires javafx.fxml;
	requires javafx.base;
	requires java.sql;
	requires java.desktop;
	
	opens application to javafx.graphics, javafx.fxml;
}
