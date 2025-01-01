package org.example.service;

import org.example.annotation.Component;
import org.example.dao.ElementModuleDAO;
import org.example.entity.ElementModule;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component
public class ElementModuleService implements CrudService<ElementModule> {

    private final ElementModuleDAO elementModuleDAO;

    // Constructor with dependency injection
    public ElementModuleService(ElementModuleDAO elementModuleDAO) {
        this.elementModuleDAO = elementModuleDAO;
    }

    @Override
    public ElementModule create(ElementModule elementModule) throws SQLException {
        return elementModuleDAO.save(elementModule);
    }

    @Override
    public ElementModule update(Long id, ElementModule elementModule) throws SQLException {
        Optional<ElementModule> existingElementModule = elementModuleDAO.findById(id);
        if (existingElementModule.isPresent()) {
            return elementModuleDAO.update(elementModule);
        } else {
            throw new IllegalArgumentException("ElementModule not found with ID: " + id);
        }
    }

    @Override
    public void delete(Long id) throws SQLException {
        elementModuleDAO.delete(id);
    }

    @Override
    public Optional<ElementModule> getById(Long id) throws SQLException {
        return elementModuleDAO.findById(id);
    }

    @Override
    public List<ElementModule> getAll() throws SQLException {
        return elementModuleDAO.findAll();
    }

    // Specific methods for ElementModule service

    public List<ElementModule> getElementModulesByProfesseurId(Long professeurId) throws SQLException {
        return elementModuleDAO.findByProfesseurId(professeurId);
    }
}
