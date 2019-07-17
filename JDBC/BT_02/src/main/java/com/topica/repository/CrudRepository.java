package com.topica.repository;

import java.sql.SQLException;
import java.util.Optional;

public interface CrudRepository<T, ID> {
    Iterable<T> findAll() throws SQLException;

    Optional<T> findById(ID id) throws SQLException;

    <S extends T> S save(S entity);

    void delete();
}
