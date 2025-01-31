package gr.ihu.robotics.homesense.service.scheduled;

import io.vavr.control.Try;

public interface SensorSamplesScheduledService {
    Try<Void> fetchSensorSamples();
}
