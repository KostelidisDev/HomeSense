package gr.ihu.robotics.homesense.service.scheduled;

import com.google.gson.Gson;
import gr.ihu.robotics.homesense.domain.entity.Sensor;
import gr.ihu.robotics.homesense.domain.entity.SensorSample;
import gr.ihu.robotics.homesense.domain.model.NodeSenseData;
import gr.ihu.robotics.homesense.service.config.NodeSenseServiceProperties;
import gr.ihu.robotics.homesense.service.crud.SensorCrudService;
import gr.ihu.robotics.homesense.service.crud.SensorSampleCrudService;
import gr.ihu.robotics.homesense.service.rpc.NodeSenseRpcService;
import io.vavr.Value;
import io.vavr.collection.List;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class SensorSamplesScheduledServiceImpl implements SensorSamplesScheduledService {

    private final SensorCrudService sensorCrudService;
    private final SensorSampleCrudService sensorSampleCrudService;
    private final NodeSenseRpcService nodeSenseRpcService;
    private final NodeSenseServiceProperties nodeSenseServiceProperties;

    @Override
    @Scheduled(fixedRateString = "${nodesense-service.scheduled-interval}", initialDelayString = "${nodesense-service.scheduled-delay}")
    public Try<Void> fetchSensorSamples() {
        return Try.run(() -> {
            List<SensorSample> sensorSamples = sensorCrudService.findAll()
                    .flatMap(this::processSensors)
                    .flatMap(sensorSampleCrudService::create)
                    .onFailure(Throwable::printStackTrace)
                    .get();
            System.out.println("Sensor samples fetched: " + sensorSamples.size());
        });
    }

    private Try<List<SensorSample>> processSensors(final List<Sensor> sensors) {
        return Try.sequence(sensors.map(this::processSensor)).map(Value::toList);
    }

    private Try<SensorSample> processSensor(final Sensor sensor) {
        return Try.of(sensor::getIp)
                .flatMap(ip -> nodeSenseRpcService.getData(ip, nodeSenseServiceProperties.getScheduledTimeout()))
                .flatMap(nodeSenseData -> nodeSenseDataToSensorSample(nodeSenseData, sensor));
    }

    private Try<SensorSample> nodeSenseDataToSensorSample(final NodeSenseData nodeSenseData,
                                                          final Sensor sensor) {
        return Try.of(() -> {
            SensorSample sensorSample = new SensorSample();
            sensorSample.setSensor(sensor);
            sensorSample.setRaw(nodeSenseData.getRaw());
            sensorSample.setProcessed(mapToString(nodeSenseData.getProcessed()));
            return sensorSample;
        });
    }

    private String mapToString(final Map<String, String> processed) {
        return new Gson().toJson(processed);
    }
}
