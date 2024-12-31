package org.example.dao;


import org.example.entity.Evaluation;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EvaluationDAO extends AbstractDAO<Evaluation> {
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
        stmt.setObject(1, entity.getNote());  // Using setObject to handle null values
        stmt.setBoolean(2, entity.getAbsent());
        stmt.setLong(3, entity.getEtudiant().getId());
        stmt.setLong(4, entity.getModaliteEvaluation().getId());
    }

    @Override
    protected void setUpdateParameters(PreparedStatement stmt, Evaluation entity) throws SQLException {
        stmt.setObject(1, entity.getNote());  // Using setObject to handle null values
        stmt.setBoolean(2, entity.getAbsent());
        stmt.setLong(3, entity.getEtudiant().getId());
        stmt.setLong(4, entity.getModaliteEvaluation().getId());
        stmt.setLong(5, entity.getId());
    }

    @Override
    protected Evaluation mapResultSetToEntity(ResultSet rs) throws SQLException {
        Evaluation evaluation = new Evaluation();
        evaluation.setId(rs.getLong("id"));
        evaluation.setNote(rs.getObject("note", Float.class));  // Handle null values
        evaluation.setAbsent(rs.getBoolean("absent"));

        // Load the associated Etudiant
        Long etudiantId = rs.getLong("etudiant_id");
        EtudiantDAO etudiantDAO = new EtudiantDAO();
        etudiantDAO.findById(etudiantId).ifPresent(evaluation::setEtudiant);

        // Load the associated ModaliteEvaluation
        Long modaliteId = rs.getLong("modalite_evaluation_id");
        ModaliteEvaluationDAO modaliteDAO = new ModaliteEvaluationDAO();
        modaliteDAO.findById(modaliteId).ifPresent(evaluation::setModaliteEvaluation);

        return evaluation;
    }

    @Override
    public Evaluation save(Evaluation entity) throws SQLException {
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(getInsertQuery(), Statement.RETURN_GENERATED_KEYS)) {

            setInsertParameters(stmt, entity);
            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating evaluation failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    entity.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Creating evaluation failed, no ID obtained.");
                }
            }

            return entity;
        }
    }

    @Override
    public Evaluation update(Evaluation entity) throws SQLException {
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(getUpdateQuery())) {

            setUpdateParameters(stmt, entity);
            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Updating evaluation failed, no rows affected.");
            }

            return entity;
        }
    }

    @Override
    public void delete(Long id) throws SQLException {
        String sql = "DELETE FROM " + getTableName() + " WHERE id = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Deleting evaluation failed, no rows affected.");
            }
        }
    }

    @Override
    public Optional<Evaluation> findById(Long id) throws SQLException {
        String sql = "SELECT * FROM " + getTableName() + " WHERE id = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToEntity(rs));
            }

            return Optional.empty();
        }
    }

    // Additional utility methods

    public List<Evaluation> findByEtudiant(Long etudiantId) throws SQLException {
        String sql = "SELECT * FROM " + getTableName() + " WHERE etudiant_id = ?";
        List<Evaluation> evaluations = new ArrayList<>();

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, etudiantId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                evaluations.add(mapResultSetToEntity(rs));
            }
        }

        return evaluations;
    }

    public List<Evaluation> findByModaliteEvaluation(Long modaliteId) throws SQLException {
        String sql = "SELECT * FROM " + getTableName() + " WHERE modalite_evaluation_id = ?";
        List<Evaluation> evaluations = new ArrayList<>();

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, modaliteId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                evaluations.add(mapResultSetToEntity(rs));
            }
        }

        return evaluations;
    }

    public List<Evaluation> findAll() throws SQLException {
        String sql = "SELECT * FROM " + getTableName();
        List<Evaluation> evaluations = new ArrayList<>();

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                evaluations.add(mapResultSetToEntity(rs));
            }
        }

        return evaluations;
    }

    public float calculateAverageNoteByEtudiant(Long etudiantId) throws SQLException {
        String sql = "SELECT AVG(note) as average_note FROM " + getTableName() +
                " WHERE etudiant_id = ? AND note IS NOT NULL AND absent = false";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, etudiantId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getFloat("average_note");
            }

            return 0.0f;
        }
    }
}
