package gr.ihu.robotics.homesense.service.mapper.impl;

import gr.ihu.robotics.homesense.domain.entity.SensorSample;
import gr.ihu.robotics.homesense.domain.model.ChartJSModel;
import gr.ihu.robotics.homesense.service.mapper.ChartJsMapperService;
import io.vavr.collection.List;
import io.vavr.control.Try;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ChartJsMapperServiceImpl implements ChartJsMapperService {
    @Override
    public Try<ChartJSModel> fromSensorSamples(List<SensorSample> sensorSamples) {
        return Try.of(() -> {
            final ChartJSModel chartJSModel = new ChartJSModel(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
            for (SensorSample sensorSample : sensorSamples) {
                String createdAt = sensorSample.getCreatedAt().toString();
                String raw = sensorSample.getRaw();
                String processed = sensorSample.getProcessed();

                chartJSModel.getCreatedAt().add(createdAt);
                chartJSModel.getRaw().add(raw);
                chartJSModel.getProcessed().add(processed);
            }
            return chartJSModel;
        });
    }
}
