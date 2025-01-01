package org.example.service;

import org.example.annotation.Component;
import org.example.dao.FiliereDAO;
import org.example.entity.Filiere;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component
public class FiliereService implements CrudService<Filiere> {
    private final FiliereDAO filiereDAO;

    public FiliereService(FiliereDAO filiereDAO) {
        this.filiereDAO = filiereDAO;
    }

    @Override
    public Filiere create(Filiere filiere) throws SQLException {
        return filiereDAO.save(filiere);
    }

    @Override
    public Filiere update(Long id, Filiere filiere) throws SQLException {
        return filiereDAO.update(filiere);
    }

    @Override
    public void delete(Long id) throws SQLException {
        filiereDAO.delete(id);
    }

    @Override
    public Optional<Filiere> getById(Long id) throws SQLException {
        return filiereDAO.findById(id);
    }

    @Override
    public List<Filiere> getAll() throws SQLException {
        return filiereDAO.findAll();
    }

    public Optional<Filiere> getFiliereByCode(String code) throws SQLException {
        return filiereDAO.findByCode(code);
    }
}
