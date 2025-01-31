package gr.ihu.robotics.homesense.domain.repository;

import gr.ihu.robotics.homesense.domain.entity.Sensor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorRepository extends AbstractRepository<Sensor> {
    @Query("SELECT COUNT(s.ip) > 0 FROM Sensor s WHERE s.ip = ?1")
    Boolean hasSensorWithIp(String ip);
}
