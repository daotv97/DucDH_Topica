package com.topica.repository;

public interface CrudRepository<T, ID> {

    String findById(ID id);

    String findAll();

    String save(T entity);
}
