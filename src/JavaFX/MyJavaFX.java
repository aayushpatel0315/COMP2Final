package JavaFX;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MyJavaFX extends Application {
    
    // Static stage
    private static Stage stg;

    @Override
    public void start(Stage primaryStage) throws Exception {
        stg = primaryStage; // Assign the primary stage to your static stage variable
        primaryStage.setResizable(false); // Optional: Make the window not resizable
        Parent root = FXMLLoader.load(getClass().getResource("LogIn.fxml")); // Load the initial FXML
        primaryStage.setTitle("To-Do List Application"); // Set the window title
        primaryStage.setScene(new Scene(root, 600, 400)); // Set the initial scene
        primaryStage.show(); // Show the stage
    }
    
    // Static method to change scenes
    public static void changeScene(String fxml) throws IOException {
        Parent pane = FXMLLoader.load(MyJavaFX.class.getResource(fxml)); // Load the FXML for the new scene
        stg.getScene().setRoot(pane); // Set the root of the scene to the newly loaded pane
    }

    // Main method to launch the application
    public static void main(String[] args) {
        launch(args); // Launch the JavaFX application
    }
}
