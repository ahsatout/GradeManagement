package org.example.dao;


import org.example.annotation.Component;
import org.example.entity.Etudiant;
import org.example.entity.Filiere;
import org.example.entity.Evaluation;
import org.example.entity.ModaliteEvaluation;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component

public class EtudiantDAO extends AbstractDAO<Etudiant> {

    @Override
    protected String getTableName() {
        return "etudiant";
    }

    @Override
    protected String getInsertQuery() {
        return "INSERT INTO etudiant (cne, nom, prenom, filiere_id) VALUES (?, ?, ?, ?)";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE etudiant SET cne = ?, nom = ?, prenom = ?, filiere_id = ? WHERE id = ?";
    }

    @Override
    protected void setInsertParameters(PreparedStatement stmt, Etudiant etudiant) throws SQLException {
        stmt.setString(1, etudiant.getCne());
        stmt.setString(2, etudiant.getNom());
        stmt.setString(3, etudiant.getPrenom());
        stmt.setLong(4, etudiant.getFiliere().getId());
    }

    @Override
    protected void setUpdateParameters(PreparedStatement stmt, Etudiant etudiant) throws SQLException {
        setInsertParameters(stmt, etudiant);
        stmt.setLong(5, etudiant.getId());
    }

    @Override
    protected Etudiant mapResultSetToEntity(ResultSet rs) throws SQLException {
        Etudiant etudiant = new Etudiant();
        etudiant.setId(rs.getLong("id"));
        etudiant.setCne(rs.getString("cne"));
        etudiant.setNom(rs.getString("nom"));
        etudiant.setPrenom(rs.getString("prenom"));

        // Load the Filiere
        Long filiereId = rs.getLong("filiere_id");
        loadFiliere(filiereId).ifPresent(etudiant::setFiliere);

        // Load evaluations
        List<Evaluation> evaluations = loadEvaluations(etudiant.getId());
        etudiant.setEvaluations(evaluations);

        return etudiant;
    }

    private Optional<Filiere> loadFiliere(Long filiereId) throws SQLException {
        String sql = "SELECT * FROM filiere WHERE id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, filiereId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Filiere filiere = new Filiere();
                    filiere.setId(rs.getLong("id"));
                    // Set other Filiere properties as needed
                    return Optional.of(filiere);
                }
            }
        }
        return Optional.empty();
    }

    private List<Evaluation> loadEvaluations(Long etudiantId) throws SQLException {
        List<Evaluation> evaluations = new ArrayList<>();
        String sql = "SELECT e.*, m.* FROM evaluation e " +
                "LEFT JOIN modalite_evaluation m ON e.modalite_evaluation_id = m.id " +
                "WHERE e.etudiant_id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, etudiantId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Evaluation evaluation = new Evaluation();
                    evaluation.setId(rs.getLong("e.id"));
                    evaluation.setNote(rs.getFloat("e.note"));
                    evaluation.setAbsent(rs.getBoolean("e.absent"));

                    // Create and set ModaliteEvaluation
                    ModaliteEvaluation modalite = new ModaliteEvaluation();
                    modalite.setId(rs.getLong("m.id"));
                    // Set other ModaliteEvaluation properties
                    evaluation.setModaliteEvaluation(modalite);

                    evaluations.add(evaluation);
                }
            }
        }
        return evaluations;
    }

    @Override
    public Etudiant save(Etudiant etudiant) throws SQLException {
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(getInsertQuery(), Statement.RETURN_GENERATED_KEYS)) {
            setInsertParameters(stmt, etudiant);
            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating Etudiant failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    etudiant.setId(generatedKeys.getLong(1));
                }
                else {
                    throw new SQLException("Creating Etudiant failed, no ID obtained.");
                }
            }
            return etudiant;
        }
    }

    @Override
    public Etudiant update(Etudiant etudiant) throws SQLException {
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(getUpdateQuery())) {
            setUpdateParameters(stmt, etudiant);
            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Updating Etudiant failed, no rows affected.");
            }
            return etudiant;
        }
    }

    @Override
    public void delete(Long id) throws SQLException {
        String sql = "DELETE FROM etudiants WHERE id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }

    // Additional helper methods as needed
    public Optional<Etudiant> findByCNE(String cne) throws SQLException {
        String sql = "SELECT * FROM " + getTableName() + " WHERE cne = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cne);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToEntity(rs));
                }
            }
            return Optional.empty();
        }
    }
}
