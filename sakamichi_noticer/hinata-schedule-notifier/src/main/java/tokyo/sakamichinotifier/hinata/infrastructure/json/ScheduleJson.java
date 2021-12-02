package tokyo.sakamichinotifier.hinata.infrastructure.json;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import org.jspecify.nullness.NullMarked;
import org.jspecify.nullness.Nullable;
import tokyo.sakamichinotifier.hinata.domain.Schedule;
import tokyo.sakamichinotifier.hinata.domain.ScheduleType;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.Optional;

/** スケジュールの元データの JSON のマッピング用クラス */
@Data
@NullMarked
public class ScheduleJson {

	protected static final ZoneId DEFAULT_TIMEZONE = ZoneId.of("Asia/Tokyo");

	private String scheduleId;

	private String title;

	@JsonDeserialize(using = ScheduleTypeDeserializer.class)
	private ScheduleType scheduleType;

	private LocalDate scheduleDate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime startTime;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	@Nullable
	private LocalDateTime endTime;

	/**
	 * Schedule ドメインオブジェクトへの変換
	 * @return 変換された Schedule オブジェクト
	 */
	public Schedule toSchedule() {
		return Schedule.create(scheduleId, title, scheduleType, scheduleDate,
				startTime.atZone(DEFAULT_TIMEZONE).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime(),
				Optional.ofNullable(endTime)
						.map(x -> x.atZone(DEFAULT_TIMEZONE).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime())
						.orElse(null));
	}

	/** schedule_type フィールドの変換用デシリアライザ */
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
