package tokyo.sakamichinotifier.hinata.domain;

import java.util.concurrent.CompletableFuture;

public interface LineClient {

	CompletableFuture<LineBotApiResponse> broadcast(LineMessage lineMessage);

}
