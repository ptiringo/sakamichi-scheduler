package tokyo.sakamichinotifier.hinata.domain;

import java.util.List;

public interface CloudStorageClient {

	List<Schedule> fetchNewSchedules(String bucketName, String blobName);

}
