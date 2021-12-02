package tokyo.sakamichinotifier.hinata.infrastructure;

import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import tokyo.sakamichinotifier.hinata.domain.CloudStorageClient;
import tokyo.sakamichinotifier.hinata.domain.Schedule;

import java.util.List;

@Component
@Profile("test")
@Slf4j
public class CloudStorageClientTestImpl implements CloudStorageClient {

	public List<Schedule> fetchNewSchedules(@NonNull String bucketName, @NonNull String blobName) {
		log.debug("bucketName: {}, blobName: {}", bucketName, blobName);
		return null;
	}

}
