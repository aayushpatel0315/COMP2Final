package JavaFX;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.control.Alert;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.stream.Collectors;

public class ToDoListApp extends Application {

	private Stage primaryStage;
	private String username;
	private BorderPane rootLayout;
	private ListView<Task> toDoListView;
	private TextField taskField;
	private DatePicker datePicker;
	private ComboBox<String> hourComboBox;
	private ComboBox<String> minuteComboBox;
	private ComboBox<String> amPmComboBox;

	private File tasksFile;
	private ObservableList<Task> tasks;

	public ToDoListApp(String username) {
		this.username = username;
		tasksFile = new File(username + "_tasks.txt");
	}

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		primaryStage.setTitle("To-Do List - " + username);

		tasks = FXCollections.observableArrayList(loadTasks());

		rootLayout = new BorderPane();
		rootLayout.setPadding(new Insets(20));

		VBox centerVBox = new VBox(10);
		centerVBox.setPadding(new Insets(20));

		HBox taskInputHBox = new HBox(10);
		taskInputHBox.setPadding(new Insets(5, 0, 5, 0));

		taskField = new TextField();
		taskField.setPromptText("Enter new task");

		datePicker = new DatePicker(LocalDate.now());

		hourComboBox = new ComboBox<>(FXCollections.observableArrayList("01", "02", "03", "04", "05", "06", "07", "08",
				"09", "10", "11", "12"));
		hourComboBox.setValue("12");

		minuteComboBox = new ComboBox<>(FXCollections.observableArrayList("00", "01", "02", "03", "04", "05", "06",
				"07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23",
				"24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40",
				"41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57",
				"58", "59"));
		minuteComboBox.setValue("00");

		amPmComboBox = new ComboBox<>(FXCollections.observableArrayList("AM", "PM"));
		amPmComboBox.setValue("AM");

		Button addButton = new Button("Add");
	    addButton.setStyle("-fx-background-color: #bfa575; -fx-text-fill: black;");
		addButton.setOnAction(e -> addTask());

		Button deleteButton = new Button("Delete");
	    deleteButton.setStyle("-fx-background-color: #bfa575; -fx-text-fill: black;");
		deleteButton.setOnAction(e -> deleteTask());

		
		taskInputHBox.getChildren().addAll(taskField, datePicker, hourComboBox, minuteComboBox, amPmComboBox, addButton,
				deleteButton);
		centerVBox.getChildren().addAll(taskInputHBox);

		toDoListView = new ListView<>(tasks);
		centerVBox.getChildren().add(toDoListView);

		Button logoutButton = new Button("Logout");
	    logoutButton.setStyle("-fx-background-color: #bfa575; -fx-text-fill: black;");
		logoutButton.setOnAction(e -> logout());

		Button exportButton = new Button("Export");
	    exportButton.setStyle("-fx-background-color: #bfa575; -fx-text-fill: black;");
		exportButton.setOnAction(e -> exportTasks()); // Set the action to the exportTasks method



	    Button changeThemeButton = new Button("Change Theme");
	    changeThemeButton.setStyle("-fx-background-color: #bfa575; -fx-text-fill: black;");
	    changeThemeButton.setOnAction(e -> changeTheme());

	    HBox bottomLeftHBox = new HBox(10);
	    bottomLeftHBox.setPadding(new Insets(10));
	    bottomLeftHBox.getChildren().addAll(logoutButton, exportButton, changeThemeButton);


		// Remove or comment out the below duplicate lines
		// HBox bottomLeftHBox = new HBox(10);
		// bottomLeftHBox.setPadding(new Insets(10));
		// bottomLeftHBox.getChildren().add(logoutButton);

		rootLayout.setBottom(bottomLeftHBox);
		rootLayout.setCenter(centerVBox);

		
		rootLayout.setBottom(bottomLeftHBox);
		rootLayout.setCenter(centerVBox);

		Scene scene = new Scene(rootLayout, Screen.getPrimary().getBounds().getWidth() / 1.9,
				Screen.getPrimary().getBounds().getHeight() / 2);
		primaryStage.setScene(scene);
		primaryStage.show();

		refreshListView();

