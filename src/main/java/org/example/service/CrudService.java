package org.example.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface CrudService<T> {
    T create(T entity) throws SQLException;
    T update(Long id, T entity) throws SQLException;
    void delete(Long id) throws SQLException;
    Optional<T> getById(Long id) throws SQLException;
    List<T> getAll() throws SQLException;
}
