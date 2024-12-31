package org.example.service;

import org.example.dao.EvaluationDAO;
import org.example.entity.Evaluation;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class EvaluationService {
    private final EvaluationDAO evaluationDAO;

    // Constructor with dependency injection
    public EvaluationService(EvaluationDAO evaluationDAO) {
        this.evaluationDAO = evaluationDAO;
    }

    public Evaluation saveEvaluation(Evaluation evaluation) throws SQLException {
        return evaluationDAO.save(evaluation);
    }

    public Evaluation updateEvaluation(Evaluation evaluation) throws SQLException {
        return evaluationDAO.update(evaluation);
    }

    public void deleteEvaluation(Long id) throws SQLException {
        evaluationDAO.delete(id);
    }

    public Optional<Evaluation> getEvaluationById(Long id) throws SQLException {
        return evaluationDAO.findById(id);
    }

    public List<Evaluation> getEvaluationsByEtudiant(Long etudiantId) throws SQLException {
        return evaluationDAO.findByEtudiant(etudiantId);
    }

    public List<Evaluation> getEvaluationsByModaliteEvaluation(Long modaliteId) throws SQLException {
        return evaluationDAO.findByModaliteEvaluation(modaliteId);
    }

    public List<Evaluation> getAllEvaluations() throws SQLException {
        return evaluationDAO.findAll();
    }

    public float calculateAverageNoteByEtudiant(Long etudiantId) throws SQLException {
        return evaluationDAO.calculateAverageNoteByEtudiant(etudiantId);
    }
}
