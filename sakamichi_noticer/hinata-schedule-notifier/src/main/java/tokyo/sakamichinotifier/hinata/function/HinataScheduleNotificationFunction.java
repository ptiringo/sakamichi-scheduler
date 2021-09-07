package tokyo.sakamichinotifier.hinata.function;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tokyo.sakamichinotifier.hinata.application.HinataScheduleService;
import tokyo.sakamichinotifier.hinata.function.HinataScheduleNotificationFunction.CloudStorageObject;

import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
@Slf4j
public class HinataScheduleNotificationFunction implements Consumer<CloudStorageObject> {

	private final HinataScheduleService hinataScheduleService;

	@Override
	public void accept(CloudStorageObject object) {
		hinataScheduleService.readSchedules(object.getBucket(), object.getName())
				.forEach(hinataScheduleService::saveAndNotify);
	}

	@Data
	public static class CloudStorageObject {

		private String bucket;

		private String name;

	}

}
