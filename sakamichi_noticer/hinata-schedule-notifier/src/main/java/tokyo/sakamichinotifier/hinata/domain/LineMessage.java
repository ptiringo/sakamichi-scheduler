package tokyo.sakamichinotifier.hinata.domain;

import lombok.Getter;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

public class LineMessage {

	@Getter
	private final String message;

	public LineMessage(@NonNull String message) {
		this.message = Objects.requireNonNull(message);
	}

}
