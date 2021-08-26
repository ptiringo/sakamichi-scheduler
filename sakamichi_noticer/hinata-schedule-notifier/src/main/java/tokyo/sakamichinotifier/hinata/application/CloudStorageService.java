package tokyo.sakamichinotifier.hinata.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tokyo.sakamichinotifier.hinata.function.json.ScheduleJson;
import tokyo.sakamichinotifier.hinata.model.Schedule;
import tokyo.sakamichinotifier.hinata.model.ScheduleRepository;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Slf4j
public class CloudStorageService {

	private final Storage storage;

	private final ObjectMapper mapper;

	private final ScheduleRepository scheduleRepository;

	public List<Schedule> readSchedules(String bucketName, String blobName) {
		var blob = storage.get(bucketName, blobName);
		var lines = new String(blob.getContent()).split("\n");
		return Arrays.stream(lines).map(line -> {
			try {
				return mapper.readValue(line, ScheduleJson.class).toSchedule();
			}
			catch (JsonProcessingException e) {
				throw new RuntimeException(e);
			}
		}).collect(toList());
	}

	public void saveAllSchedules(List<Schedule> schedules) {
		scheduleRepository.saveAll(schedules);
	}

}
