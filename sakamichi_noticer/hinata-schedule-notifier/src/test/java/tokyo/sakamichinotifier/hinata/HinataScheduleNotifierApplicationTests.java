package tokyo.sakamichinotifier.hinata;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cloud.function.context.FunctionCatalog;
import org.springframework.cloud.function.context.test.FunctionalSpringBootTest;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import tokyo.sakamichinotifier.CloudStorageObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.function.Consumer;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@FunctionalSpringBootTest(webEnvironment = RANDOM_PORT)
class HinataScheduleNotifierApplicationTests {

    @Autowired
    private TestRestTemplate rest;

    @Test
    void contextLoads() throws URISyntaxException {
        var object = new CloudStorageObject();
        object.setBucket("hinata-schedule");
        object.setName("hinata_schedule_2021-08-20T21-22-04.592475.jl");
        var response = this.rest.postForEntity(new URI("/"), object, String.class);
        System.out.println(response);
    }

}
