package gr.ihu.robotics.homesense.api.controller.rpc.impl;

import gr.ihu.robotics.homesense.api.controller.rpc.SensorRpcController;
import gr.ihu.robotics.homesense.domain.entity.Sensor;
import gr.ihu.robotics.homesense.service.rpc.SensorRpcService;
import io.vavr.Value;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/rpc/sensors")
@RequiredArgsConstructor
public class SensorRpcControllerImpl implements SensorRpcController {

    private final SensorRpcService sensorRpcService;

    @Override
    @GetMapping("/discover")
    public ResponseEntity<List<Sensor>> discoverSensors() {
        return sensorRpcService.discoverSensors()
                .map(Value::toJavaList)
                .map(ResponseEntity::ok)
                .get();
    }

    @Override
    @GetMapping("/discover/{ip}")
    public ResponseEntity<Sensor> discover(@PathVariable final String ip) {
        return sensorRpcService.discoverSensor(ip)
                .map(ResponseEntity::ok)
                .get();
    }
}
