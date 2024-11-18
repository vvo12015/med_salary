package net.vrakin.medsalary.mapper;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

public interface BaseMapper <E, D>{
    D toDto(E entity);
    E toEntity(D dto) throws Exception;
    default List<D> toDtoList(List<E> entityList){
        return entityList.stream().map(this::toDto).collect(Collectors.toList());
    }
    default List<E> toEntityList(List<D> dtoList){
        return dtoList.stream().map(d->{
            E e = null;
            try {
                e = toEntity(d);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            return e;
        }).collect(Collectors.toList());
    }

    D toDto(String stringDto);
    E toEntity(String stringEntity);
}
