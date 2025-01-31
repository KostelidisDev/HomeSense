package gr.ihu.robotics.homesense.service.rpc.impl;

import com.google.gson.Gson;
import gr.ihu.robotics.homesense.domain.entity.Sensor;
import gr.ihu.robotics.homesense.domain.entity.SensorSample;
import gr.ihu.robotics.homesense.domain.model.NodeSenseProcessedModel;
import gr.ihu.robotics.homesense.domain.repository.SensorSampleRepository;
import gr.ihu.robotics.homesense.service.rpc.SensorSampleRpcService;
import io.vavr.collection.List;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SensorSampleRpcServiceImpl implements SensorSampleRpcService {

    private final SensorSampleRepository sensorSampleRepository;

    @Override
    public Try<List<SensorSample>> getBySensorId(final UUID sensorId, final Boolean interpolate) {
        return Try.of(() -> sensorSampleRepository.findBySensorId(sensorId))
                .map(sensorSamples -> (interpolate)
                        ? interpolateSensorSample(sensorSamples)
                        : sensorSamples)
                .map(List::ofAll);
    }

    @Override
    public Try<List<SensorSample>> getInLastMinutesBySensorId(final UUID sensorId, final Long minutes, final Boolean interpolate) {
        return calculateTimestampBeforeMinutes(minutes)
                .map(threshold -> sensorSampleRepository.findAllByCreatedAtAfterAndSensorIdOrderByCreatedAtAsc(threshold, sensorId))
                .map(sensorSamples -> (interpolate)
                        ? interpolateSensorSample(sensorSamples)
                        : sensorSamples)
                .map(List::ofAll);
    }

    private Try<LocalDateTime> calculateTimestampBeforeMinutes(final Long minutes) {
        return Try.of(LocalDateTime::now)
                .map(now -> now.minusMinutes(minutes));
    }

    private java.util.List<SensorSample> interpolateSensorSample(final java.util.List<SensorSample> sensorSamples) {
        return interpolateSensorSample(sensorSamples, 100);
    }

    private java.util.List<SensorSample> interpolateSensorSample(final java.util.List<SensorSample> sensorSamples,
                                                                 final long intervalMillis) {
        if (sensorSamples.isEmpty()) {
            return sensorSamples;
        }

        final Sensor sensor = sensorSamples.get(0).getSensor();

        final java.util.List<SensorSample> interpolatedSamples = new ArrayList<>();

        for (int i = 0; i < sensorSamples.size() - 1; i++) {
            final SensorSample currentSample = sensorSamples.get(i);
            final SensorSample nextSample = sensorSamples.get(i + 1);

            interpolatedSamples.add(currentSample);

            final long timeDiff = calculateTimeDiff(currentSample, nextSample);

            if (timeDiff <= intervalMillis) {
                continue;
            }

            final int numPoints = calculateNumPoints(timeDiff, intervalMillis);

            for (int numPoint = 1; numPoint <= numPoints; numPoint++) {
                final SensorSample interpolatedSample = generateInterpolatedSample(
                        currentSample,
                        nextSample,
                        calculateInterpolatedTime(
                                currentSample,
                                numPoint,
                                intervalMillis
                        ),
                        calculateProgress(numPoint, intervalMillis, timeDiff)
                );
                interpolatedSamples.add(interpolatedSample);
                interpolatedSample.setSensor(sensor);
            }
        }

        interpolatedSamples.add(sensorSamples.get(sensorSamples.size() - 1));

        return interpolatedSamples;
    }

    private double calculateProgress(final int numPoint,
                                     final long intervalMillis,
                                     final long timeDiff) {
        return (double) (numPoint * intervalMillis) / timeDiff;
    }

    private LocalDateTime calculateInterpolatedTime(final SensorSample currentSample,
                                                    final int numPoint,
                                                    final long intervalMillis) {
        return currentSample
                .getCreatedAt()
                .plusNanos(numPoint * intervalMillis * 1_000_000);
    }

    private int calculateNumPoints(final long timeDiff,
                                   final long intervalMillis) {
        return (int) (timeDiff / intervalMillis);
    }

    private long calculateTimeDiff(final SensorSample currentSample,
                                   final SensorSample nextSample) {
        return Duration.between(
                currentSample.getCreatedAt(),
                nextSample.getCreatedAt()
        ).toMillis();
    }

    private SensorSample generateInterpolatedSample(final SensorSample currentSample,
                                                    final SensorSample nextSample,
                                                    final LocalDateTime interpolatedTime,
                                                    final double progress) {
        return Try.of(currentSample::getSensor)
                .map(Sensor::getType)
                .flatMap(sensorType -> {
                    switch (sensorType) {
                        case GAS -> {
                            return generateInterpolatedSampleForGasSensor(
                                    currentSample,
                                    nextSample,
                                    interpolatedTime,
                                    progress
                            );
                        }
                        case LIGHT -> {
                            return generateInterpolatedSampleForLightSensor(
                                    currentSample,
                                    nextSample,
                                    interpolatedTime,
                                    progress
                            );
                        }
                        case TEMPERATURE -> {
                            return generateInterpolatedSampleForTemperatureSensor(
                                    currentSample,
                                    nextSample,
                                    interpolatedTime,
                                    progress
                            );
                        }
                        default -> {
                            return generateInterpolatedSampleForGenericSensor(
                                    currentSample,
                                    nextSample,
                                    interpolatedTime,
                                    progress
                            );
                        }
                    }
                })
                .get();
    }

    private Try<SensorSample> generateInterpolatedSampleForGenericSensor(SensorSample currentSample,
                                                                         SensorSample nextSample,
                                                                         LocalDateTime interpolatedTime,
                                                                         double progress) {
        return Try.of(() -> {
            double currentRaw = Double.parseDouble(currentSample.getRaw());
            double nextRaw = Double.parseDouble(nextSample.getRaw());
            double interpolatedValue = currentRaw * (1 - progress) + nextRaw * progress;

            final SensorSample interpolatedSensorSample = new SensorSample(
                    currentSample.getSensor(),
                    String.valueOf(interpolatedValue),
                    null
            );

            interpolatedSensorSample.setCreatedAt(interpolatedTime);
            interpolatedSensorSample.setUpdatedAt(interpolatedTime);
            interpolatedSensorSample.setId(null);
            return interpolatedSensorSample;
        });
    }

    private Try<SensorSample> generateInterpolatedSampleForTemperatureSensor(final SensorSample currentSample,
                                                                             final SensorSample nextSample,
                                                                             final LocalDateTime interpolatedTime,
                                                                             final double progress) {
        return Try.of(() -> {
            double currentRaw = Double.parseDouble(currentSample.getRaw());
            double nextRaw = Double.parseDouble(nextSample.getRaw());
            double interpolatedRawValue = currentRaw * (1 - progress) + nextRaw * progress;

            final NodeSenseProcessedModel currentProcessed = new Gson().fromJson(
                    currentSample.getProcessed(),
                    NodeSenseProcessedModel.class
            );
            final double currentCelsius = Double.parseDouble(currentProcessed.get("celsius"));
            final double currentFahrenheit = Double.parseDouble(currentProcessed.get("fahrenheit"));

            final NodeSenseProcessedModel nextProcessed = new Gson().fromJson(
                    nextSample.getProcessed(),
                    NodeSenseProcessedModel.class
            );
            final double nextCelsius = Double.parseDouble(nextProcessed.get("celsius"));
            final double nextFahrenheit = Double.parseDouble(nextProcessed.get("fahrenheit"));

            final double interpolatedCelsius = currentCelsius * (1 - progress) + nextCelsius * progress;
            final double interpolatedFahrenheit = currentFahrenheit * (1 - progress) + nextFahrenheit * progress;

            final NodeSenseProcessedModel interpolatedProcessed = new NodeSenseProcessedModel();
            interpolatedProcessed.put("celsius", String.valueOf(interpolatedCelsius));
            interpolatedProcessed.put("fahrenheit", String.valueOf(interpolatedFahrenheit));

            final SensorSample interpolatedSensorSample = new SensorSample(
                    currentSample.getSensor(),
                    String.valueOf(interpolatedRawValue),
                    new Gson().toJson(interpolatedProcessed)
            );

            interpolatedSensorSample.setCreatedAt(interpolatedTime);
            interpolatedSensorSample.setUpdatedAt(interpolatedTime);
            interpolatedSensorSample.setId(null);
            return interpolatedSensorSample;
        });
    }

    private Try<SensorSample> generateInterpolatedSampleForLightSensor(final SensorSample currentSample,
                                                                       final SensorSample nextSample,
                                                                       final LocalDateTime interpolatedTime,
                                                                       final double progress) {
        return generateInterpolatedSampleForGenericSensor(currentSample, nextSample, interpolatedTime, progress);
    }

    private Try<SensorSample> generateInterpolatedSampleForGasSensor(final SensorSample currentSample,
                                                                     final SensorSample nextSample,
                                                                     final LocalDateTime interpolatedTime,
                                                                     final double progress) {
        return generateInterpolatedSampleForGenericSensor(currentSample, nextSample, interpolatedTime, progress)
                .map(interpolatedSensorSample -> {
                    final NodeSenseProcessedModel currentProcessed = new Gson().fromJson(
                            currentSample.getProcessed(),
                            NodeSenseProcessedModel.class
                    );
                    if (currentProcessed == null) {
                        return interpolatedSensorSample;
                    }
                    final String threshold = currentProcessed.getOrDefault(
                            "threshold",
                            "0"
                    );
                    final boolean detected = (Double.parseDouble(nextSample.getRaw()) >= Double.parseDouble(threshold));

                    final NodeSenseProcessedModel interpolatedProcessed = new NodeSenseProcessedModel();
                    interpolatedProcessed.put("threshold", threshold);
                    interpolatedProcessed.put("detected", Boolean.toString(detected));
                    interpolatedSensorSample.setProcessed(new Gson().toJson(interpolatedProcessed));
                    return interpolatedSensorSample;
                });
    }

}
