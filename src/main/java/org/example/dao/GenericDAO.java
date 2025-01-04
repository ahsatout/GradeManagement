package org.example.dao;


import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface GenericDAO<T> {
    T save(T entity) throws SQLException;
    T update(T entity) throws SQLException;
    void delete(Long id) throws SQLException;
    Optional<T> findById(Long id) throws SQLException;
    List<T> findAll() throws SQLException;
}
