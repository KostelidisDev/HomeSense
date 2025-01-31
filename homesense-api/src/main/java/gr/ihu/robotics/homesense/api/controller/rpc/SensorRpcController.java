package gr.ihu.robotics.homesense.api.controller.rpc;

import gr.ihu.robotics.homesense.domain.entity.Sensor;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface SensorRpcController {
    ResponseEntity<List<Sensor>> discoverSensors();

    ResponseEntity<Sensor> discover(String ip);
}
