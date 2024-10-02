package net.vrakin.medsalary.mapper;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

public interface BaseMapper <E, D>{
    D toDto(E entity);
    E toEntity(D dto);
    default List<D> toDtoList(List<E> entityList){
        return entityList.stream().map(this::toDto).collect(Collectors.toList());
    }
    default List<E> toEntityList(List<D> dtoList){
        return dtoList.stream().map(d->{
            if (d.toString().contains("servicePackageName")) {
                System.out.println(d.toString().substring(d.toString().indexOf("servicePackageName"),
                        d.toString().indexOf("servicePackageNumber")));
            }
            var e = toEntity(d);
            if (e.toString().contains("servicePackageName")) {
                System.out.println(e.toString().substring(d.toString().indexOf("servicePackageName"),
                        e.toString().indexOf("servicePackageNumber")));
            }
            return e;
        }).collect(Collectors.toList());
    }

    D toDto(String stringDto);
    E toEntity(String stringEntity);
}
