package tokyo.sakamichinotifier.hinata.model;

import com.google.cloud.spring.data.datastore.repository.DatastoreRepository;

public interface ScheduleRepository extends DatastoreRepository<Schedule, String> {

}
