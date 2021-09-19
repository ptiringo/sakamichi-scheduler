package tokyo.sakamichinotifier.hinata.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.stereotype.Service;
import tokyo.sakamichinotifier.hinata.domain.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static tokyo.sakamichinotifier.hinata.domain.ScheduleType.TV;

/** アプリケーションサービス */
@Service
@RequiredArgsConstructor
@Slf4j
public class HinataScheduleApplicationService {

	/** 通知に使用する日付のフォーマッター */
	private static final DateTimeFormatter NOTIFICATION_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy年MM月dd日");

	/** 通知に使用する時間のフォーマッター */
	private static final DateTimeFormatter NOTIFICATION_TIME_FORMATTER = DateTimeFormatter.ofPattern("hh:mm");

	private final CloudStorageClient cloudStorageClient;

	private final ScheduleRepository scheduleRepository;

	private final LineClient lineClient;

	/**
	 * Cloud Storage のオブジェクトからスケジュールを読み込みます。<br>
	 * スケジュールの保存及び通知を行います。
	 * @param bucketName バケット名
	 * @param blobName オブジェクトのパス
	 */
	public void fetchAndSaveNewSchedules(@NonNull String bucketName, @NonNull String blobName) {
		cloudStorageClient.fetchNewSchedules(bucketName, blobName)
				.forEach(schedule -> scheduleRepository.findById(schedule.getId()).ifPresentOrElse( //
						existingSchedule -> updateExistingSchedule(schedule, //
								existingSchedule.getTitle(), //
								existingSchedule.getScheduleType(), //
								existingSchedule.getScheduleDate()), //
						() -> {
							var savedSchedule = saveNewSchedule(schedule);
							if (savedSchedule.getScheduleType() == TV) {
								pushNotificationMessage(savedSchedule);
							}
						}));
	}

	private void updateExistingSchedule(@NonNull Schedule schedule, @NonNull String newTitle,
			@NonNull ScheduleType newScheduleType, LocalDate newScheduleDate) {
		schedule.update(newTitle, newScheduleType, newScheduleDate);
		var savedSchedule = scheduleRepository.save(schedule);
		log.info("hinata schedule updated: {}", savedSchedule);
	}

	private Schedule saveNewSchedule(@NonNull Schedule schedule) {
		var savedSchedule = scheduleRepository.save(schedule);
		log.info("hinata schedule saved: {}", savedSchedule);
		return savedSchedule;
	}

	private void pushNotificationMessage(@NonNull Schedule savedSchedule) {
		var message = "＜日向坂46☀️＞" + "\n" //
				+ savedSchedule.getTitle() + "\n" //
				+ savedSchedule.getScheduleDate().format(NOTIFICATION_DATE_FORMATTER);

		if (savedSchedule.hasTime()) {
			message += "\n" //
					+ savedSchedule.getStartTime().orElseThrow().format(NOTIFICATION_TIME_FORMATTER) //
					+ "〜" //
					+ savedSchedule.getEndTime().map(time -> time.format(NOTIFICATION_TIME_FORMATTER)).orElse("");
		}

		lineClient.broadcast(new LineMessage(message));
	}

}
