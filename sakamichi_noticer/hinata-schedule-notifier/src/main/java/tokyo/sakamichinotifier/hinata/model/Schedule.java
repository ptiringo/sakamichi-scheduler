package tokyo.sakamichinotifier.hinata.model;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.time.LocalDate;
import java.util.Objects;

public class Schedule {

	private String id;

	private String title;

	private ScheduleType scheduleType;

	private LocalDate scheduleDate;

	private LocalDate scheduleStartDate;

	public Schedule(@NonNull String id, @NonNull String title, @NonNull ScheduleType scheduleType,
			@NonNull LocalDate scheduleDate) {
		this.id = Objects.requireNonNull(id);
		this.title = Objects.requireNonNull(title);
		this.scheduleType = Objects.requireNonNull(scheduleType);
		this.scheduleDate = Objects.requireNonNull(scheduleDate);
	}

}
