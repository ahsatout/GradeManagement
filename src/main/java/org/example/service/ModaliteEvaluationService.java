package org.example.service;

import org.example.annotation.Component;
import org.example.dao.ModaliteEvaluationDAO;
import org.example.entity.ModaliteEvaluation;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component
public class ModaliteEvaluationService implements CrudService<ModaliteEvaluation> {
    private final ModaliteEvaluationDAO modaliteEvaluationDAO;

    public ModaliteEvaluationService(ModaliteEvaluationDAO modaliteEvaluationDAO) {
        this.modaliteEvaluationDAO = modaliteEvaluationDAO;
    }

    @Override
    public ModaliteEvaluation create(ModaliteEvaluation modaliteEvaluation) throws SQLException {
        return modaliteEvaluationDAO.save(modaliteEvaluation);
    }

    @Override
    public ModaliteEvaluation update(Long id, ModaliteEvaluation modaliteEvaluation) throws SQLException {
        return modaliteEvaluationDAO.update(modaliteEvaluation);
    }

    @Override
    public void delete(Long id) throws SQLException {
        modaliteEvaluationDAO.delete(id);
    }

    @Override
    public Optional<ModaliteEvaluation> getById(Long id) throws SQLException {
        return modaliteEvaluationDAO.findById(id);
    }

    @Override
    public List<ModaliteEvaluation> getAll() throws SQLException {
        return modaliteEvaluationDAO.findAll();
    }

    public List<ModaliteEvaluation> getModaliteEvaluationsByElementModuleId(Long elementModuleId) throws SQLException {
        return modaliteEvaluationDAO.findByElementModuleId(elementModuleId);
    }
}
