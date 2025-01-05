package org.example.dao;

import org.example.annotation.Component;
import org.example.entity.Etudiant;
import org.example.entity.Evaluation;
import org.example.entity.ModaliteEvaluation;
import org.example.entity.Professeur;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class EvaluationDAO extends AbstractDAO<Evaluation> {

    // Méthode pour récupérer la connexion à la base de données
    private Connection getConnection() throws SQLException {
        return dbConnection.getConnection();
    }

    @Override
    public Evaluation save(Evaluation entity) throws SQLException {
        String insertQuery = "INSERT INTO Evaluation (note, absent, etudiant_id, modalite_evaluation_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setFloat(1, entity.getNote());
            stmt.setBoolean(2, entity.getAbsent());
            stmt.setLong(3, entity.getEtudiant().getId());
            stmt.setLong(4, entity.getModaliteEvaluation().getId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        entity.setId(generatedKeys.getLong(1)); // Set the generated ID
                    }
                }
            }
        }
        return entity;
    }

    @Override
    public Evaluation update(Evaluation entity) throws SQLException {
        String updateQuery = "UPDATE Evaluation SET note = ?, absent = ?, etudiant_id = ?, modalite_evaluation_id = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(updateQuery)) {

            stmt.setFloat(1, entity.getNote());
            stmt.setBoolean(2, entity.getAbsent());
            stmt.setLong(3, entity.getEtudiant().getId());
            stmt.setLong(4, entity.getModaliteEvaluation().getId());
            stmt.setLong(5, entity.getId());

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0 ? entity : null;
        }
    }

    @Override
    public void delete(Long id) throws SQLException {
        String deleteQuery = "DELETE FROM Evaluation WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(deleteQuery)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    protected String getTableName() {
        return "Evaluation";
    }

    @Override
    protected String getInsertQuery() {
        return "INSERT INTO Evaluation (note, absent, etudiant_id, modalite_evaluation_id) VALUES (?, ?, ?, ?)";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE Evaluation SET note = ?, absent = ?, etudiant_id = ?, modalite_evaluation_id = ? WHERE id = ?";
    }

    @Override
    protected void setInsertParameters(PreparedStatement stmt, Evaluation entity) throws SQLException {
        stmt.setFloat(1, entity.getNote());
        stmt.setBoolean(2, entity.getAbsent());
        stmt.setLong(3, entity.getEtudiant().getId());
        stmt.setLong(4, entity.getModaliteEvaluation().getId());
    }

    @Override
    protected void setUpdateParameters(PreparedStatement stmt, Evaluation entity) throws SQLException {
        stmt.setFloat(1, entity.getNote());
        stmt.setBoolean(2, entity.getAbsent());
        stmt.setLong(3, entity.getEtudiant().getId());
        stmt.setLong(4, entity.getModaliteEvaluation().getId());
        stmt.setLong(5, entity.getId());
    }

    @Override
    protected Evaluation mapResultSetToEntity(ResultSet rs) throws SQLException {
        Evaluation evaluation = new Evaluation();
        evaluation.setId(rs.getLong("id"));
        evaluation.setNote(rs.getFloat("note"));
        evaluation.setAbsent(rs.getBoolean("absent"));

        // Load associated Etudiant and ModaliteEvaluation
        Etudiant etudiant = new Etudiant();
        etudiant.setId(rs.getLong("etudiant_id"));
        evaluation.setEtudiant(etudiant);

        ModaliteEvaluation modaliteEvaluation = new ModaliteEvaluation();
        modaliteEvaluation.setId(rs.getLong("modalite_evaluation_id"));
        evaluation.setModaliteEvaluation(modaliteEvaluation);

        return evaluation;
    }

    // Méthode pour récupérer toutes les évaluations d'un professeur
    public List<Evaluation> findEvaluationsByProfesseurId(Long professeurId) throws SQLException {
        String sql = "SELECT * FROM Evaluation e " +
                "JOIN Etudiant et ON e.etudiant_id = et.id " +
                "WHERE et.professeur_id = ?";
        List<Evaluation> evaluations = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, professeurId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    evaluations.add(mapResultSetToEntity(rs));
                }
            }
        }
        return evaluations;
    }

    // Méthode pour valider une évaluation
    public boolean validateEvaluation(Long evaluationId) throws SQLException {
        String sql = "UPDATE Evaluation SET validated = TRUE WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, evaluationId);
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        }
    }

    // Méthode pour calculer la moyenne des notes d'un élément (module d'évaluation)
    public float calculateAverage(Long elementModuleId) throws SQLException {
        String sql = "SELECT AVG(note) AS average FROM Evaluation WHERE modalite_evaluation_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, elementModuleId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getFloat("average");
                }
            }
        }
        return 0;
    }

    // Méthode pour vérifier si une note est valide
    public boolean isValidNote(Float note) {
        return note >= 0 && note <= 20;
    }

    // Méthode pour vérifier que toutes les notes sont saisies
    public boolean areAllNotesEntered(Long elementModuleId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Evaluation WHERE modalite_evaluation_id = ? AND note IS NULL";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, elementModuleId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) == 0;
                }
            }
        }
        return false;
    }

    // Méthode pour vérifier si des 0 ou 20 existent dans les évaluations
    public boolean containsZeroOrTwenty(Long elementModuleId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Evaluation WHERE modalite_evaluation_id = ? AND (note = 0 OR note = 20)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, elementModuleId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    // Méthode pour exporter les notes d'un élément
    public void exportNotesToFile(Long elementModuleId, String format) throws SQLException {
        List<Evaluation> evaluations = findEvaluationsByProfesseurId(elementModuleId);
        if ("PDF".equalsIgnoreCase(format)) {
            // Code pour exporter au format PDF
        } else if ("Excel".equalsIgnoreCase(format)) {
            // Code pour exporter au format Excel
        }
    }
}
