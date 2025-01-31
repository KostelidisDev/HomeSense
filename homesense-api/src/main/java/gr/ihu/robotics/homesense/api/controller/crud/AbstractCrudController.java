package gr.ihu.robotics.homesense.api.controller.crud;

import gr.ihu.robotics.homesense.api.util.HttpReqRespUtils;
import gr.ihu.robotics.homesense.domain.entity.AbstractEntity;
import gr.ihu.robotics.homesense.security.service.AuthorizationService;
import gr.ihu.robotics.homesense.service.crud.AbstractCrudService;
import io.vavr.Value;
import io.vavr.control.Option;
import io.vavr.control.Try;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

public interface AbstractCrudController<E extends AbstractEntity> {
    AbstractCrudService<E> getService();

    AuthorizationService getAuthorizationService();

    Try<URI> createUriByEntity(final E entity);

    @PostMapping("")
    default ResponseEntity<E> create(@RequestBody final E entity) {
        if (!(isAdminClient())) {
            return ResponseEntity.status(403).build();
        }

        return getService()
                .create(entity)
                .flatMap(_entity -> createUriByEntity(_entity)
                        .map(ResponseEntity::created)
                        .map(bodyBuilder -> bodyBuilder.body(_entity)))
                .get();
    }

    default boolean isAdminClient() {
        final String clientIpAddressIfServletRequestExist = HttpReqRespUtils.getClientIpAddressIfServletRequestExist();
        if (clientIpAddressIfServletRequestExist == null) {
            return false;
        }
        return getAuthorizationService().isAllowed(clientIpAddressIfServletRequestExist);
    }

    @GetMapping("")
    default ResponseEntity<List<E>> findAll() {
        return getService()
                .findAll()
                .map(Value::toJavaList)
                .map(ResponseEntity::ok)
                .get();
    }

    @GetMapping("/{id}")
    default ResponseEntity<E> findById(@PathVariable final UUID id) {
        return getService()
                .findById(id)
                .flatMap(Option::toTry)
                .map(ResponseEntity::ok)
                .get();
    }

    @PutMapping("/{id}")
    default ResponseEntity<E> update(@PathVariable final UUID id, @RequestBody final E entity) {
        if (!(isAdminClient())) {
            return ResponseEntity.status(403).build();
        }
        return getService()
                .update(id, entity)
                .map(ResponseEntity::ok)
                .get();
    }

    @DeleteMapping("/{id}")
    default ResponseEntity<Void> delete(@PathVariable final UUID id) {
        if (!(isAdminClient())) {
            return ResponseEntity.status(403).build();
        }
        return getService()
                .deleteById(id)
                .map(ignored -> ResponseEntity.noContent())
                .map(ResponseEntity.HeadersBuilder::<Void>build)
                .get();
    }
}
