package tokyo.sakamichinotifier.hinata.function;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tokyo.sakamichinotifier.hinata.application.CloudStorageService;
import tokyo.sakamichinotifier.hinata.function.HinataScheduleNotificationFunction.CloudStorageObject;

import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
@Slf4j
public class HinataScheduleNotificationFunction implements Consumer<CloudStorageObject> {

	private final CloudStorageService cloudStorageService;

	@Override
	public void accept(CloudStorageObject object) {
		var schedules = cloudStorageService.readSchedules(object.getBucket(), object.getName());
		schedules.forEach(s -> log.error(s.toString()));
	}

	@Data
	public static class CloudStorageObject {

		private String bucket;

		private String name;

	}

}
