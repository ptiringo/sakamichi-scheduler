package tokyo.sakamichinotifier.hinata.function;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.Broadcast;
import com.linecorp.bot.model.message.TextMessage;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import tokyo.sakamichinotifier.hinata.application.CloudStorageService;
import tokyo.sakamichinotifier.hinata.function.HinataScheduleNotificationFunction.CloudStorageObject;
import tokyo.sakamichinotifier.hinata.model.Schedule;
import tokyo.sakamichinotifier.hinata.model.ScheduleRepository;
import tokyo.sakamichinotifier.hinata.model.ScheduleType;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import static tokyo.sakamichinotifier.hinata.model.ScheduleType.TV;

@Component
@RequiredArgsConstructor
@Slf4j
public class HinataScheduleNotificationFunction implements Consumer<CloudStorageObject> {

	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy年MM月dd日");

	private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("hh:mm");

	private final CloudStorageService cloudStorageService;

	private final ScheduleRepository scheduleRepository;

	private final LineMessagingClient lineMessagingClient;

	@Override
	public void accept(CloudStorageObject object) {
		cloudStorageService.readSchedules(object.getBucket(), object.getName())
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
	private void pushNotificationMessage(Schedule savedSchedule) {
		var message = "＜日向坂46☀️＞" + "\n" //
				+ savedSchedule.getTitle() + "\n" //
				+ savedSchedule.getScheduleDate().format(DATE_FORMATTER);

		if (savedSchedule.hasTime()) {
			message += "\n" //
					+ savedSchedule.getStartTime().orElseThrow().format(TIME_FORMATTER) //
					+ "〜" //
					+ savedSchedule.getEndTime().map(time -> time.format(TIME_FORMATTER)).orElse("");
		}

		var broadcastMessage = new Broadcast(new TextMessage(message));
		log.info("LINE message: {}", broadcastMessage);

		var response = lineMessagingClient.broadcast(broadcastMessage).get();
		log.info("LINE push message response: {}", response);
	}

	@Data
	public static class CloudStorageObject {

		private String bucket;

		private String name;

	}

}
