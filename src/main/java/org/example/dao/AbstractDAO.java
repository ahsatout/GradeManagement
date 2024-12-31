package org.example.dao;

import org.example.config.DatabaseConnection;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class AbstractDAO<T> implements GenericDAO<T> {
    protected final DatabaseConnection dbConnection;

    protected AbstractDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
    }

    protected abstract T mapResultSetToEntity(ResultSet rs) throws SQLException;
    protected abstract String getTableName();
    protected abstract String getInsertQuery();
    protected abstract String getUpdateQuery();
    protected abstract void setInsertParameters(PreparedStatement stmt, T entity) throws SQLException;
    protected abstract void setUpdateParameters(PreparedStatement stmt, T entity) throws SQLException;


    public Optional<T> findById(Long id) throws SQLException {
        String sql = "SELECT * FROM " + getTableName() + " WHERE id = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToEntity(rs));
                }
            }
            return Optional.empty();

        } catch (SQLException e) {
            throw new SQLException("Error finding entity by ID", e);
        }
    }

    @Override
    public List<T> findAll() throws SQLException {
        String sql = "SELECT * FROM " + getTableName();
        List<T> entities = new ArrayList<>();

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                entities.add(mapResultSetToEntity(rs));
            }
            return entities;

        } catch (SQLException e) {
            throw new SQLException("Error finding all entities", e);
        }
    }
}
