package tokyo.sakamichinotifier.hinata.infrastructure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import tokyo.sakamichinotifier.hinata.application.exception.HinataApplicationRuntimeException;
import tokyo.sakamichinotifier.hinata.domain.CloudStorageClient;
import tokyo.sakamichinotifier.hinata.domain.Schedule;
import tokyo.sakamichinotifier.hinata.infrastructure.json.ScheduleJson;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
@RequiredArgsConstructor
@Profile("!test")
public class CloudStorageClientImpl implements CloudStorageClient {

	private final Storage storage;

	private final ObjectMapper mapper;

	/**
	 * Cloud Storage のオブジェクトからスケジュールを読み込みます。
	 * @param bucketName バケット名
	 * @param blobName オブジェクトのパス
	 * @return 読み込んだスケジュール
	 */
	public List<Schedule> fetchNewSchedules(@NonNull String bucketName, @NonNull String blobName) {
		var blob = storage.get(bucketName, blobName);
		var lines = new String(blob.getContent()).split("\n");
		return Arrays.stream(lines).map(line -> {
			try {
				return mapper.readValue(line, ScheduleJson.class).toSchedule();
			}
			catch (JsonProcessingException e) {
				throw new HinataApplicationRuntimeException(e);
			}
		}).collect(toList());
	}

}
