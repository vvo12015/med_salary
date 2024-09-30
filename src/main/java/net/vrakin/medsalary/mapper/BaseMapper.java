package net.vrakin.medsalary.mapper;

import java.util.List;
import java.util.stream.Collectors;

public interface BaseMapper <E, D>{
    D toDto(E entity);
    E toEntity(D dto);
    default List<D> toDtoList(List<E> entityList){
        return entityList.stream().map(this::toDto).collect(Collectors.toList());
    }
    default List<E> toEntityList(List<D> dtoList){
        return dtoList.stream().map(this::toEntity).collect(Collectors.toList());
    }

    D toDto(String stringDto);
    E toEntity(String stringEntity);
}
