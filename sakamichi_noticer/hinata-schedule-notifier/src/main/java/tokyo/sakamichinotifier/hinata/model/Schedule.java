package tokyo.sakamichinotifier.hinata.model;

import com.google.cloud.spring.data.datastore.core.mapping.Entity;
import com.google.cloud.spring.data.datastore.core.mapping.Field;
import lombok.Getter;
import lombok.ToString;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.util.Objects;

@Entity(name = "hinata_schedule")
@ToString
public class Schedule {

	@Id
	@Field(name = "id")
	@Getter
	private String id;

	@Field(name = "title")
	private String title;

	@Field(name = "schedule_type")
	private ScheduleType scheduleType;

	@Field(name = "schedule_date")
	private LocalDate scheduleDate;

	@Field(name = "schedule_start_date")
	private LocalDate scheduleStartDate;

	protected Schedule(String id, String title, ScheduleType scheduleType, LocalDate scheduleDate,
			LocalDate scheduleStartDate) {
		this.id = id;
		this.title = title;
		this.scheduleType = scheduleType;
		this.scheduleDate = scheduleDate;
		this.scheduleStartDate = scheduleStartDate;
	}

	public static Schedule create(@NonNull String id, @NonNull String title, @NonNull ScheduleType scheduleType,
			@NonNull LocalDate scheduleDate) {
		return new Schedule(Objects.requireNonNull(id), Objects.requireNonNull(title),
				Objects.requireNonNull(scheduleType), Objects.requireNonNull(scheduleDate), null);
	}

}
