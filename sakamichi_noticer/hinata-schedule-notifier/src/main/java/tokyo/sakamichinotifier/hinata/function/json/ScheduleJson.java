package tokyo.sakamichinotifier.hinata.function.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import tokyo.sakamichinotifier.hinata.model.Schedule;
import tokyo.sakamichinotifier.hinata.model.ScheduleType;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

@Data
public class ScheduleJson {

	private String scheduleId;

	private String title;

	@JsonDeserialize(using = ScheduleTypeDeserializer.class)
	private ScheduleType scheduleType;

	private LocalDate scheduleDate;

	private LocalDate scheduleStartDate;

	public Schedule toSchedule() {
		return new Schedule(scheduleId, title, scheduleType, scheduleDate);
	}

	public static class ScheduleTypeDeserializer extends JsonDeserializer<ScheduleType> {

		private static final Map<String, ScheduleType> SCHEDULE_TYPE_MAPPINGS = Map.of(//
				"テレビ", ScheduleType.TV, //
				"ラジオ", ScheduleType.RADIO, //
				"雑誌", ScheduleType.MAGAZINE, //
				"イベント", ScheduleType.EVENT, //
				"誕生日", ScheduleType.BIRTHDAY, //
				"握手会", ScheduleType.HANDSHAKE_MEETING, //
				"リリース", ScheduleType.RELEASE, //
				"LIVE", ScheduleType.LIVE, //
				"WEB", ScheduleType.WEB //
		);

		@Override
		public ScheduleType deserialize(JsonParser parser, DeserializationContext context) throws IOException {
			return SCHEDULE_TYPE_MAPPINGS.getOrDefault(parser.getValueAsString(), ScheduleType.OTHER);
		}

	}

}
