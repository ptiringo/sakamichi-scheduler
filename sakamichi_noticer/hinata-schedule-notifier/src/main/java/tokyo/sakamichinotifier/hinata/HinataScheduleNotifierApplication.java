package tokyo.sakamichinotifier.hinata;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@LineMessageHandler
public class HinataScheduleNotifierApplication {

	public static void main(String[] args) {
		SpringApplication.run(HinataScheduleNotifierApplication.class, args);
	}

	@Bean
	public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
		return builder -> builder.modules(new JavaTimeModule()).timeZone("Asia/Tokyo").build();
	}

}
