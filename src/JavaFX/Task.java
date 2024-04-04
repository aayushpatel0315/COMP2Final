package JavaFX;

import java.time.LocalDate;
import java.time.LocalTime;

public class Task {
	private String description;
	private LocalDate date;
	private LocalTime time; // New field for time

	public Task(String description, LocalDate date, LocalTime time) {
		this.description = description;
		this.date = date;
		this.time = time;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public LocalTime getTime() {
		return time;
	}

	public void setTime(LocalTime time) {
		this.time = time;
	}

	@Override
	public String toString() {
		// Convert time to 12-hour format
		int hour = time.getHour();
		String amPm;
		if (hour >= 12) {
			amPm = "PM";
			if (hour > 12) {
				hour -= 12;
			}
		} else {
			amPm = "AM";
			if (hour == 0) {
				hour = 12;
			}
		}

		// Format the time in 12-hour format
		String formattedTime = String.format("%02d:%02d %s", hour, time.getMinute(), amPm);

		// Return the string representation including description, date, and formatted
		// time
		return description + " - " + date.toString() + " " + formattedTime;
	}

}
