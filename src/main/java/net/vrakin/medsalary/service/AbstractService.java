package net.vrakin.medsalary.service;

import net.vrakin.medsalary.exception.ResourceNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public abstract class AbstractService<E> {
    protected final JpaRepository<E, Long> repository;

    protected AbstractService(JpaRepository<E, Long> repository) {
        this.repository = repository;
    }
    public List<E> findAll() {
        return repository.findAll();
    }

    public Optional<E> findById(Long id) {
        return repository.findById(id);
    }

    public E save(E entity) {
        return repository.save(entity);
    }

    public void deleteById(Long id) throws ResourceNotFoundException {
        repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Entity", "id", id.toString()));
        repository.deleteById(id);
    }

    public void deleteAllById(List<Long> ids) {
        ids.forEach(id -> {
            try {
                deleteById(id);
            } catch (ResourceNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public List<E> findAllById(List<Long> ids) throws ResourceNotFoundException {
        return repository.findAllById(ids);
    }

    public void saveAll(List<E> entities){
        repository.saveAll(entities);
    }

    public void deleteAll(){
        repository.deleteAll();
    }
}
