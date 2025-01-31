package gr.ihu.robotics.homesense.api.controller.crud.impl;

import gr.ihu.robotics.homesense.api.controller.crud.SensorCrudController;
import gr.ihu.robotics.homesense.domain.entity.Sensor;
import gr.ihu.robotics.homesense.security.service.AuthorizationService;
import gr.ihu.robotics.homesense.service.crud.AbstractCrudService;
import gr.ihu.robotics.homesense.service.crud.SensorCrudService;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/crud/sensors")
@RequiredArgsConstructor
public class SensorCrudControllerImpl implements SensorCrudController {
    private final SensorCrudService sensorCrudService;
    private final AuthorizationService authorizationService;

    @Override
    public AbstractCrudService<Sensor> getService() {
        return sensorCrudService;
    }

    @Override
    public AuthorizationService getAuthorizationService() {
        return authorizationService;
    }

    @Override
    public Try<URI> createUriByEntity(final Sensor entity) {
        return Try.of(entity::getId)
                .map(id -> String.format("%s/%s", "/api/crud/sensors", id))
                .map(URI::create);
    }
}
