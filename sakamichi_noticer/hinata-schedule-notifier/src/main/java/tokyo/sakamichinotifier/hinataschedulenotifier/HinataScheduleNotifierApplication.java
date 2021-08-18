package tokyo.sakamichinotifier.hinataschedulenotifier;

import java.util.function.Consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import lombok.extern.java.Log;
import tokyo.sakamichinotifier.GcsEvent;

@SpringBootApplication
@Log
public class HinataScheduleNotifierApplication {

	public static void main(String[] args) {
		SpringApplication.run(HinataScheduleNotifierApplication.class, args);
	}

	@Bean
	public Consumer<GcsEvent> function() {
		return event -> {
			// log.info("Event: " + context.eventId());
			// log.info("Event Type: " + context.eventType());
			log.info("Bucket: " + event.getBucket());
			log.info("File: " + event.getName());
			log.info("Metageneration: " + event.getMetageneration());
			log.info("Created: " + event.getTimeCreated());
			log.info("Updated: " + event.getUpdated());
		};
	}

}
