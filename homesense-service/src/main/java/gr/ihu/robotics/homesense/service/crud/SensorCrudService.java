package gr.ihu.robotics.homesense.service.crud;

import gr.ihu.robotics.homesense.domain.entity.Sensor;
import io.vavr.control.Try;

public interface SensorCrudService extends AbstractCrudService<Sensor> {
    Try<Boolean> hasWithIp(String ip);
}
