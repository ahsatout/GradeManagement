package org.example.service;

import org.example.dao.ElementModuleDAO;
import org.example.entity.ElementModule;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ElementModuleService {

    private final ElementModuleDAO elementModuleDAO;

    // Constructor with dependency injection
    public ElementModuleService(ElementModuleDAO elementModuleDAO) {
        this.elementModuleDAO = elementModuleDAO;
    }

    // Save a new ElementModule
    public ElementModule saveElementModule(ElementModule elementModule) throws SQLException {
        return elementModuleDAO.save(elementModule);
    }

    // Update an existing ElementModule
    public ElementModule updateElementModule(ElementModule elementModule) throws SQLException {
        return elementModuleDAO.update(elementModule);
    }

    // Delete an ElementModule by ID
    public void deleteElementModule(Long id) throws SQLException {
        elementModuleDAO.delete(id);
    }

    // Find an ElementModule by ID
    public Optional<ElementModule> getElementModuleById(Long id) throws SQLException {
        return elementModuleDAO.findById(id);
    }

    // Get all ElementModules
    public List<ElementModule> getAllElementModules() throws SQLException {
        return elementModuleDAO.findAll();
    }

    // Get ElementModules by Professeur ID
    public List<ElementModule> getElementModulesByProfesseurId(Long professeurId) throws SQLException {
        return elementModuleDAO.findByProfesseurId(professeurId);
    }
}