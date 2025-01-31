package gr.ihu.robotics.homesense.service.crud.impl;

import gr.ihu.robotics.homesense.domain.entity.SensorSample;
import gr.ihu.robotics.homesense.domain.repository.AbstractRepository;
import gr.ihu.robotics.homesense.domain.repository.SensorSampleRepository;
import gr.ihu.robotics.homesense.service.crud.SensorSampleCrudService;
import io.vavr.collection.List;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SensorSampleCrudServiceImpl implements SensorSampleCrudService {

    private final SensorSampleRepository sensorSampleRepository;

    @Override
    public AbstractRepository<SensorSample> getRepository() {
        return sensorSampleRepository;
    }

    @Override
    public Try<SensorSample> update(final UUID id, final SensorSample entity) {
        return Try.failure(new UnsupportedOperationException("Not supported"));
    }

    @Override
    public Try<Void> delete(final List<SensorSample> sensorSamples) {
        return Try.run(() -> sensorSampleRepository.deleteAll(sensorSamples));
    }
}
