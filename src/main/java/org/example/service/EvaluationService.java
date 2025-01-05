package org.example.service;

import org.example.annotation.Component;
import org.example.dao.EvaluationDAO;
import org.example.entity.Evaluation;
import org.example.entity.Professeur;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component
public class EvaluationService implements CrudService<Evaluation> {
    private final EvaluationDAO evaluationDAO;

    // Constructor with dependency injection
    public EvaluationService(EvaluationDAO evaluationDAO) {
        this.evaluationDAO = evaluationDAO;
    }

    @Override
    public Evaluation create(Evaluation evaluation) throws SQLException {
        return evaluationDAO.save(evaluation);
    }

    @Override
    public Evaluation update(Long id, Evaluation evaluation) throws SQLException {
        Optional<Evaluation> existingEvaluation = evaluationDAO.findById(id);
        if (existingEvaluation.isPresent()) {
            return evaluationDAO.update(evaluation);
        } else {
            throw new IllegalArgumentException("Evaluation not found with ID: " + id);
        }
    }

    @Override
    public void delete(Long id) throws SQLException {
        evaluationDAO.delete(id);
    }

    @Override
    public Optional<Evaluation> getById(Long id) throws SQLException {
        return evaluationDAO.findById(id);
    }

    @Override
    public List<Evaluation> getAll() throws SQLException {
        return evaluationDAO.findAll();
    }

    // Specific methods for Evaluation service

    // Method to retrieve evaluations by professor ID
    public List<Evaluation> getEvaluationsByProfesseurId(Long professeurId) throws SQLException {
        return evaluationDAO.findEvaluationsByProfesseurId(professeurId);
    }

    // Method to validate an evaluation
    public boolean validateEvaluation(Long evaluationId) throws SQLException {
        return evaluationDAO.validateEvaluation(evaluationId);
    }

    // Method to calculate the average grade of an evaluation module
    public float calculateAverage(Long elementModuleId) throws SQLException {
        return evaluationDAO.calculateAverage(elementModuleId);
    }

    // Method to check if the note is valid (between 0 and 20)
    public boolean isValidNote(Float note) {
        return evaluationDAO.isValidNote(note);
    }

    // Method to check if all notes have been entered for a module
    public boolean areAllNotesEntered(Long elementModuleId) throws SQLException {
        return evaluationDAO.areAllNotesEntered(elementModuleId);
    }

    // Method to check if there are any 0 or 20 in the evaluations for a module
    public boolean containsZeroOrTwenty(Long elementModuleId) throws SQLException {
        return evaluationDAO.containsZeroOrTwenty(elementModuleId);
    }

    // Method to export the evaluations' notes to a specific file format (e.g., PDF or Excel)
    public void exportEvaluationsToFile(Long elementModuleId, String format) throws SQLException {
        evaluationDAO.exportNotesToFile(elementModuleId, format);
    }
}
