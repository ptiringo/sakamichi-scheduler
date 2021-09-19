package tokyo.sakamichinotifier.hinata.domain;

import com.google.cloud.spring.data.datastore.repository.DatastoreRepository;

public interface ScheduleRepository extends DatastoreRepository<Schedule, String> {

}
