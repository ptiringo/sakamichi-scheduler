package tokyo.sakamichinotifier.hinata.function;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tokyo.sakamichinotifier.hinata.application.HinataScheduleApplicationService;
import tokyo.sakamichinotifier.hinata.function.HinataScheduleNotificationFunction.CloudStorageObject;

import java.util.function.Consumer;

/** スケジュール通知に使用する関数 */
@Component
@RequiredArgsConstructor
@Slf4j
public class HinataScheduleNotificationFunction implements Consumer<CloudStorageObject> {

	private final HinataScheduleApplicationService hinataScheduleApplicationService;

	@Override
	public void accept(CloudStorageObject object) {
		hinataScheduleApplicationService.fetchAndSaveNewSchedules(object.getBucket(), object.getName());
	}

	/** Cloud Storage ファイルのメタデータ */
	@Data
	public static class CloudStorageObject {

		private String bucket;

		private String name;

	}

}
