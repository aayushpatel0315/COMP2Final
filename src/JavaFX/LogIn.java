package JavaFX;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;

public class LogIn {

	public LogIn() {

	}

	@FXML
	private Button login;
	@FXML
	private Label wrongLogIn;
	@FXML
	private TextField username;
	@FXML
	private TextField password;

	public void userLogIn(ActionEvent event) throws IOException {
		checkLogin();
	}

	private void checkLogin() throws IOException {
		MyJavaFX m = new MyJavaFX();
		if (username.getText().toString().equals("admin") && password.getText().toString().equals("admin")) {
			wrongLogIn.setText("Success!");
			m.changeScene("afterLogIn.fxml");
		} else if (username.getText().isEmpty() && password.getText().isEmpty()) {
			wrongLogIn.setText("Please enter your data.");
		} else {
			wrongLogIn.setText("Wrong username or password.");
		}
	}

}
