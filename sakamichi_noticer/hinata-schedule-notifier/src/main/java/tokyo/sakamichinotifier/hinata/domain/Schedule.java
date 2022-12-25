package tokyo.sakamichinotifier.hinata.domain;

import com.google.cloud.spring.data.datastore.core.mapping.Entity;
import com.google.cloud.spring.data.datastore.core.mapping.Field;
import lombok.Getter;
import lombok.ToString;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
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

	/** 開始時刻 (UTC) */
	@Field(name = "start_time")
	private LocalDateTime startTime;

	/** 終了時刻 (UTC) */
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

	/**
	 * スケジュールの作成
	 * @param id ID
	 * @param title タイトル
	 * @param scheduleType スケジュール種別
	 * @param scheduleDate スケジュール日
	 * @param startTime 開始日時 (UTC)
	 * @param endTime 終了日時 (UTC)
	 * @return 作成されたスケジュール
	 */
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
