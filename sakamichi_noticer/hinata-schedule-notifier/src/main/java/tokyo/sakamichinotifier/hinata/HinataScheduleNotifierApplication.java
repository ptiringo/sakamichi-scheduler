package tokyo.sakamichinotifier.hinata;

import lombok.extern.java.Log;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Log
public class HinataScheduleNotifierApplication {
    public static void main(String[] args) {
        SpringApplication.run(HinataScheduleNotifierApplication.class, args);
    }
}
