package tokyo.sakamichinotifier.hinata.function;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.response.BotApiResponse;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import tokyo.sakamichinotifier.hinata.application.CloudStorageService;
import tokyo.sakamichinotifier.hinata.function.HinataScheduleNotificationFunction.CloudStorageObject;
import tokyo.sakamichinotifier.hinata.model.Schedule;
import tokyo.sakamichinotifier.hinata.model.ScheduleRepository;

import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
@Slf4j
public class HinataScheduleNotificationFunction implements Consumer<CloudStorageObject> {

	private final CloudStorageService cloudStorageService;

	private final ScheduleRepository scheduleRepository;

	private final LineMessagingClient lineMessagingClient;

	@Value("${my.application.line.target-notification-target-user-id}")
	private String notificationTargetLineUserId;

	@Override
	@SneakyThrows({ InterruptedException.class, ExecutionException.class })
	public void accept(CloudStorageObject object) {
		var schedules = cloudStorageService.readSchedules(object.getBucket(), object.getName());
		var response = lineMessagingClient
				.pushMessage(new PushMessage(notificationTargetLineUserId, new TextMessage("Hello, world!"))).get();
		log.error("Sent message: {}", response);
		for (var schedule : schedules) {
			if (scheduleRepository.existsById(schedule.getId())) {
				// try {
				// }
				// catch (InterruptedException | ExecutionException e) {
				// throw new RuntimeException(e);
				// }
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
