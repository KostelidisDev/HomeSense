package gr.ihu.robotics.homesense.service.rpc.impl;

import gr.ihu.robotics.homesense.domain.entity.Sensor;
import gr.ihu.robotics.homesense.domain.model.NodeSenseStatus;
import gr.ihu.robotics.homesense.service.config.NodeSenseServiceProperties;
import gr.ihu.robotics.homesense.service.crud.SensorCrudService;
import gr.ihu.robotics.homesense.service.rpc.NodeSenseRpcService;
import gr.ihu.robotics.homesense.service.rpc.SensorRpcService;
import gr.ihu.robotics.homesense.service.utils.NodeSenseUtils;
import io.vavr.Value;
import io.vavr.collection.List;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SensorRpcServiceImpl implements SensorRpcService {

    private final NodeSenseServiceProperties nodeSenseServiceProperties;
    private final NodeSenseRpcService nodeSenseRpcService;
    private final SensorCrudService sensorCrudService;

    @Override
    public Try<List<Sensor>> discoverSensors() {
        return Try.of(nodeSenseServiceProperties::getIpPrefix)
                .flatMap(ipPrefix -> Try.of(nodeSenseServiceProperties::getTimeout)
                        .flatMap(timeout -> nodeSenseRpcService.discover(ipPrefix, timeout)))
                .map(io.vavr.collection.List::ofAll)
                .flatMap(nodeSenses -> Try.sequence(
                        nodeSenses
                                .map(this::nodeSenseStatusToSensor)
                ))
                .map(Value::toList)
                .map(sensors -> sensors.filter(this::isNotPaired));
    }

    @Override
    public Try<Sensor> discoverSensor(final String ip) {
        return nodeSenseRpcService.getStatus(ip, nodeSenseServiceProperties.getTimeout())
                .flatMap(this::nodeSenseStatusToSensor);

    }

    private boolean isPaired(final Sensor sensor) {
        return Try.of(sensor::getIp)
                .flatMap(sensorCrudService::hasWithIp)
                .get();
    }

    private boolean isNotPaired(final Sensor sensor) {
        return !isPaired(sensor);
    }

    private Try<Sensor> nodeSenseStatusToSensor(final NodeSenseStatus nodeSenseStatus) {
        return Try.of(() -> {
            Sensor sensor = new Sensor();
            sensor.setName(nodeSenseStatus.getHostname());
            sensor.setIp(nodeSenseStatus.getIp());
            sensor.setType(NodeSenseUtils.sensorToSensorType(nodeSenseStatus.getSensor()));
            return sensor;
        });
    }
}
