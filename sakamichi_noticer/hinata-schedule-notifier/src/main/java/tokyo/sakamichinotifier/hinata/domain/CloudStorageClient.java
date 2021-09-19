package tokyo.sakamichinotifier.hinata.domain;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

public interface CloudStorageClient {

	List<Schedule> fetchNewSchedules(@NonNull String bucketName, @NonNull String blobName);

}