		toDoListView.setOnMouseClicked(event -> {
			if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
				Task selectedTask = toDoListView.getSelectionModel().getSelectedItem();
				if (selectedTask != null) {
					openTaskEditor(selectedTask);
				}
			}
		});
	}
	private boolean isDarkTheme = false;

	private void changeTheme() {
	    if (!isDarkTheme) {
	        // Change to dark theme
	        rootLayout.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
	        isDarkTheme = true;
	    } else {
	        // Change back to default theme
	        // Assuming the default is white or the default JavaFX background
	        rootLayout.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
	        // If you want to reset to the very default background (JavaFX default), use:
	        // rootLayout.setBackground(Background.EMPTY);
	        isDarkTheme = false;
	    }
	}


	
	private void exportTasks() {
	    FileChooser fileChooser = new FileChooser();
	    fileChooser.setTitle("Save Export File");
	    fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
	    fileChooser.setInitialFileName(username + "_tasks_export.txt");
	    fileChooser.getExtensionFilters().addAll(
	        new FileChooser.ExtensionFilter("Text Files", "*.txt"),
	        new FileChooser.ExtensionFilter("All Files", "*.*"));
	    
	    File exportFile = fileChooser.showSaveDialog(primaryStage);
	    
	    if (exportFile != null) {
	        try (PrintWriter writer = new PrintWriter(new FileWriter(exportFile))) {
	            for (Task task : tasks) {
	                String formattedDate = task.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	                String formattedTime = task.getTime().format(DateTimeFormatter.ofPattern("HH:mm"));
	                writer.println(task.getDescription() + "," + formattedDate + "," + formattedTime);
	            }
	            showAlert("Tasks Exported Successfully", "Your tasks have been successfully exported to " + exportFile.getPath());
	        } catch (IOException e) {
	            showAlert("Export Failed", "Failed to export tasks. Error: " + e.getMessage());
	        }
	    }
	}


	private void showAlert(String title, String message) {
	    Alert alert = new Alert(Alert.AlertType.INFORMATION);
	    alert.setTitle(title);
	    alert.setHeaderText(null);
	    alert.setContentText(message);
	    alert.showAndWait();
	}

	private void addTask() {
		String taskDescription = taskField.getText();
		LocalDate taskDate = datePicker.getValue();
		String hour = hourComboBox.getValue();
		String minute = minuteComboBox.getValue();
		String amPm = amPmComboBox.getValue();

		int hourValue = Integer.parseInt(hour);
		if (amPm.equals("PM") && hourValue != 12) {
			hourValue += 12;
		} else if (amPm.equals("AM") && hourValue == 12) {
			hourValue = 0;
		}

		LocalTime taskTime = LocalTime.of(hourValue, Integer.parseInt(minute));

		if (!taskDescription.isEmpty()) {
			Task task = new Task(taskDescription, taskDate, taskTime);
			tasks.add(task);
			saveTasks();
		}
	}

	private void deleteTask() {
		Task selectedTask = toDoListView.getSelectionModel().getSelectedItem();
		if (selectedTask != null) {
			tasks.remove(selectedTask);
			saveTasks();
		}
	}

	private void saveTasks() {
		try (PrintWriter writer = new PrintWriter(new FileWriter(tasksFile))) {
			for (Task task : tasks) {
				String formattedDate = task.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
				String formattedTime = task.getTime().format(DateTimeFormatter.ofPattern("HH:mm"));
				writer.println(task.getDescription() + "," + formattedDate + "," + formattedTime);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		// After saving, reload tasks from the file
		tasks.clear(); // Clear the existing tasks
		tasks.addAll(loadTasks()); // Load tasks from the file

		refreshListView(); // Refresh the list view after reloading tasks
	}

	private ObservableList<Task> loadTasks() {
		ObservableList<Task> loadedTasks = FXCollections.observableArrayList();
		if (tasksFile.exists()) {
			try (BufferedReader reader = new BufferedReader(new FileReader(tasksFile))) {
				loadedTasks.addAll(reader.lines().map(line -> {
					String[] parts = line.split(",");
					if (parts.length == 3) {
						String description = parts[0];
						LocalDate date = LocalDate.parse(parts[1]);
						LocalTime time = LocalTime.parse(parts[2]);
						return new Task(description, date, time);
					}
					return null;
				}).filter(task -> task != null).collect(Collectors.toList()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return loadedTasks;
	}

	private void logout() {
		primaryStage.close();
		new LoginApp().start(new Stage());
	}

	private void openTaskEditor(Task task) {
		Stage taskEditorStage = new Stage();
		taskEditorStage.setTitle("Edit Task");
		taskEditorStage.initModality(Modality.APPLICATION_MODAL);
		taskEditorStage.setResizable(false);

		GridPane gridPane = new GridPane();
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		gridPane.setPadding(new Insets(20));

		Label descriptionLabel = new Label("Description:");
		TextField descriptionField = new TextField(task.getDescription());
		gridPane.addRow(0, descriptionLabel, descriptionField);

		Label dateLabel = new Label("Date:");
		DatePicker datePicker = new DatePicker(task.getDate());
		gridPane.addRow(1, dateLabel, datePicker);

		Label timeLabel = new Label("Time:");
		HBox timeBox = new HBox(5);
		ComboBox<String> hourComboBox = new ComboBox<>(FXCollections.observableArrayList("01", "02", "03", "04", "05",
				"06", "07", "08", "09", "10", "11", "12"));
		hourComboBox.setValue(task.getTime().format(DateTimeFormatter.ofPattern("hh")));
		ComboBox<String> minuteComboBox = new ComboBox<>(FXCollections.observableArrayList("00", "01", "02", "03", "04",
				"05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21",
				"22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38",
				"39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55",
				"56", "57", "58", "59"));
		minuteComboBox.setValue(task.getTime().format(DateTimeFormatter.ofPattern("mm")));
		ComboBox<String> amPmComboBox = new ComboBox<>(FXCollections.observableArrayList("AM", "PM"));
		amPmComboBox.setValue(task.getTime().format(DateTimeFormatter.ofPattern("a")));
		timeBox.getChildren().addAll(hourComboBox, new Label(":"), minuteComboBox, amPmComboBox);
		gridPane.addRow(2, timeLabel, timeBox);

		Button saveButton = new Button("Save");
		saveButton.setOnAction(e -> {
			task.setDescription(descriptionField.getText());
			task.setDate(datePicker.getValue());

			int hour = Integer.parseInt(hourComboBox.getValue());
			if (amPmComboBox.getValue().equals("PM") && hour != 12) {
				hour += 12;
			} else if (amPmComboBox.getValue().equals("AM") && hour == 12) {
				hour = 0;
			}

			LocalTime time = LocalTime.of(hour, Integer.parseInt(minuteComboBox.getValue()));
			task.setTime(time);

			taskEditorStage.close();

			saveTasks();
		});
		gridPane.add(saveButton, 1, 3);

		Scene scene = new Scene(gridPane);
		taskEditorStage.setScene(scene);
		taskEditorStage.showAndWait();
	}

	private void refreshListView() {
		tasks.sort(Comparator.comparing(Task::getDate).thenComparing(Task::getTime));
	}

	public static void main(String[] args) {
		launch(args);
	}
}
