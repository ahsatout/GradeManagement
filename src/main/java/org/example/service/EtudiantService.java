package org.example.service;

import org.example.dao.EtudiantDAO;
import org.example.entity.Etudiant;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class EtudiantService {
    private final EtudiantDAO etudiantDAO;

    // Constructor with dependency injection
    public EtudiantService(EtudiantDAO etudiantDAO) {
        this.etudiantDAO = etudiantDAO;
    }

    // Save a new Etudiant
    public Etudiant saveEtudiant(Etudiant etudiant) throws SQLException {
        return etudiantDAO.save(etudiant);
    }

    // Update an existing Etudiant
    public Etudiant updateEtudiant(Etudiant etudiant) throws SQLException {
        return etudiantDAO.update(etudiant);
    }

    // Delete an Etudiant by ID
    public void deleteEtudiant(Long id) throws SQLException {
        etudiantDAO.delete(id);
    }

    // Find an Etudiant by ID
    public Optional<Etudiant> getEtudiantById(Long id) throws SQLException {
        return etudiantDAO.findById(id);
    }

    // Find an Etudiant by CNE
    public Optional<Etudiant> getEtudiantByCNE(String cne) throws SQLException {
        return etudiantDAO.findByCNE(cne);
    }

    // Get all Etudiants
    public List<Etudiant> getAllEtudiants() throws SQLException {
        return etudiantDAO.findAll();
    }
}
