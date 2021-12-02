package tokyo.sakamichinotifier.hinata.infrastructure.json;

import org.junit.jupiter.api.Test;
import tokyo.sakamichinotifier.hinata.domain.ScheduleType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;

class ScheduleJsonTest {

	@Test
	void testToSchedule() {
		var json = new ScheduleJson();
		json.setScheduleId("scheduleId");
		json.setTitle("title");
		json.setScheduleType(ScheduleType.TV);
		json.setScheduleDate(LocalDate.of(2021, 1, 1));

		var startTime = LocalDateTime.of(2021, 1, 1, 12, 30);
		var endTime = LocalDateTime.of(2021, 1, 1, 15, 30);
		json.setStartTime(startTime);
		json.setEndTime(endTime);

		var schedule = json.toSchedule();
		assertThat(schedule.getStartTime()).isPresent().get().isEqualTo(
				startTime.atZone(ScheduleJson.DEFAULT_TIMEZONE).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime());
		assertThat(schedule.getEndTime()).isPresent().get().isEqualTo(
				endTime.atZone(ScheduleJson.DEFAULT_TIMEZONE).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime());
	}

	@Test
	void testToScheduleWithoutEndTime() {
		var json = new ScheduleJson();
		json.setScheduleId("scheduleId");
		json.setTitle("title");
		json.setScheduleType(ScheduleType.TV);
		json.setScheduleDate(LocalDate.of(2021, 1, 1));

		var startTime = LocalDateTime.of(2021, 1, 1, 12, 30);
		json.setStartTime(startTime);

		var schedule = json.toSchedule();
		assertThat(schedule.getStartTime()).isPresent().get().isEqualTo(
				startTime.atZone(ScheduleJson.DEFAULT_TIMEZONE).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime());
		assertThat(schedule.getEndTime()).isEmpty();
	}

}