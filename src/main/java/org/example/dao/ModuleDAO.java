package org.example.dao;

import org.example.entity.ElementModule;
import org.example.entity.Module;
import org.example.entity.Semestre;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ModuleDAO extends AbstractDAO<Module> {

    @Override
    protected String getTableName() {
        return "Module";
    }

    @Override
    protected String getInsertQuery() {
        return "INSERT INTO Module (code, nom, filiere_id, semestre) VALUES (?, ?, ?, ?)";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE Module SET code = ?, nom = ?, filiere_id = ?, semestre = ? WHERE id = ?";
    }

    @Override
    protected void setInsertParameters(PreparedStatement stmt, Module entity) throws SQLException {
        stmt.setString(1, entity.getCode());
        stmt.setString(2, entity.getNom());
        stmt.setLong(3, entity.getFiliere().getId());
        stmt.setString(4, entity.getSemestre().name());
    }

    @Override
    protected void setUpdateParameters(PreparedStatement stmt, Module entity) throws SQLException {
        stmt.setString(1, entity.getCode());
        stmt.setString(2, entity.getNom());
        stmt.setLong(3, entity.getFiliere().getId());
        stmt.setString(4, entity.getSemestre().name());
        stmt.setLong(5, entity.getId());
    }

    @Override
    protected Module mapResultSetToEntity(ResultSet rs) throws SQLException {
        Module module = new Module();
        module.setId(rs.getLong("id"));
        module.setCode(rs.getString("code"));
        module.setNom(rs.getString("nom"));
        module.setSemestre(Semestre.valueOf(rs.getString("semestre")));

        // Load filiere
        Long filiereId = rs.getLong("filiere_id");
        FiliereDAO filiereDAO = new FiliereDAO();
        filiereDAO.findById(filiereId).ifPresent(module::setFiliere);

        // Load elementModules
        module.setElementModules(findElementModules(module.getId()));

        return module;
    }

    private List<ElementModule> findElementModules(Long moduleId) throws SQLException {
        String sql = "SELECT * FROM ElementModule WHERE module_id = ?";
        List<ElementModule> elements = new ArrayList<>();
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, moduleId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ElementModule element = new ElementModule();
                element.setId(rs.getLong("id"));
                element.setNom(rs.getString("nom"));
                element.setCoefficient(rs.getFloat("coefficient"));
                elements.add(element);
            }
        }
        return elements;
    }

    public List<Module> findBySemestre(Semestre semestre) throws SQLException {
        String sql = "SELECT * FROM Module WHERE semestre = ?";
        List<Module> modules = new ArrayList<>();
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, semestre.name());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                modules.add(mapResultSetToEntity(rs));
            }
        }
        return modules;
    }

    public Optional<Module> findByCode(String code) throws SQLException {
        String sql = "SELECT * FROM Module WHERE code = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, code);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToEntity(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public Module save(Module entity) throws SQLException {
        String sql = getInsertQuery();
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            setInsertParameters(stmt, entity);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    entity.setId(generatedKeys.getLong(1));
                }
            }
        }
        return entity;
    }

    @Override
    public Module update(Module entity) throws SQLException {
        String sql = getUpdateQuery();
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            setUpdateParameters(stmt, entity);
            stmt.executeUpdate();
        }
        return entity;
    }

    @Override
    public void delete(Long id) throws SQLException {
        String sql = "DELETE FROM Module WHERE id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public Optional<Module> findById(Long id) throws SQLException {
        String sql = "SELECT * FROM Module WHERE id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToEntity(rs));
            }
        }
        return Optional.empty();
    }
}