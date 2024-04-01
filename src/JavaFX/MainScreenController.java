package JavaFX;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import java.io.IOException;
import java.util.Optional;
import java.util.Scanner;

import javafx.scene.Node;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.PrintWriter;
import java.time.LocalDate;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;



public class MainScreenController {
    @FXML private Button addTaskButton;
    @FXML private Button logoutButton;
    @FXML private ListView<Task> tasksListView;
    @FXML private Label taskTitleLabel;
    @FXML private Label taskDateLabel;
    @FXML private Label taskDescriptionLabel;
    @FXML private Button completeTaskButton;

    private ObservableList<Task> tasks = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        tasksListView.setItems(tasks);
        tasksListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                displayTaskDetails(newValue);
            }
        });
    }

    @FXML
    private void addTask() {
        Dialog<Task> dialog = new Dialog<>();
        dialog.setTitle("Add New Task");
        dialog.setHeaderText("Enter task details");
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField title = new TextField();
        title.setPromptText("Title");
        TextField description = new TextField();
        description.setPromptText("Description");
        DatePicker dueDate = new DatePicker();

        grid.add(new Label("Title:"), 0, 0);
        grid.add(title, 1, 0);
        grid.add(new Label("Description:"), 0, 1);
        grid.add(description, 1, 1);
        grid.add(new Label("Due Date:"), 0, 2);
        grid.add(dueDate, 1, 2);

        Node saveButton = dialog.getDialogPane().lookupButton(saveButtonType);
        saveButton.setDisable(true);

        title.textProperty().addListener((observable, oldValue, newValue) -> saveButton.setDisable(newValue.trim().isEmpty()));

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                return new Task(title.getText(), description.getText(), dueDate.getValue());
            }
            return null;
        });

        Optional<Task> result = dialog.showAndWait();
        result.ifPresent(task -> tasks.add(task));
    }

    @FXML
    private void completeTask() {
        Task selectedTask = tasksListView.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            selectedTask.setCompleted(true);
            // Update the list to reflect changes
            tasksListView.refresh();
        }
    }

    private void displayTaskDetails(Task task) {
        taskTitleLabel.setText("Title: " + task.getTitle());
        taskDateLabel.setText("Date: " + task.getDueDate().toString());
        taskDescriptionLabel.setText("Description: " + task.getDescription());
    }
    @FXML
    private void exportTasks() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showSaveDialog(null); // Consider using a more specific window here

        if (file != null) {
            try {
                saveTasksToFile(file);
                // Success popup
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Export Successful");
                alert.setHeaderText(null);
                alert.setContentText("Tasks have been successfully exported to " + file.getName() + ".");
                alert.showAndWait();
            } catch (Exception e) {
                // Error popup
                Alert errorAlert = new Alert(AlertType.ERROR);
                errorAlert.setTitle("Export Failed");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("An error occurred while exporting tasks: " + e.getMessage());
                errorAlert.showAndWait();
            }
        }
    }


    private void saveTasksToFile(File file) {
        try (PrintWriter writer = new PrintWriter(file)) {
            for (Task task : tasks) {
                writer.println("Title: " + task.getTitle());
                writer.println("Description: " + task.getDescription());
                writer.println("Due Date: " + task.getDueDate());
                writer.println("Completed: " + (task.isCompleted() ? "Yes" : "No"));
                writer.println(); // Adds an empty line between tasks
            }
        } catch (Exception e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }

    @FXML
    private void importTasks() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(null); // Consider using a specific window here

        if (file != null) {
            readTasksFromFile(file);
        }
    }

    private void readTasksFromFile(File file) {
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String titleLine = scanner.nextLine();
                String descriptionLine = scanner.hasNextLine() ? scanner.nextLine() : "";
                String dueDateLine = scanner.hasNextLine() ? scanner.nextLine() : "";
                String completedLine = scanner.hasNextLine() ? scanner.nextLine() : "";
                scanner.nextLine(); // Skip the empty line between tasks
                
                String title = titleLine.replace("Title: ", "");
                String description = descriptionLine.replace("Description: ", "");
                LocalDate dueDate = LocalDate.parse(dueDateLine.replace("Due Date: ", ""));
                boolean isCompleted = "Completed: Yes".equals(completedLine);

                Task task = new Task(title, description, dueDate);
                task.setCompleted(isCompleted);
                tasks.add(task);
            }
        } catch (Exception e) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Import Failed");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("An error occurred while importing tasks: " + e.getMessage());
            errorAlert.showAndWait();
        }
    }

    @FXML
    private void logout() {
        try {
            MyJavaFX.changeScene("LogIn.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
