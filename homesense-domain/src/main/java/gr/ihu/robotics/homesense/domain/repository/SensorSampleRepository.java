package gr.ihu.robotics.homesense.domain.repository;

import gr.ihu.robotics.homesense.domain.entity.SensorSample;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface SensorSampleRepository extends AbstractRepository<SensorSample> {
    List<SensorSample> findBySensorId(UUID sensorId);

    List<SensorSample> findAllByCreatedAtAfterAndSensorIdOrderByCreatedAtAsc(LocalDateTime createdAt, UUID sensorId);
}
