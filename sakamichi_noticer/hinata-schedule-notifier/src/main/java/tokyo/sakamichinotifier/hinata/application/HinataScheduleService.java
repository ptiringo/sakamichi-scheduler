package tokyo.sakamichinotifier.hinata.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.storage.Storage;
import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.Broadcast;
import com.linecorp.bot.model.message.TextMessage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.stereotype.Service;
import tokyo.sakamichinotifier.hinata.function.json.ScheduleJson;
import tokyo.sakamichinotifier.hinata.model.Schedule;
import tokyo.sakamichinotifier.hinata.model.ScheduleRepository;
import tokyo.sakamichinotifier.hinata.model.ScheduleType;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static java.util.stream.Collectors.toList;
import static tokyo.sakamichinotifier.hinata.model.ScheduleType.TV;

@Service
@RequiredArgsConstructor
@Slf4j
public class HinataScheduleService {

	/** 通知に使用する日付のフォーマッター */
	private static final DateTimeFormatter NOTIFICATION_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy年MM月dd日");

	/** 通知に使用する時間のフォーマッター */
	private static final DateTimeFormatter NOTIFICATION_TIME_FORMATTER = DateTimeFormatter.ofPattern("hh:mm");

	private final Storage storage;

	private final ObjectMapper mapper;

	private final ScheduleRepository scheduleRepository;

	private final LineMessagingClient lineMessagingClient;

	/**
	 * Cloud Storage のオブジェクトからスケジュールを読み込みます。
	 * @param bucketName バケット名
	 * @param blobName オブジェクトのパス
	 * @return 読み込んだスケジュール
	 */
	public List<Schedule> readSchedules(@NonNull String bucketName, @NonNull String blobName) {
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

	/**
	 * スケジュールの保存及び通知を行います。
	 * @param schedule 保存・通知対象のスケジュール
	 */
	public void saveAndNotify(@NonNull Schedule schedule) {
		scheduleRepository.findById(schedule.getId()).ifPresentOrElse( //
				existingSchedule -> updateExistingSchedule(schedule, //
						existingSchedule.getTitle(), //
						existingSchedule.getScheduleType(), //
						existingSchedule.getScheduleDate()), //
				() -> {
					var savedSchedule = saveNewSchedule(schedule);
					if (savedSchedule.getScheduleType() == TV) {
						pushNotificationMessage(savedSchedule);
					}
				});
	}

	private Schedule saveNewSchedule(@NonNull Schedule schedule) {
		var savedSchedule = scheduleRepository.save(schedule);
		log.info("hinata schedule saved: {}", savedSchedule);
		return savedSchedule;
	}

	private void updateExistingSchedule(@NonNull Schedule schedule, @NonNull String newTitle,
			@NonNull ScheduleType newScheduleType, LocalDate newScheduleDate) {
		schedule.update(newTitle, newScheduleType, newScheduleDate);
		var savedSchedule = scheduleRepository.save(schedule);
		log.info("hinata schedule updated: {}", savedSchedule);
	}

	@SneakyThrows({ InterruptedException.class, ExecutionException.class })
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

		var broadcastMessage = new Broadcast(new TextMessage(message));
		log.info("LINE message: {}", broadcastMessage);

		var response = lineMessagingClient.broadcast(broadcastMessage).get();
		log.info("LINE push message response: {}", response);
	}

}
