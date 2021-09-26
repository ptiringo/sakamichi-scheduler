package tokyo.sakamichinotifier.hinata.domain;

import lombok.Value;

import java.util.List;

@Value
public class LineBotApiResponse {

	String requestId;

	String message;

	List<String> details;

}
