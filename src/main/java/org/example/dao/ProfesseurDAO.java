package org.example.dao;

import org.example.annotation.Component;
import org.example.entity.ElementModule;
import org.example.entity.Professeur;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ProfesseurDAO extends AbstractDAO<Professeur> {
    @Override
    protected String getTableName() {
        return "Professeur";
    }

    @Override
    protected String getInsertQuery() {
        return "INSERT INTO Professeur (code, nom, prenom, specialite, utilisateur_id) VALUES (?, ?, ?, ?, ?)";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE Professeur SET code = ?, nom = ?, prenom = ?, specialite = ?, utilisateur_id = ? WHERE id = ?";
    }

    @Override
    protected void setInsertParameters(PreparedStatement stmt, Professeur entity) throws SQLException {
        stmt.setString(1, entity.getCode());
        stmt.setString(2, entity.getNom());
        stmt.setString(3, entity.getPrenom());
        stmt.setString(4, entity.getSpecialite());
        if (entity.getUtilisateur() != null && entity.getUtilisateur().getId() != null) {
            stmt.setLong(5, entity.getUtilisateur().getId());
        } else {
            stmt.setNull(5, Types.BIGINT);
        }
    }

    @Override
    protected void setUpdateParameters(PreparedStatement stmt, Professeur entity) throws SQLException {
        stmt.setString(1, entity.getCode());
        stmt.setString(2, entity.getNom());
        stmt.setString(3, entity.getPrenom());
        stmt.setString(4, entity.getSpecialite());
        if (entity.getUtilisateur() != null && entity.getUtilisateur().getId() != null) {
            stmt.setLong(5, entity.getUtilisateur().getId());
        } else {
            stmt.setNull(5, Types.BIGINT);
        }
        stmt.setLong(6, entity.getId());
    }

    @Override
    protected Professeur mapResultSetToEntity(ResultSet rs) throws SQLException {
        Professeur professeur = new Professeur();
        professeur.setId(rs.getLong("id"));
        professeur.setCode(rs.getString("code"));
        professeur.setNom(rs.getString("nom"));
        professeur.setPrenom(rs.getString("prenom"));
        professeur.setSpecialite(rs.getString("specialite"));

        // Load element modules
        professeur.setElementModules(findElementModules(professeur.getId()));

        return professeur;
    }

    private List<ElementModule> findElementModules(Long professeurId) throws SQLException {
        String sql = "SELECT * FROM ElementModule WHERE professeur_id = ?";
        List<ElementModule> elementModules = new ArrayList<>();
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, professeurId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ElementModule elementModule = new ElementModule();
                    elementModule.setId(rs.getLong("id"));
                    elementModule.setNom(rs.getString("nom"));
                    elementModule.setCoefficient(rs.getFloat("coefficient"));
                    elementModules.add(elementModule);
                }
            }
        }
        return elementModules;
    }

    public Optional<Professeur> findByUtilisateurId(Long utilisateurId) throws SQLException {
        String sql = "SELECT * FROM Professeur WHERE utilisateur_id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, utilisateurId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToEntity(rs));
                }
                return Optional.empty();
            }
        }
    }

    @Override
    public Professeur save(Professeur entity) throws SQLException {
        String sql = getInsertQuery();
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setInsertParameters(stmt, entity);
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    entity.setId(generatedKeys.getLong(1));
                }
            }
        }
        return entity;
    }

    @Override
    public Professeur update(Professeur entity) throws SQLException {
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
        String sql = "DELETE FROM Professeur WHERE id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error deleting professeur", e);
        }
    }

    @Override
    public Optional<Professeur> findById(Long id) throws SQLException {
        String sql = "SELECT * FROM Professeur WHERE id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToEntity(rs));
                }
                return Optional.empty();
            }
        }
    }

    public List<Professeur> findAll() throws SQLException {
        String sql = "SELECT * FROM Professeur";
        List<Professeur> professeurs = new ArrayList<>();
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                professeurs.add(mapResultSetToEntity(rs));
            }
        }
        return professeurs;
    }
}