package org.example.service;

import org.example.dao.ProfesseurDAO;
import org.example.entity.ElementModule;
import org.example.entity.Professeur;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ProfesseurService {

    private final ProfesseurDAO professeurDAO;

    // Constructor with Dependency Injection
    public ProfesseurService(ProfesseurDAO professeurDAO) {
        this.professeurDAO = professeurDAO;
    }

    public Professeur addProfesseur(Professeur professeur) throws SQLException {
        return professeurDAO.save(professeur);
    }

    public Professeur updateProfesseur(Long id, Professeur updatedProfesseur) throws SQLException {
        Optional<Professeur> existingProfesseur = professeurDAO.findById(id);
        if (existingProfesseur.isPresent()) {
            Professeur professeur = existingProfesseur.get();
            professeur.setCode(updatedProfesseur.getCode());
            professeur.setNom(updatedProfesseur.getNom());
            professeur.setPrenom(updatedProfesseur.getPrenom());
            professeur.setSpecialite(updatedProfesseur.getSpecialite());
            professeur.setUtilisateur(updatedProfesseur.getUtilisateur());
            return professeurDAO.update(professeur);
        } else {
            throw new SQLException("Professeur not found with id: " + id);
        }
    }

    public void deleteProfesseur(Long id) throws SQLException {
        professeurDAO.delete(id);
    }

    public Optional<Professeur> getProfesseurById(Long id) throws SQLException {
        return professeurDAO.findById(id);
    }

    public List<Professeur> getAllProfesseurs() throws SQLException {
        return professeurDAO.findAll();
    }

    public void addElementModuleToProfesseur(Long professeurId, ElementModule elementModule) throws SQLException {
        Optional<Professeur> professeur = professeurDAO.findById(professeurId);
        if (professeur.isPresent()) {
            Professeur existingProfesseur = professeur.get();
            List<ElementModule> elementModules = existingProfesseur.getElementModules();
            elementModules.add(elementModule);
            existingProfesseur.setElementModules(elementModules);
            professeurDAO.update(existingProfesseur);
        } else {
            throw new SQLException("Professeur not found with id: " + professeurId);
        }
    }

    public void removeElementModuleFromProfesseur(Long professeurId, ElementModule elementModule) throws SQLException {
        Optional<Professeur> professeur = professeurDAO.findById(professeurId);
        if (professeur.isPresent()) {
            Professeur existingProfesseur = professeur.get();
            List<ElementModule> elementModules = existingProfesseur.getElementModules();
            elementModules.remove(elementModule);
            existingProfesseur.setElementModules(elementModules);
            professeurDAO.update(existingProfesseur);
        } else {
            throw new SQLException("Professeur not found with id: " + professeurId);
        }
    }
}
