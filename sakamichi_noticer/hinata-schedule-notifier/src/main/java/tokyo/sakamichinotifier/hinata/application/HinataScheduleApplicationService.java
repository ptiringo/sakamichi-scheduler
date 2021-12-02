package tokyo.sakamichinotifier.hinata.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.nullness.NullMarked;
import org.jspecify.nullness.Nullable;
import org.springframework.stereotype.Service;
import tokyo.sakamichinotifier.hinata.domain.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import static tokyo.sakamichinotifier.hinata.domain.ScheduleType.TV;

/** アプリケーションサービス */
@Service
@RequiredArgsConstructor
@Slf4j
@NullMarked
public class HinataScheduleApplicationService {

	/** 通知に使用する日付のフォーマッター */
	private static final DateTimeFormatter NOTIFICATION_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy年MM月dd日");

	/** 通知に使用する時間のフォーマッター */
	private static final DateTimeFormatter NOTIFICATION_TIME_FORMATTER = DateTimeFormatter.ofPattern("hh:mm");

	private static final ZoneId DEFAULT_TIMEZONE = ZoneId.of("Asia/Tokyo");

	private final CloudStorageClient cloudStorageClient;

	private final ScheduleRepository scheduleRepository;

	private final LineClient lineClient;

	/**
	 * Cloud Storage のオブジェクトからスケジュールを読み込みます。<br>
	 * スケジュールの保存及び通知を行います。
	 * @param bucketName バケット名
	 * @param blobName オブジェクトのパス
	 */
	public void fetchAndSaveNewSchedules(String bucketName, String blobName) {
		var lineResponseFutures = new ArrayList<CompletableFuture<LineBotApiResponse>>();

		cloudStorageClient.fetchNewSchedules(bucketName, blobName)
				.forEach(schedule -> scheduleRepository.findById(schedule.getId()).ifPresentOrElse( //
						existingSchedule -> updateExistingSchedule(existingSchedule, //
								schedule.getTitle(), //
								schedule.getScheduleType(), //
								schedule.getScheduleDate(), //
								schedule.getStartTime()
										.map(x -> x.atZone(DEFAULT_TIMEZONE).withZoneSameLocal(ZoneId.of("UTC"))
												.toLocalDateTime())
										.orElse(null), //
								schedule.getEndTime().map(x -> x.atZone(DEFAULT_TIMEZONE).toLocalDateTime())
										.orElse(null)), //
						() -> {
							var savedSchedule = saveNewSchedule(schedule);
							if (savedSchedule.getScheduleType() == TV) {
								var lineResponseFuture = pushNotificationMessage(savedSchedule);
								lineResponseFutures.add(lineResponseFuture);
							}
						}));

		if (!lineResponseFutures.isEmpty()) {
			CompletableFuture.allOf(lineResponseFutures.toArray(new CompletableFuture[0]))
					.whenComplete((response, ex) -> {
						if (ex == null) {
							log.info("LINE broadcast succeeded.");
						}
						else {
							log.error("LINE broadcast failed.", ex);
						}
					});
		}
	}

	private void updateExistingSchedule(Schedule schedule, String newTitle, ScheduleType newScheduleType,
			@Nullable LocalDate newScheduleDate, @Nullable LocalDateTime startTime, @Nullable LocalDateTime endTime) {
		log.debug("schedule ({}) found", schedule.getId());
		schedule.update(newTitle, newScheduleType, newScheduleDate, startTime, endTime);
		var savedSchedule = scheduleRepository.save(schedule);
		log.info("hinata schedule updated: {}", savedSchedule);
	}

	private Schedule saveNewSchedule(Schedule schedule) {
		log.debug("schedule ({}) not found", schedule.getId());
		var savedSchedule = scheduleRepository.save(schedule);
		log.info("hinata schedule saved: {}", savedSchedule);
		return savedSchedule;
	}

	private CompletableFuture<LineBotApiResponse> pushNotificationMessage(Schedule savedSchedule) {
		var message = "＜日向坂46☀️＞" + "\n" //
				+ savedSchedule.getTitle() + "\n" //
				+ savedSchedule.getScheduleDate().format(NOTIFICATION_DATE_FORMATTER);

		if (savedSchedule.hasTime()) {
			message += "\n" //
					+ savedSchedule.getStartTime().orElseThrow().format(NOTIFICATION_TIME_FORMATTER) //
					+ "〜" //
					+ savedSchedule.getEndTime().map(time -> time.format(NOTIFICATION_TIME_FORMATTER)).orElse("");
		}

		return lineClient.broadcast(new LineMessage(message));
	}

}
