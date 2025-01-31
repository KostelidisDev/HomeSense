package gr.ihu.robotics.homesense.api.controller.rpc.impl;

import gr.ihu.robotics.homesense.api.controller.rpc.SensorSampleRpcController;
import gr.ihu.robotics.homesense.domain.model.ChartJSModel;
import gr.ihu.robotics.homesense.service.mapper.ChartJsMapperService;
import gr.ihu.robotics.homesense.service.rpc.SensorSampleRpcService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/rpc/sensor-samples")
@RequiredArgsConstructor
public class SensorSampleRpcControllerImpl implements SensorSampleRpcController {

    private final SensorSampleRpcService sensorSampleRpcService;
    private final ChartJsMapperService chartJsMapperService;

    @Override
    @GetMapping("/chartjs/{sensorId}/{lastMinutes}/{interpolate}")
    public ResponseEntity<ChartJSModel> getSensorSamplesBySensorIdAndInLastMinutesInChartJSFormat(@PathVariable final UUID sensorId,
                                                                                                  @PathVariable final Long lastMinutes,
                                                                                                  @PathVariable final Boolean interpolate) {
        return sensorSampleRpcService.getInLastMinutesBySensorId(sensorId, lastMinutes, interpolate)
                .flatMap(chartJsMapperService::fromSensorSamples)
                .map(ResponseEntity::ok)
                .get();
    }
}
