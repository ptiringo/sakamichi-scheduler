package tokyo.sakamichinotifier.hinata;

import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tokyo.sakamichinotifier.CloudStorageObject;

import java.io.IOException;
import java.nio.file.Files;
import java.util.function.Consumer;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
@Slf4j
public class HinataScheduleNotificationFunction implements Function<CloudStorageObject,String> {

    private Storage storage;

//    @Override
//    @SneakyThrows(IOException.class)
//    public void accept(CloudStorageObject object) {
//        var blob = storage.get(object.getBucket(), object.getName());
//        var localBlob = Files.createTempDirectory("hinata_schedule").resolve(object.getName());
//        blob.downloadTo(localBlob);
//        var list = Files.readAllLines(localBlob);
//        log.error(list.toString());
//    }

    @Override
    @SneakyThrows(IOException.class)
    public String apply(CloudStorageObject object) {
        var blob = storage.get(object.getBucket(), object.getName());
        var localBlob = Files.createTempDirectory("hinata_schedule").resolve(object.getName());
        blob.downloadTo(localBlob);
        var list = Files.readAllLines(localBlob);
        log.error(list.toString());
        return list.toString();
    }
}
