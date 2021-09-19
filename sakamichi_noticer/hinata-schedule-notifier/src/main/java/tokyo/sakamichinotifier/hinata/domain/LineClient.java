package tokyo.sakamichinotifier.hinata.domain;

import org.checkerframework.checker.nullness.qual.NonNull;

public interface LineClient {

	void broadcast(@NonNull LineMessage lineMessage);

}
