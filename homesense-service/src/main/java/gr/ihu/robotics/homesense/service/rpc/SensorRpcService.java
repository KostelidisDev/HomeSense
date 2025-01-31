package gr.ihu.robotics.homesense.service.rpc;

import gr.ihu.robotics.homesense.domain.entity.Sensor;
import io.vavr.collection.List;
import io.vavr.control.Try;

public interface SensorRpcService {
    Try<List<Sensor>> discoverSensors();

    Try<Sensor> discoverSensor(String ip);
}
