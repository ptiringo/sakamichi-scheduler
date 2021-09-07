package tokyo.sakamichinotifier.hinata.model;

import com.google.cloud.spring.data.datastore.core.mapping.Entity;
import com.google.cloud.spring.data.datastore.core.mapping.Field;
import lombok.Getter;
import lombok.ToString;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;
import java.util.Optional;

@Entity(name = "hinata_schedule")
@ToString
public class Schedule {

	@Id
	@Field(name = "id")
	@Getter
	private String id;

	@Field(name = "title")
	@Getter
	private String title;

	@Field(name = "schedule_type")
	@Getter
	private ScheduleType scheduleType;

	@Field(name = "schedule_date")
	@Getter
	private LocalDate scheduleDate;

	@Field(name = "start_time")
	private LocalTime startTime;

	@Field(name = "end_time")
	private LocalTime endTime;

	protected Schedule(String id, String title, ScheduleType scheduleType, LocalDate scheduleDate, LocalTime startTime,
			LocalTime endTime) {
		this.id = id;
		this.title = title;
		this.scheduleType = scheduleType;
		this.scheduleDate = scheduleDate;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public Optional<LocalTime> getStartTime() {
		return Optional.ofNullable(this.startTime);
	}

	public Optional<LocalTime> getEndTime() {
		return Optional.ofNullable(this.endTime);
	}

	public static Schedule create(@NonNull String id, @NonNull String title, @NonNull ScheduleType scheduleType,
			@NonNull LocalDate scheduleDate, LocalTime startTime, LocalTime endTime) {
		return new Schedule(Objects.requireNonNull(id), Objects.requireNonNull(title),
				Objects.requireNonNull(scheduleType), Objects.requireNonNull(scheduleDate), startTime, endTime);
	}

	public void update(@Nullable String newTitle, ScheduleType newScheduleType, LocalDate newScheduleDate) {
		this.title = newTitle;
		this.scheduleType = newScheduleType;
		this.scheduleDate = newScheduleDate;
	}

	public boolean hasTime() {
		return this.getStartTime().isPresent();
	}

}
