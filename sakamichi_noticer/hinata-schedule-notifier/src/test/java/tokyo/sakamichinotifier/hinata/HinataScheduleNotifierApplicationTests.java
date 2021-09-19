package tokyo.sakamichinotifier.hinata;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.cloud.function.context.FunctionCatalog;
import org.springframework.cloud.function.context.test.FunctionalSpringBootTest;
import org.springframework.http.HttpStatus;
import tokyo.sakamichinotifier.hinata.function.HinataScheduleNotificationFunction.CloudStorageObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@FunctionalSpringBootTest
class HinataScheduleNotifierApplicationTests {

	@Autowired
	private FunctionCatalog catalog;

	@Test
	void contextLoads() {
		var object = new CloudStorageObject();
		object.setBucket("hinata-schedule");
		object.setName("hinata_schedule_2021-08-20T21-22-04.592475.jl");
		Consumer function = catalog.lookup(Consumer.class, "hinataScheduleNotificationFunction");
		function.accept(object);
	}

}
