package tokyo.sakamichinotifier.hinata.infrastructure;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.Broadcast;
import com.linecorp.bot.model.message.TextMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.stereotype.Component;
import tokyo.sakamichinotifier.hinata.domain.LineClient;
import tokyo.sakamichinotifier.hinata.domain.LineMessage;

@Component
@RequiredArgsConstructor
@Slf4j
public class LineClientImpl implements LineClient {

	private final LineMessagingClient lineMessagingClient;

	@Override
	public void broadcast(@NonNull LineMessage lineMessage) {
		var message = new TextMessage(lineMessage.getMessage());
		lineMessagingClient.broadcast(new Broadcast(message)).whenComplete((response, ex) -> {
			if (ex == null) {
				log.debug("LINE broadcast succeeded. response: {}", response);
			}
			else {
				log.debug("LINE broadcast failed.", ex);
			}
		});
	}

}
