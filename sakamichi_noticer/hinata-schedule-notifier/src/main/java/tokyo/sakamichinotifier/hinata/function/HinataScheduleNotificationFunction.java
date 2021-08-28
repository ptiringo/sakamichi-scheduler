package tokyo.sakamichinotifier.hinata.function;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.storage.Storage;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tokyo.sakamichinotifier.hinata.application.CloudStorageService;
import tokyo.sakamichinotifier.hinata.function.HinataScheduleNotificationFunction.CloudStorageObject;
import tokyo.sakamichinotifier.hinata.function.json.ScheduleJson;

import java.io.IOException;
import java.nio.file.Files;
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
