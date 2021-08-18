package tokyo.sakamichinotifier;

import java.util.Date;

import lombok.Data;

@Data
public class GcsEvent {
    private String bucket;
    private String name;
    private String metageneration;
    private Date timeCreated;
    private Date updated;
}