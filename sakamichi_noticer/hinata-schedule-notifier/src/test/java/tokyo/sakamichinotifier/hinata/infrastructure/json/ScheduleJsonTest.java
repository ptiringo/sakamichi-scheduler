package tokyo.sakamichinotifier.hinata.infrastructure.json;

import org.junit.jupiter.api.Test;
import tokyo.sakamichinotifier.hinata.domain.ScheduleType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;

class ScheduleJsonTest {

	@Test
	void testToSchedule() {
		var json = new ScheduleJson();
		json.setScheduleId("scheduleId");
		json.setTitle("title");
		json.setScheduleType(ScheduleType.TV);
		json.setScheduleDate(LocalDate.of(2021, 1, 1));
		json.setStartTime(LocalDateTime.of(2021, 1, 1, 12, 30));
		json.setEndTime(LocalDateTime.of(2021, 1, 1, 15, 30));

		var schedule = json.toSchedule();
		assertThat(schedule.getStartTime())
				.hasValueSatisfying(startTime -> assertThat(startTime.getZone()).isEqualTo(ZoneId.of("Asia/Tokyo")));
		assertThat(schedule.getEndTime())
				.hasValueSatisfying(endTime -> assertThat(endTime.getZone()).isEqualTo(ZoneId.of("Asia/Tokyo")));
	}

	@Test
	void testToScheduleWithoutEndTime() {
		var json = new ScheduleJson();
		json.setScheduleId("scheduleId");
		json.setTitle("title");
		json.setScheduleType(ScheduleType.TV);
		json.setScheduleDate(LocalDate.of(2021, 1, 1));
		json.setStartTime(LocalDateTime.of(2021, 1, 1, 12, 30));

		var schedule = json.toSchedule();
		assertThat(schedule.getStartTime())
				.hasValueSatisfying(startTime -> assertThat(startTime.getZone()).isEqualTo(ZoneId.of("Asia/Tokyo")));
		assertThat(schedule.getEndTime()).isEmpty();
	}

}