package org.example.dao;

import org.example.annotation.Component;
import org.example.entity.Etudiant;
import org.example.entity.Filiere;
import org.example.entity.Module;
import org.example.entity.Semestre;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class FiliereDAO extends AbstractDAO<Filiere> {
    @Override
    protected String getTableName() {
        return "Filiere";
    }

    @Override
    protected String getInsertQuery() {
        return "INSERT INTO Filiere (code, nom) VALUES (?, ?)";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE Filiere SET code = ?, nom = ? WHERE id = ?";
    }

    @Override
    protected void setInsertParameters(PreparedStatement stmt, Filiere entity) throws SQLException {
        stmt.setString(1, entity.getCode());
        stmt.setString(2, entity.getNom());
    }

    @Override
    protected void setUpdateParameters(PreparedStatement stmt, Filiere entity) throws SQLException {
        stmt.setString(1, entity.getCode());
        stmt.setString(2, entity.getNom());
        stmt.setLong(3, entity.getId());
    }

    @Override
    protected Filiere mapResultSetToEntity(ResultSet rs) throws SQLException {
        Filiere filiere = new Filiere();
        filiere.setId(rs.getLong("id"));
        filiere.setCode(rs.getString("code"));
        filiere.setNom(rs.getString("nom"));

        // Charger les modules et étudiants
        filiere.setModules(findModules(filiere.getId()));
        filiere.setEtudiants(findEtudiants(filiere.getId()));

        return filiere;
    }

    private List<Module> findModules(Long filiereId) throws SQLException {
        String sql = "SELECT * FROM Module WHERE filiere_id = ?";
        List<Module> modules = new ArrayList<>();
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, filiereId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Module module = new Module.Builder()
                        .id(rs.getLong("id"))
                        .code(rs.getString("code"))
                        .nom(rs.getString("nom"))
                        .semestre(Semestre.valueOf(rs.getString("semestre")))
                        .build();
                modules.add(module);
            }
        }
        return modules;
    }

    private List<Etudiant> findEtudiants(Long filiereId) throws SQLException {
        String sql = "SELECT * FROM Etudiant WHERE filiere_id = ?";
        List<Etudiant> etudiants = new ArrayList<>();
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, filiereId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Etudiant etudiant = new Etudiant();
                etudiant.setId(rs.getLong("id"));
                etudiant.setCne(rs.getString("cne"));
                etudiant.setNom(rs.getString("nom"));
                etudiant.setPrenom(rs.getString("prenom"));
                etudiants.add(etudiant);
            }
        }
        return etudiants;
    }

    public Optional<Filiere> findByCode(String code) throws SQLException {
        String sql = "SELECT * FROM Filiere WHERE code = ?";
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
    public Filiere save(Filiere entity) throws SQLException {
        String sql = getInsertQuery();
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            setInsertParameters(stmt, entity);
            stmt.executeUpdate();

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                entity.setId(generatedKeys.getLong(1));
            }
            return entity;
        }
    }

    @Override
    public Filiere update(Filiere entity) throws SQLException {
        String sql = getUpdateQuery();
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            setUpdateParameters(stmt, entity);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Update failed, no rows affected.");
            }
            return entity;
        }
    }

    @Override
    public void delete(Long id) throws SQLException {
        String sql = "DELETE FROM Filiere WHERE id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Delete failed, no rows affected.");
            }
        }
    }

    @Override
    public Optional<Filiere> findById(Long id) throws SQLException {
        String sql = "SELECT * FROM Filiere WHERE id = ?";
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