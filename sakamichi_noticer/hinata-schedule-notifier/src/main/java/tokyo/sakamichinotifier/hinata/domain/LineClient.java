package tokyo.sakamichinotifier.hinata.domain;

import org.jspecify.nullness.NullMarked;

@NullMarked
public interface LineClient {

	void broadcast(LineMessage lineMessage);

}
