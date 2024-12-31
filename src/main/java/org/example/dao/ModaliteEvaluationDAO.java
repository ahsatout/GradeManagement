package org.example.dao;

import org.example.annotation.Component;
import org.example.entity.Evaluation;
import org.example.entity.ModaliteEvaluation;
import org.example.entity.Type;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ModaliteEvaluationDAO extends AbstractDAO<ModaliteEvaluation> {
    @Override
    protected String getTableName() {
        return "Modalite_Evaluation";
    }

    @Override
    protected String getInsertQuery() {
        return "INSERT INTO Modalite_Evaluation (type, coefficient, element_module_id) VALUES (?, ?, ?)";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE Modalite_Evaluation SET type = ?, coefficient = ?, element_module_id = ? WHERE id = ?";
    }

    @Override
    protected void setInsertParameters(PreparedStatement stmt, ModaliteEvaluation entity) throws SQLException {
        stmt.setString(1, entity.getType().name());
        stmt.setFloat(2, entity.getCoefficient());
        stmt.setLong(3, entity.getElementModule().getId());
    }

    @Override
    protected void setUpdateParameters(PreparedStatement stmt, ModaliteEvaluation entity) throws SQLException {
        stmt.setString(1, entity.getType().name());
        stmt.setFloat(2, entity.getCoefficient());
        stmt.setLong(3, entity.getElementModule().getId());
        stmt.setLong(4, entity.getId());
    }

    @Override
    protected ModaliteEvaluation mapResultSetToEntity(ResultSet rs) throws SQLException {
        ModaliteEvaluation modalite = new ModaliteEvaluation();
        modalite.setId(rs.getLong("id"));
        modalite.setType(Type.valueOf(rs.getString("type")));
        modalite.setCoefficient(rs.getFloat("coefficient"));

        // Load the ElementModule
        Long elementId = rs.getLong("element_module_id");
        ElementModuleDAO elementDAO = new ElementModuleDAO();
        elementDAO.findById(elementId).ifPresent(modalite::setElementModule);

        // Load the evaluations
        modalite.setEvaluations(findEvaluations(modalite.getId()));

        return modalite;
    }

    private List<Evaluation> findEvaluations(Long modaliteId) throws SQLException {
        String sql = "SELECT * FROM Evaluation WHERE modalite_evaluation_id = ?";
        List<Evaluation> evaluations = new ArrayList<>();
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, modaliteId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Evaluation evaluation = new Evaluation();
                evaluation.setId(rs.getLong("id"));
                evaluation.setNote(rs.getFloat("note"));
                evaluation.setAbsent(rs.getBoolean("absent"));
                evaluations.add(evaluation);
            }
        }
        return evaluations;
    }

    @Override
    public ModaliteEvaluation save(ModaliteEvaluation entity) throws SQLException {
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(getInsertQuery(), Statement.RETURN_GENERATED_KEYS)) {

            setInsertParameters(stmt, entity);
            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating modalite evaluation failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    entity.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Creating modalite evaluation failed, no ID obtained.");
                }
            }

            // Save associated evaluations if any
            if (entity.getEvaluations() != null && !entity.getEvaluations().isEmpty()) {
                EvaluationDAO evaluationDAO = new EvaluationDAO();
                for (Evaluation evaluation : entity.getEvaluations()) {
                    evaluation.setModaliteEvaluation(entity);
                    evaluationDAO.save(evaluation);
                }
            }

            return entity;
        }
    }

    @Override
    public ModaliteEvaluation update(ModaliteEvaluation entity) throws SQLException {
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(getUpdateQuery())) {

            setUpdateParameters(stmt, entity);
            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Updating modalite evaluation failed, no rows affected.");
            }

            // Update associated evaluations
            if (entity.getEvaluations() != null) {
                EvaluationDAO evaluationDAO = new EvaluationDAO();
                for (Evaluation evaluation : entity.getEvaluations()) {
                    if (evaluation.getId() == null) {
                        evaluation.setModaliteEvaluation(entity);
                        evaluationDAO.save(evaluation);
                    } else {
                        evaluationDAO.update(evaluation);
                    }
                }
            }

            return entity;
        }
    }

    @Override
    public void delete(Long id) throws SQLException {
        String sql = "DELETE FROM " + getTableName() + " WHERE id = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // First, delete associated evaluations
            EvaluationDAO evaluationDAO = new EvaluationDAO();
            List<Evaluation> evaluations = findEvaluations(id);
            for (Evaluation evaluation : evaluations) {
                evaluationDAO.delete(evaluation.getId());
            }

            // Then delete the modalite evaluation
            stmt.setLong(1, id);
            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Deleting modalite evaluation failed, no rows affected.");
            }
        }
    }

    @Override
    public Optional<ModaliteEvaluation> findById(Long id) throws SQLException {
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

    public List<ModaliteEvaluation> findByElementModuleId(Long elementModuleId) throws SQLException {
        String sql = "SELECT * FROM " + getTableName() + " WHERE element_module_id = ?";
        List<ModaliteEvaluation> modalites = new ArrayList<>();

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, elementModuleId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                modalites.add(mapResultSetToEntity(rs));
            }
        }

        return modalites;
    }

    public List<ModaliteEvaluation> findAll() throws SQLException {
        String sql = "SELECT * FROM " + getTableName();
        List<ModaliteEvaluation> modalites = new ArrayList<>();

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                modalites.add(mapResultSetToEntity(rs));
            }
        }

        return modalites;
    }
}