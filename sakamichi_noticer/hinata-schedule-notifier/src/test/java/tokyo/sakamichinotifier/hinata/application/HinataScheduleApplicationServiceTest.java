package tokyo.sakamichinotifier.hinata.application;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import tokyo.sakamichinotifier.hinata.domain.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class HinataScheduleApplicationServiceTest {

	@Autowired
	private HinataScheduleApplicationService applicationService;

	@MockBean
	private CloudStorageClient cloudStorageClient;

	@MockBean
	private ScheduleRepository scheduleRepository;

	@MockBean
	private LineClient lineClient;

	@Captor
	private ArgumentCaptor<LineMessage> captor;

	private static final DateTimeFormatter NOTIFICATION_TIME_FORMATTER = DateTimeFormatter.ofPattern("hh:mm");

	@Test
	void testFetchAndSaveNewSchedules() {
		var bucketName = "hinata-schedule";
		var blobName = "hinata_schedule_2021-08-20T21-22-04.592475.jl";

		var existingSchedule = Schedule.create("existing_id", "existing_schedule", ScheduleType.TV,
				LocalDate.of(2021, 1, 1), LocalDateTime.of(2021, 1, 1, 12, 0), LocalDateTime.of(2021, 1, 1, 15, 0));
		var nonExistingTvSchedule = Schedule.create("non_existing_tv_id", "non_existing_tv_schedule", ScheduleType.TV,
				LocalDate.of(2021, 1, 1), LocalDateTime.of(2021, 1, 1, 12, 0), LocalDateTime.of(2021, 1, 1, 15, 0));
		var nonExistingMagazineSchedule = Schedule.create("non_existing_magazine_id", "non_existing_magazine_schedule",
				ScheduleType.MAGAZINE, LocalDate.of(2021, 1, 1), null, null);
		var schedules = List.of(existingSchedule, nonExistingTvSchedule, nonExistingMagazineSchedule);

		when(cloudStorageClient.fetchNewSchedules(bucketName, blobName)).thenReturn(schedules);
		when(scheduleRepository.findById(existingSchedule.getId())).thenReturn(Optional.of(existingSchedule));
		when(scheduleRepository.save(nonExistingTvSchedule)).thenReturn(nonExistingTvSchedule);
		when(scheduleRepository.save(nonExistingMagazineSchedule)).thenReturn(nonExistingMagazineSchedule);

		applicationService.fetchAndSaveNewSchedules(bucketName, blobName);

		verify(scheduleRepository).save(existingSchedule);
		verify(scheduleRepository).save(nonExistingTvSchedule);
		verify(scheduleRepository).save(nonExistingMagazineSchedule);

//		verify(lineClient, times(1)).broadcast(captor.capture());
// assertThat(captor.getValue().getMessage()).contains(nonExistingTvSchedule.getTitle(),
// nonExistingTvSchedule.getStartTime().orElseThrow().format(NOTIFICATION_TIME_FORMATTER)
// + "〜"
// +
// nonExistingTvSchedule.getEndTime().orElseThrow().format(NOTIFICATION_TIME_FORMATTER));
	}

}