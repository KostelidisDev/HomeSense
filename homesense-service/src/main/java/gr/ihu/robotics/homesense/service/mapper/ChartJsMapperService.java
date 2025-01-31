package gr.ihu.robotics.homesense.service.mapper;

import gr.ihu.robotics.homesense.domain.entity.SensorSample;
import gr.ihu.robotics.homesense.domain.model.ChartJSModel;
import io.vavr.collection.List;
import io.vavr.control.Try;

public interface ChartJsMapperService {
    Try<ChartJSModel> fromSensorSamples(List<SensorSample> sensorSamples);
}
