package gr.ihu.robotics.homesense.api.controller.crud.impl;

import gr.ihu.robotics.homesense.api.controller.crud.SensorSampleCrudController;
import gr.ihu.robotics.homesense.domain.entity.SensorSample;
import gr.ihu.robotics.homesense.security.service.AuthorizationService;
import gr.ihu.robotics.homesense.service.crud.AbstractCrudService;
import gr.ihu.robotics.homesense.service.crud.SensorSampleCrudService;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/crud/sensor-samples")
@RequiredArgsConstructor
public class SensorSampleCrudControllerImpl implements SensorSampleCrudController {

    private final SensorSampleCrudService sensorSampleCrudService;
    private final AuthorizationService authorizationService;

    @Override
    public AbstractCrudService<SensorSample> getService() {
        return sensorSampleCrudService;
    }

    @Override
    public AuthorizationService getAuthorizationService() {
        return authorizationService;
    }

    @Override
    public Try<URI> createUriByEntity(final SensorSample entity) {
        return Try.of(entity::getId)
                .map(id -> String.format("%s/%s", "/api/crud/sensor-samples", id))
                .map(URI::create);
    }
}
