package org.example.dao;

import org.example.annotation.Component;
import org.example.entity.ElementModule;
import org.example.entity.ModaliteEvaluation;
import org.example.entity.Type;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ElementModuleDAO extends AbstractDAO<ElementModule> {
    @Override
    protected String getTableName() {
        return "ElementModule";
    }

    @Override
    protected String getInsertQuery() {
        return "INSERT INTO ElementModule (nom, coefficient, module_id, professeur_id) VALUES (?, ?, ?, ?)";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE ElementModule SET nom = ?, coefficient = ?, module_id = ?, professeur_id = ? WHERE id = ?";
    }

    @Override
    protected void setInsertParameters(PreparedStatement stmt, ElementModule entity) throws SQLException {
        stmt.setString(1, entity.getNom());
        stmt.setFloat(2, entity.getCoefficient());
        stmt.setLong(3, entity.getModule().getId());
        if (entity.getProfesseur() != null) {
            stmt.setLong(4, entity.getProfesseur().getId());
        } else {
            stmt.setNull(4, Types.BIGINT);
        }
    }

    @Override
    protected void setUpdateParameters(PreparedStatement stmt, ElementModule entity) throws SQLException {
        stmt.setString(1, entity.getNom());
        stmt.setFloat(2, entity.getCoefficient());
        stmt.setLong(3, entity.getModule().getId());
        if (entity.getProfesseur() != null) {
            stmt.setLong(4, entity.getProfesseur().getId());
        } else {
            stmt.setNull(4, Types.BIGINT);
        }
        stmt.setLong(5, entity.getId());
    }

    @Override
    protected ElementModule mapResultSetToEntity(ResultSet rs) throws SQLException {
        ElementModule element = new ElementModule();
        element.setId(rs.getLong("id"));
        element.setNom(rs.getString("nom"));
        element.setCoefficient(rs.getFloat("coefficient"));

        // Charger le module
        Long moduleId = rs.getLong("module_id");
        ModuleDAO moduleDAO = new ModuleDAO();
        moduleDAO.findById(moduleId).ifPresent(element::setModule);

        // Charger le professeur
        Long professeurId = rs.getLong("professeur_id");
        if (!rs.wasNull()) {
            ProfesseurDAO professeurDAO = new ProfesseurDAO();
            professeurDAO.findById(professeurId).ifPresent(element::setProfesseur);
        }

        // Charger les modalités d'évaluation
        element.setModaliteEvaluations(findModalites(element.getId()));

        return element;
    }

    private List<ModaliteEvaluation> findModalites(Long elementId) throws SQLException {
        String sql = "SELECT * FROM Modalite_Evaluation WHERE element_module_id = ?";
        List<ModaliteEvaluation> modalites = new ArrayList<>();
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, elementId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ModaliteEvaluation modalite = new ModaliteEvaluation();
                modalite.setId(rs.getLong("id"));
                modalite.setType(Type.valueOf(rs.getString("type")));
                modalite.setCoefficient(rs.getFloat("coefficient"));
                modalites.add(modalite);
            }
        }
        return modalites;
    }

    public List<ElementModule> findByProfesseurId(Long professeurId) throws SQLException {
        String sql = "SELECT * FROM ElementModule WHERE professeur_id = ?";
        List<ElementModule> elements = new ArrayList<>();
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, professeurId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                elements.add(mapResultSetToEntity(rs));
            }
        }
        return elements;
    }

    @Override
    public ElementModule save(ElementModule entity) throws SQLException {
        String sql = getInsertQuery();
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setInsertParameters(stmt, entity);
            stmt.executeUpdate();

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                entity.setId(generatedKeys.getLong(1));
                // Save associated modalités d'évaluation if they exist
                if (entity.getModaliteEvaluations() != null && !entity.getModaliteEvaluations().isEmpty()) {
                    saveModalites(entity.getId(), entity.getModaliteEvaluations());
                }
            }
            return entity;
        }
    }

    @Override
    public ElementModule update(ElementModule entity) throws SQLException {
        String sql = getUpdateQuery();
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            setUpdateParameters(stmt, entity);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Update failed, no rows affected.");
            }

            // Update associated modalités d'évaluation
            if (entity.getModaliteEvaluations() != null) {
                // First delete existing modalités
                deleteModalites(entity.getId());
                // Then save new ones
                saveModalites(entity.getId(), entity.getModaliteEvaluations());
            }

            return entity;
        }
    }

    @Override
    public void delete(Long id) throws SQLException {
        // First delete associated modalités d'évaluation
        deleteModalites(id);

        // Then delete the element module
        String sql = "DELETE FROM ElementModule WHERE id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Delete failed, no rows affected.");
            }
        }
    }

    private void deleteModalites(Long elementModuleId) throws SQLException {
        String sql = "DELETE FROM Modalite_Evaluation WHERE element_module_id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, elementModuleId);
            stmt.executeUpdate();
        }
    }

    private void saveModalites(Long elementModuleId, List<ModaliteEvaluation> modalites) throws SQLException {
        String sql = "INSERT INTO Modalite_Evaluation (type, coefficient, element_module_id) VALUES (?, ?, ?)";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            for (ModaliteEvaluation modalite : modalites) {
                stmt.setString(1, modalite.getType().name());
                stmt.setFloat(2, modalite.getCoefficient());
                stmt.setLong(3, elementModuleId);
                stmt.executeUpdate();

                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    modalite.setId(generatedKeys.getLong(1));
                }
            }
        }
    }

    @Override
    public Optional<ElementModule> findById(Long id) throws SQLException {
        String sql = "SELECT * FROM ElementModule WHERE id = ?";
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