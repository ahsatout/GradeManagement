package org.example.service;

import org.example.annotation.Component;
import org.example.dao.ModuleDAO;
import org.example.entity.Module;
import org.example.entity.Semestre;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component
public class ModuleService implements CrudService<Module> {
    private final ModuleDAO moduleDAO;

    public ModuleService(ModuleDAO moduleDAO) {
        this.moduleDAO = moduleDAO;
    }

    @Override
    public Module create(Module module) throws SQLException {
        return moduleDAO.save(module);
    }

    @Override
    public Module update(Long id, Module updatedModule) throws SQLException {
        Optional<Module> existingModule = moduleDAO.findById(id);
        if (existingModule.isPresent()) {
            updatedModule.setId(id);
            return moduleDAO.update(updatedModule);
        } else {
            throw new IllegalArgumentException("Module with ID " + id + " does not exist.");
        }
    }

    @Override
    public void delete(Long id) throws SQLException {
        Optional<Module> existingModule = moduleDAO.findById(id);
        if (existingModule.isPresent()) {
            moduleDAO.delete(id);
        } else {
            throw new IllegalArgumentException("Module with ID " + id + " does not exist.");
        }
    }

    @Override
    public Optional<Module> getById(Long id) throws SQLException {
        return moduleDAO.findById(id);
    }

    @Override
    public List<Module> getAll() throws SQLException {
        return moduleDAO.findAll();
    }

    public List<Module> getModulesBySemester(Semestre semestre) throws SQLException {
        return moduleDAO.findBySemestre(semestre);
    }
}
