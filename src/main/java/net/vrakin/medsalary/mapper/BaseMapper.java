package net.vrakin.medsalary.mapper;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Базовий інтерфейс для маперів, які відповідають за перетворення між сутностями (Entity) та об'єктами передачі даних (DTO).
 *
 * <p>Цей інтерфейс визначає загальні методи для мапінгу:</p>
 * <ul>
 *     <li>Перетворення сутності у DTO.</li>
 *     <li>Перетворення DTO у сутність.</li>
 *     <li>Масове перетворення списків сутностей у списки DTO та навпаки.</li>
 * </ul>
 *
 * @param <E> Тип сутності (Entity).
 * @param <D> Тип об'єкта передачі даних (DTO).
 *
 * @author YourName
 * @version 1.0
 */
public interface BaseMapper<E, D> {

    /**
     * Перетворює сутність (Entity) у DTO.
     *
     * @param entity Сутність для перетворення.
     * @return DTO, що відповідає переданій сутності.
     */
    D toDto(E entity);

    /**
     * Перетворює DTO у сутність (Entity).
     *
     * @param dto DTO для перетворення.
     * @return Сутність, що відповідає переданому DTO.
     * @throws Exception У разі виникнення помилок під час перетворення.
     */
    E toEntity(D dto) throws Exception;

    /**
     * Перетворює список сутностей (Entity) у список DTO.
     *
     * @param entityList Список сутностей для перетворення.
     * @return Список DTO, що відповідають сутностям.
     */
    default List<D> toDtoList(List<E> entityList) {
        return entityList.stream().map(this::toDto).collect(Collectors.toList());
    }

    /**
     * Перетворює список DTO у список сутностей (Entity).
     *
     * @param dtoList Список DTO для перетворення.
     * @return Список сутностей, що відповідають DTO.
     * @throws RuntimeException У разі виникнення помилок під час перетворення.
     */
    default List<E> toEntityList(List<D> dtoList) {
        return dtoList.stream().map(d -> {
            E entity = null;
            try {
                entity = toEntity(d);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            return entity;
        }).collect(Collectors.toList());
    }

    /**
     * Перетворює рядок у DTO.
     *
     * @param stringDto Рядок для перетворення.
     * @return DTO, отримане з рядка.
     */
    D toDto(String stringDto);

    /**
     * Перетворює рядок у сутність (Entity).
     *
     * @param stringEntity Рядок для перетворення.
     * @return Сутність, отримана з рядка.
     */
    E toEntity(String stringEntity);
}
