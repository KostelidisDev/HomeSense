package gr.ihu.robotics.homesense.service.rpc;

import gr.ihu.robotics.homesense.domain.entity.SensorSample;
import io.vavr.collection.List;
import io.vavr.control.Try;

import java.util.UUID;

public interface SensorSampleRpcService {
    Try<List<SensorSample>> getBySensorId(UUID sensorId, Boolean interpolate);

    default Try<List<SensorSample>> getBySensorId(UUID sensorId) {
        return getBySensorId(sensorId, false);
    }

    Try<List<SensorSample>> getInLastMinutesBySensorId(UUID sensorId, Long minutes, Boolean interpolate);
}
