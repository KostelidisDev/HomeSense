package gr.ihu.robotics.homesense.service.crud;

import gr.ihu.robotics.homesense.domain.entity.SensorSample;
import io.vavr.collection.List;
import io.vavr.control.Try;

import java.util.UUID;

public interface SensorSampleCrudService extends AbstractCrudService<SensorSample> {
    @Override
    default Try<Void> deleteById(final UUID id) {
        return Try.failure(new UnsupportedOperationException());
    }

    @Override
    default Try<SensorSample> update(final UUID id, final SensorSample entity) {
        return Try.failure(new UnsupportedOperationException());
    }

    Try<Void> delete(List<SensorSample> sensorSamples);
}
