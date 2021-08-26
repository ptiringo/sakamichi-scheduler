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

		@Override
		public ScheduleType deserialize(JsonParser parser, DeserializationContext context) throws IOException {
			var value = parser.getValueAsString();
			switch (value) {
			case "テレビ":
				return ScheduleType.TV;
			case "ラジオ":
				return ScheduleType.RADIO;
			case "雑誌":
				return ScheduleType.MAGAZINE;
			default:
				return ScheduleType.UNKNOWN;
			}
		}

	}

}
