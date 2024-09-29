package net.vrakin.medsalary.service;

import java.util.List;
import java.util.Optional;

/**
 * A generic service interface that defines common CRUD operations.
 *
 * @param <T> The type of entity this service manages.
 */
public interface Service<T> {

    T save(T t);

    Optional<T> findById(Long id);

    List<T> findAll();

    void deleteById(Long id);

    void deleteAllById(List<Long> ids);

    public List<T> findAllById(List<Long> ids);

    void saveAll(List<T> entities);
}