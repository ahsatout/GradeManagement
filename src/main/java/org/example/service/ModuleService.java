package org.example.service;

import org.example.dao.ModuleDAO;
import org.example.entity.Module;
import org.example.entity.Semestre;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ModuleService {
    private final ModuleDAO moduleDAO;

    // Constructor injection
    public ModuleService(ModuleDAO moduleDAO) {
        this.moduleDAO = moduleDAO;
    }

    // Add a new module
    public Module addModule(Module module) throws SQLException {
        return moduleDAO.save(module);
    }

    // Get a module by its ID
    public Optional<Module> getModuleById(Long id) throws SQLException {
        return moduleDAO.findById(id);
    }

    // Get a module by its code
    public Optional<Module> getModuleByCode(String code) throws SQLException {
        return moduleDAO.findByCode(code);
    }

    // Get all modules by semester
    public List<Module> getModulesBySemester(Semestre semestre) throws SQLException {
        return moduleDAO.findBySemestre(semestre);
    }

    // Update an existing module
    public Module updateModule(Long id, Module updatedModule) throws SQLException {
        Optional<Module> existingModule = moduleDAO.findById(id);
        if (existingModule.isPresent()) {
            updatedModule.setId(id); // Ensure the ID is set for the update
            return moduleDAO.update(updatedModule);
        } else {
            throw new IllegalArgumentException("Module with ID " + id + " does not exist.");
        }
    }

    // Delete a module by its ID
    public void deleteModule(Long id) throws SQLException {
        Optional<Module> existingModule = moduleDAO.findById(id);
        if (existingModule.isPresent()) {
            moduleDAO.delete(id);
        } else {
            throw new IllegalArgumentException("Module with ID " + id + " does not exist.");
        }
    }
}
