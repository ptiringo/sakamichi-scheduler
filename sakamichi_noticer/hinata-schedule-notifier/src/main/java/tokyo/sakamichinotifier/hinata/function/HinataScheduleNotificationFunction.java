package tokyo.sakamichinotifier.hinata.function;

import com.linecorp.bot.client.LineMessagingClient;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tokyo.sakamichinotifier.hinata.application.CloudStorageService;
import tokyo.sakamichinotifier.hinata.function.HinataScheduleNotificationFunction.CloudStorageObject;
import tokyo.sakamichinotifier.hinata.model.Schedule;
import tokyo.sakamichinotifier.hinata.model.ScheduleRepository;

import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
@Slf4j
public class HinataScheduleNotificationFunction implements Consumer<CloudStorageObject> {

	private final CloudStorageService cloudStorageService;

	private final ScheduleRepository scheduleRepository;

	@Override
	public void accept(CloudStorageObject object) {
		var schedules = cloudStorageService.readSchedules(object.getBucket(), object.getName());
		for (var schedule : schedules) {
			if (scheduleRepository.existsById(schedule.getId())) {
				var client = LineMessagingClient.builder("<channel access token>").build();
			}
			else {
				scheduleRepository.save(schedule);
				log.info("hinata schedule saved: {}", schedule);
			}
		}
	}

	@Data
	public static class CloudStorageObject {

		private String bucket;

		private String name;

	}

}
