package org.example.service;

import org.example.annotation.Component;
import org.example.dao.EvaluationDAO;
import org.example.entity.Evaluation;

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

    public List<Evaluation> getEvaluationsByEtudiant(Long etudiantId) throws SQLException {
        return evaluationDAO.findByEtudiant(etudiantId);
    }

    public List<Evaluation> getEvaluationsByModaliteEvaluation(Long modaliteId) throws SQLException {
        return evaluationDAO.findByModaliteEvaluation(modaliteId);
    }

    public float calculateAverageNoteByEtudiant(Long etudiantId) throws SQLException {
        return evaluationDAO.calculateAverageNoteByEtudiant(etudiantId);
    }
}
