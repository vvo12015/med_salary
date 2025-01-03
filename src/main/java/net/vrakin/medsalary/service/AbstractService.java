package net.vrakin.medsalary.service;

import net.vrakin.medsalary.exception.ResourceNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Абстрактний сервісний клас, що надає базовий CRUD-функціонал для будь-якого типу сутності.
 *
 * <p>Цей клас спрощує роботу з даними, використовуючи стандартні методи репозиторія {@link JpaRepository}.
 * Він забезпечує такі операції:
 * <ul>
 *     <li>Пошук усіх записів.</li>
 *     <li>Пошук запису за ідентифікатором.</li>
 *     <li>Збереження сутності.</li>
 *     <li>Видалення сутності за ідентифікатором або списком ідентифікаторів.</li>
 *     <li>Збереження списку сутностей.</li>
 *     <li>Видалення всіх записів.</li>
 * </ul>
 * </p>
 *
 * @param <E> Тип сутності, для якої виконується сервісна логіка.
 * @author YourName
 * @version 1.0
 */
public abstract class AbstractService<E> {

    /**
     * Репозиторій для виконання CRUD-операцій.
     */
    protected final JpaRepository<E, Long> repository;

    /**
     * Конструктор, що приймає репозиторій для обробки даних.
     *
     * @param repository Інтерфейс {@link JpaRepository}, пов'язаний із сутністю.
     */
    protected AbstractService(JpaRepository<E, Long> repository) {
        this.repository = repository;
    }

    /**
     * Повертає список усіх записів у базі даних.
     *
     * @return Список усіх сутностей.
     */
    public List<E> findAll() {
        return repository.findAll();
    }

    /**
     * Знаходить сутність за її ідентифікатором.
     *
     * @param id Ідентифікатор сутності.
     * @return Об'єкт {@link Optional}, що містить сутність, якщо вона існує.
     */
    public Optional<E> findById(Long id) {
        return repository.findById(id);
    }

    /**
     * Зберігає сутність у базі даних.
     *
     * @param entity Сутність, яку потрібно зберегти.
     * @return Збережена сутність.
     */
    public E save(E entity) {
        return repository.save(entity);
    }

    /**
     * Видаляє сутність за її ідентифікатором.
     *
     * @param id Ідентифікатор сутності.
     * @throws ResourceNotFoundException Якщо сутність із таким ідентифікатором не знайдено.
     */
    public void deleteById(Long id) throws ResourceNotFoundException {
        repository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Entity", "id", id.toString()));
        repository.deleteById(id);
    }

    /**
     * Видаляє список сутностей за їхніми ідентифікаторами.
     *
     * @param ids Список ідентифікаторів сутностей.
     */
    public void deleteAllById(List<Long> ids) {
        ids.forEach(id -> {
            try {
                deleteById(id);
            } catch (ResourceNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Повертає список сутностей за їхніми ідентифікаторами.
     *
     * @param ids Список ідентифікаторів.
     * @return Список знайдених сутностей.
     * @throws ResourceNotFoundException Якщо жодна сутність не знайдена.
     */
    public List<E> findAllById(List<Long> ids) throws ResourceNotFoundException {
        return repository.findAllById(ids);
    }

    /**
     * Зберігає список сутностей у базі даних.
     *
     * @param entities Список сутностей для збереження.
     */
    public void saveAll(List<E> entities) {
        repository.saveAll(entities);
    }

    /**
     * Видаляє всі записи з бази даних.
     */
    public void deleteAll() {
        repository.deleteAll();
    }
}
