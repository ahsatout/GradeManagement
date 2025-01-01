package org.example.service;

import org.example.annotation.Component;
import org.example.dao.EtudiantDAO;
import org.example.entity.Etudiant;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component
public class EtudiantService implements CrudService<Etudiant> {
    private final EtudiantDAO etudiantDAO;

    // Constructor with dependency injection
    public EtudiantService(EtudiantDAO etudiantDAO) {
        this.etudiantDAO = etudiantDAO;
    }

    @Override
    public Etudiant create(Etudiant etudiant) throws SQLException {
        return etudiantDAO.save(etudiant);
    }

    @Override
    public Etudiant update(Long id, Etudiant etudiant) throws SQLException {
        Optional<Etudiant> existingEtudiant = etudiantDAO.findById(id);
        if (existingEtudiant.isPresent()) {
            return etudiantDAO.update(etudiant);
        } else {
            throw new IllegalArgumentException("Etudiant not found with ID: " + id);
        }
    }

    @Override
    public void delete(Long id) throws SQLException {
        etudiantDAO.delete(id);
    }

    @Override
    public Optional<Etudiant> getById(Long id) throws SQLException {
        return etudiantDAO.findById(id);
    }

    @Override
    public List<Etudiant> getAll() throws SQLException {
        return etudiantDAO.findAll();
    }

    // Specific methods for Etudiant service

    public Optional<Etudiant> getEtudiantByCNE(String cne) throws SQLException {
        return etudiantDAO.findByCNE(cne);
    }
}
