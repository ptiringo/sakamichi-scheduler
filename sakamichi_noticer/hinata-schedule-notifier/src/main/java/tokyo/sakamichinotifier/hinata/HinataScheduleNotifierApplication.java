package tokyo.sakamichinotifier.hinata;

import com.google.cloud.storage.Storage;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import tokyo.sakamichinotifier.CloudStorageObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Function;

@SpringBootApplication
@Slf4j
public class HinataScheduleNotifierApplication {
    public static void main(String[] args) {
        SpringApplication.run(HinataScheduleNotifierApplication.class, args);
    }
}
