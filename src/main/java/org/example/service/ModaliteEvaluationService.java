package org.example.service;

import org.example.dao.EvaluationDAO;
import org.example.dao.ModaliteEvaluationDAO;
import org.example.entity.ModaliteEvaluation;
import org.example.entity.Evaluation;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ModaliteEvaluationService {

    private final ModaliteEvaluationDAO modaliteEvaluationDAO;

    public ModaliteEvaluationService() {
        this.modaliteEvaluationDAO = new ModaliteEvaluationDAO();
    }

    // Méthode pour sauvegarder une ModaliteEvaluation
    public ModaliteEvaluation saveModaliteEvaluation(ModaliteEvaluation modaliteEvaluation) throws SQLException {
        // Logique métier avant d'enregistrer
        // Exemple : vérifier si le coefficient est positif
        if (modaliteEvaluation.getCoefficient() <= 0) {
            throw new IllegalArgumentException("Le coefficient doit être positif.");
        }

        // Sauvegarde dans la base de données via le DAO
        return modaliteEvaluationDAO.save(modaliteEvaluation);
    }

    // Méthode pour mettre à jour une ModaliteEvaluation
    public ModaliteEvaluation updateModaliteEvaluation(ModaliteEvaluation modaliteEvaluation) throws SQLException {
        // Logique métier avant de mettre à jour
        if (modaliteEvaluation.getCoefficient() <= 0) {
            throw new IllegalArgumentException("Le coefficient doit être positif.");
        }

        return modaliteEvaluationDAO.update(modaliteEvaluation);
    }

    // Méthode pour supprimer une ModaliteEvaluation
    public void deleteModaliteEvaluation(Long id) throws SQLException {
        modaliteEvaluationDAO.delete(id);
    }

    // Méthode pour récupérer une ModaliteEvaluation par son ID
    public Optional<ModaliteEvaluation> getModaliteEvaluationById(Long id) throws SQLException {
        return modaliteEvaluationDAO.findById(id);
    }

    // Méthode pour récupérer toutes les ModaliteEvaluations
    public List<ModaliteEvaluation> getAllModaliteEvaluations() throws SQLException {
        return modaliteEvaluationDAO.findAll();
    }

    // Méthode pour récupérer toutes les ModaliteEvaluations d'un module
    public List<ModaliteEvaluation> getModaliteEvaluationsByElementModuleId(Long elementModuleId) throws SQLException {
        return modaliteEvaluationDAO.findByElementModuleId(elementModuleId);
    }

    // Méthode pour ajouter une évaluation à une ModaliteEvaluation
    public void addEvaluationToModaliteEvaluation(Long modaliteId, Evaluation evaluation) throws SQLException {
        Optional<ModaliteEvaluation> modaliteOpt = modaliteEvaluationDAO.findById(modaliteId);
        if (modaliteOpt.isPresent()) {
            ModaliteEvaluation modalite = modaliteOpt.get();
            evaluation.setModaliteEvaluation(modalite);
            // Ajouter l'évaluation via le DAO
            new EvaluationDAO().save(evaluation);
        } else {
            throw new IllegalArgumentException("Modalité d'évaluation introuvable.");
        }
    }

    // Méthode pour récupérer toutes les évaluations d'une ModaliteEvaluation
    public List<Evaluation> getEvaluationsForModalite(Long modaliteId) throws SQLException {
        Optional<ModaliteEvaluation> modaliteOpt = modaliteEvaluationDAO.findById(modaliteId);
        if (modaliteOpt.isPresent()) {
            return modaliteOpt.get().getEvaluations();
        } else {
            throw new IllegalArgumentException("Modalité d'évaluation introuvable.");
        }
    }
}
