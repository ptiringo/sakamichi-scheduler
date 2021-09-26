package tokyo.sakamichinotifier.hinata.domain;

import lombok.Getter;
import org.jspecify.nullness.NullMarked;

import java.util.Objects;

@NullMarked
public class LineMessage {

	@Getter
	private final String message;

	public LineMessage(String message) {
		this.message = Objects.requireNonNull(message);
	}

}
