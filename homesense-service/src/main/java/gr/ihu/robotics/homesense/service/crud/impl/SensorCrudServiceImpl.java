package gr.ihu.robotics.homesense.service.crud.impl;

import gr.ihu.robotics.homesense.domain.entity.AbstractEntity;
import gr.ihu.robotics.homesense.domain.entity.Sensor;
import gr.ihu.robotics.homesense.domain.repository.AbstractRepository;
import gr.ihu.robotics.homesense.domain.repository.SensorRepository;
import gr.ihu.robotics.homesense.service.crud.SensorCrudService;
import gr.ihu.robotics.homesense.service.crud.SensorSampleCrudService;
import gr.ihu.robotics.homesense.service.rpc.SensorSampleRpcService;
import io.vavr.Value;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SensorCrudServiceImpl implements SensorCrudService {

    private final SensorRepository repository;
    private final SensorSampleCrudService sensorSampleCrudService;
    private final SensorSampleRpcService sensorSampleRpcService;

    @Override
    public AbstractRepository<Sensor> getRepository() {
        return repository;
    }

    @Override
    public Try<Boolean> hasWithIp(final String ip) {
        return Try.of(() -> repository)
                .map(_repository -> _repository.hasSensorWithIp(ip));
    }

    @Override
    public Try<Void> deleteById(final UUID id) {
        return findById(id)
                .flatMap(Value::toTry)
                .map(AbstractEntity::getId)
                .flatMap(sensorSampleRpcService::getBySensorId)
                .map(sensorSampleCrudService::delete)
                .flatMap(unused -> SensorCrudService.super.deleteById(id));
    }
}
