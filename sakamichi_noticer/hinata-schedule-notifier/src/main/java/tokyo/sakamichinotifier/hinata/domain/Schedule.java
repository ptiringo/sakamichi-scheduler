package tokyo.sakamichinotifier.hinata.domain;

import com.google.cloud.spring.data.datastore.core.mapping.Entity;
import com.google.cloud.spring.data.datastore.core.mapping.Field;
import lombok.Getter;
import lombok.ToString;

import org.jspecify.nullness.NullMarked;
import org.jspecify.nullness.Nullable;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Entity(name = "hinata_schedule")
@ToString
@NullMarked
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
	private LocalDateTime startTime;

	@Field(name = "end_time")
	private LocalDateTime endTime;

	protected Schedule(@Nullable String id, @Nullable String title, @Nullable ScheduleType scheduleType,
			@Nullable LocalDate scheduleDate, @Nullable LocalDateTime startTime, @Nullable LocalDateTime endTime) {
		this.id = id;
		this.title = title;
		this.scheduleType = scheduleType;
		this.scheduleDate = scheduleDate;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public Optional<LocalDateTime> getStartTime() {
		return Optional.ofNullable(this.startTime);
	}

	public Optional<LocalDateTime> getEndTime() {
		return Optional.ofNullable(this.endTime);
	}

	public static Schedule create(String id, String title, ScheduleType scheduleType, LocalDate scheduleDate,
			@Nullable LocalDateTime startTime, @Nullable LocalDateTime endTime) {
		return new Schedule(Objects.requireNonNull(id), Objects.requireNonNull(title),
				Objects.requireNonNull(scheduleType), Objects.requireNonNull(scheduleDate), startTime, endTime);
	}

	public void update(@Nullable String newTitle, @Nullable ScheduleType newScheduleType,
			@Nullable LocalDate newScheduleDate, @Nullable LocalDateTime startTime, @Nullable LocalDateTime endTime) {
		this.title = newTitle;
		this.scheduleType = newScheduleType;
		this.scheduleDate = newScheduleDate;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public boolean hasTime() {
		return this.getStartTime().isPresent();
	}

}
