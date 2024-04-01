package JavaFX;

import java.time.LocalDate;

public class Task {
    private String title;
    private String description;
    private LocalDate dueDate;
    private boolean isCompleted;

    public Task(String title, String description, LocalDate dueDate) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.isCompleted = false;
    }

    // Getters and setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    @Override
    public String toString() {
        // This method dictates how the task is displayed in the ListView.
        // Adjust this string to display the information as you prefer.
        return title + ": Due " + dueDate + (isCompleted ? " (Completed)" : "");
    }
}
