package org.example.dao;

import org.example.annotation.Component;
import org.example.entity.Professeur;
import org.example.entity.Role;
import org.example.entity.Utilisateur;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Component
public class UtilisateurDAO extends AbstractDAO<Utilisateur> {
    private final ProfesseurDAO professeurDAO;

    public UtilisateurDAO(ProfesseurDAO professeurDAO) {
        this.professeurDAO = professeurDAO;
    }

    @Override
    protected String getTableName() {
        return "Utilisateur";
    }

    @Override
    protected String getInsertQuery() {
        return "INSERT INTO Utilisateur (login, password, role) VALUES (?, ?, ?)";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE Utilisateur SET login = ?, password = ?, role = ? WHERE id = ?";
    }

    @Override
    protected void setInsertParameters(PreparedStatement stmt, Utilisateur entity) throws SQLException {
        stmt.setString(1, entity.getLogin());
        stmt.setString(2, entity.getPassword());
        stmt.setString(3, entity.getRole().toString());
    }

    @Override
    protected void setUpdateParameters(PreparedStatement stmt, Utilisateur entity) throws SQLException {
        stmt.setString(1, entity.getLogin());
        stmt.setString(2, entity.getPassword());
        stmt.setString(3, entity.getRole().toString());
        stmt.setLong(4, entity.getId());
    }

    @Override
    protected Utilisateur mapResultSetToEntity(ResultSet rs) throws SQLException {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(rs.getLong("id"));
        utilisateur.setLogin(rs.getString("login"));
        utilisateur.setPassword(rs.getString("password"));
        utilisateur.setRole(Role.valueOf(rs.getString("role")));

        // Load associated professor if exists
        Optional<Professeur> professeur = professeurDAO.findByUtilisateurId(utilisateur.getId());
        professeur.ifPresent(utilisateur::setProfesseur);

        return utilisateur;
    }

    @Override
    public Utilisateur save(Utilisateur entity) throws SQLException {
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
    public Utilisateur update(Utilisateur entity) throws SQLException {
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
        String sql = "DELETE FROM Utilisateur WHERE id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public Optional<Utilisateur> findById(Long id) throws SQLException {
        String sql = "SELECT * FROM Utilisateur WHERE id = ?";
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

    public List<Utilisateur> findAll() throws SQLException {
        String sql = "SELECT * FROM Utilisateur";
        List<Utilisateur> utilisateurs = new ArrayList<>();
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                utilisateurs.add(mapResultSetToEntity(rs));
            }
        }
        return utilisateurs;
    }

    public Optional<Utilisateur> findByLogin(String login) throws SQLException {
        String sql = "SELECT * FROM Utilisateur WHERE login = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, login);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToEntity(rs));
                }
                return Optional.empty();
            }
        }
    }
}