package tokyo.sakamichinotifier.hinata.domain;

import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
public interface CloudStorageClient {

	List<Schedule> fetchNewSchedules(String bucketName, String blobName);

}
