package com.topica.dao;

import com.topica.annotations.Table;

public interface Dao<T, ID> {

    default String findAll(T entity) {
        if (entity.getClass().isAnnotationPresent(Table.class))
            return "SELECT * FROM " + entity.getClass().getDeclaredAnnotation(Table.class).name();
        return "";
    }

//    Optional<T> findById(ID id);
//
//    <S extends T> T save(S entity);
//
//    <S extends T> T update(S entity);
}
