package tokyo.sakamichinotifier.hinata.infrastructure;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.Broadcast;
import com.linecorp.bot.model.message.TextMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.stereotype.Component;
import tokyo.sakamichinotifier.hinata.domain.LineBotApiResponse;
import tokyo.sakamichinotifier.hinata.domain.LineClient;
import tokyo.sakamichinotifier.hinata.domain.LineMessage;

import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
@Slf4j
public class LineClientImpl implements LineClient {

	private final LineMessagingClient lineMessagingClient;

	@Override
	public CompletableFuture<LineBotApiResponse> broadcast(@NonNull LineMessage lineMessage) {
		var message = new TextMessage(lineMessage.getMessage());
		return lineMessagingClient.broadcast(new Broadcast(message))
				.thenApply(response -> new LineBotApiResponse(response.getRequestId(), response.getMessage(),
						response.getDetails()));
	}

}
