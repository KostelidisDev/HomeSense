package gr.ihu.robotics.homesense.service.crud;

import gr.ihu.robotics.homesense.domain.entity.AbstractEntity;
import gr.ihu.robotics.homesense.domain.repository.AbstractRepository;
import io.vavr.collection.List;
import io.vavr.control.Option;
import io.vavr.control.Try;

import java.util.UUID;

public interface AbstractCrudService<E extends AbstractEntity> {
    AbstractRepository<E> getRepository();

    default Try<E> create(final E entity) {
        return Try.of(() -> getRepository().save(entity));
    }

    default Try<List<E>> create(final List<E> entities) {
        return Try.of(() -> getRepository().saveAll(entities))
                .map(List::ofAll);
    }

    default Try<List<E>> findAll() {
        return Try.of(() -> getRepository().findAll()).map(List::ofAll);
    }

    default Try<Option<E>> findById(final UUID id) {
        return Try.of(() -> getRepository().findById(id))
                .map(Option::ofOptional);
    }

    default Try<E> update(final UUID id, final E entity) {
        return findById(id)
                .flatMap(Option::toTry)
                .flatMap(originalEntity -> {
                    entity.setId(originalEntity.getId());
                    return Try.of(() -> getRepository().save(entity));
                });
    }

    default Try<Void> deleteById(final UUID id) {
        return Try.run(() -> getRepository().deleteById(id));
    }
}
