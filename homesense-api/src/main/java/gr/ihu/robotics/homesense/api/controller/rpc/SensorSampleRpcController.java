package gr.ihu.robotics.homesense.api.controller.rpc;

import gr.ihu.robotics.homesense.domain.model.ChartJSModel;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface SensorSampleRpcController {
    ResponseEntity<ChartJSModel> getSensorSamplesBySensorIdAndInLastMinutesInChartJSFormat(UUID sensorId,
                                                                                           Long lastMinutes,
                                                                                           Boolean interpolate
    );
}
