package JavaFX;

import javafx.application.Application;

import javafx.geometry.Insets;

import javafx.geometry.Pos;

import javafx.scene.Scene;

import javafx.scene.control.*;

import javafx.scene.image.Image;

import javafx.scene.image.ImageView;

import javafx.scene.layout.BorderPane;

import javafx.scene.layout.HBox;

import javafx.scene.layout.VBox;

import javafx.stage.Stage;

import java.util.HashMap;

import java.util.Map;

public class LoginApp extends Application {

	private Stage primaryStage;

	private TextField usernameField;

	private PasswordField passwordField;

	private Map<String, String> credentials;

	@Override

	public void start(Stage primaryStage) {

		this.primaryStage = primaryStage;

		primaryStage.setTitle("Login");

		credentials = new HashMap<>();

		credentials.put("admin", "admin");

		credentials.put("user", "password");

		BorderPane loginLayout = new BorderPane();

		loginLayout.setStyle("-fx-background-color: black; -fx-text-fill: #bfa575;");

		loginLayout.setPadding(new Insets(20));

		HBox centerHBox = new HBox(10);

		centerHBox.setPadding(new Insets(20));

		centerHBox.setAlignment(Pos.CENTER);

		// Load the image

		Image image = new Image("/JavaFX/Doner.png");

		ImageView imageView = new ImageView(image);

		imageView.setFitWidth(500);

		imageView.setFitHeight(500);

		VBox loginVBox = new VBox(10);

		loginVBox.setAlignment(Pos.CENTER_LEFT);

		Label titleLabel = new Label("Login");

		titleLabel.setStyle(
				"-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #bfa575; -fx-font-family: Monradok;");

		usernameField = new TextField();

		usernameField.setPromptText("Username");

		passwordField = new PasswordField();

		passwordField.setPromptText("Password");

		Button loginButton = new Button("Login");

		loginButton.setStyle("-fx-background-color: #bfa575; -fx-text-fill: black;");

		loginButton.setOnAction(e -> login());

		loginVBox.getChildren().addAll(titleLabel, usernameField, passwordField, loginButton);

		centerHBox.getChildren().addAll(imageView, loginVBox);

		loginLayout.setCenter(centerHBox);

		Scene scene = new Scene(loginLayout);

		primaryStage.setScene(scene);

		primaryStage.show();

	}

	private void login() {

		String username = usernameField.getText();

		String password = passwordField.getText();

		if (credentials.containsKey(username) && credentials.get(username).equals(password)) {

			showToDoList(username);

		} else {

			showAlert("Invalid username or password!");

		}

	}

	private void showToDoList(String username) {

		ToDoListApp toDoListApp = new ToDoListApp(username);

		toDoListApp.start(new Stage());

		primaryStage.close();

	}

	private void showAlert(String message) {

		Alert alert = new Alert(Alert.AlertType.ERROR);

		alert.setTitle("Error");

		alert.setHeaderText(null);

		alert.setContentText(message);

		alert.showAndWait();

	}

	public static void main(String[] args) {

		launch(args);

	}

}
