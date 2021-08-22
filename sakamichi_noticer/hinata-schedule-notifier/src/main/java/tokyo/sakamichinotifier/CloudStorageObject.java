package tokyo.sakamichinotifier;

import java.util.Date;

import lombok.Data;

@Data
public class CloudStorageObject {
    private String bucket;
    private String name;
}