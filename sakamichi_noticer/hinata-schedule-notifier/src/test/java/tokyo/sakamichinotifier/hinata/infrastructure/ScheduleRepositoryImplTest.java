package tokyo.sakamichinotifier.hinata.infrastructure;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import tokyo.sakamichinotifier.hinata.domain.Schedule;
import tokyo.sakamichinotifier.hinata.domain.ScheduleRepository;
import tokyo.sakamichinotifier.hinata.domain.ScheduleType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = { //
		"spring.cloud.gcp.core.enabled=true", //
		"spring.cloud.gcp.datastore.enabled=true", //
		"spring.cloud.gcp.datastore.emulator.enabled=true" //
})
@ActiveProfiles("test")
class ScheduleRepositoryImplTest {

	@Autowired
	ScheduleRepository scheduleRepository;

	@Test
	void save() {
		var id = "id";
		var title = "title";
		var scheduleType = ScheduleType.TV;
		var scheduleDate = LocalDate.of(2021, 1, 1);
		var startTime = LocalDateTime.of(2021, 1, 1, 12, 30, 0);
		var endTime = LocalDateTime.of(2021, 1, 1, 13, 30, 0);
		var schedule = Schedule.create(id, title, scheduleType, scheduleDate, startTime, endTime);

		scheduleRepository.save(schedule);

		var savedSchedule = scheduleRepository.findById(id);
		assertThat(savedSchedule).get().satisfies(s -> {
			assertThat(s.getTitle()).isEqualTo(title);
			assertThat(s.getScheduleType()).isEqualTo(scheduleType);
			assertThat(s.getScheduleDate()).isEqualTo(scheduleDate);
			assertThat(s.getStartTime()).get().isEqualTo(startTime);
			assertThat(s.getEndTime()).get().isEqualTo(endTime);
		});
	}

}