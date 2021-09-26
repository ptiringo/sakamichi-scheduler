package tokyo.sakamichinotifier.hinata.domain;

import org.jspecify.nullness.NullMarked;

import java.util.concurrent.CompletableFuture;

@NullMarked
public interface LineClient {

	CompletableFuture<LineBotApiResponse> broadcast(LineMessage lineMessage);

}
