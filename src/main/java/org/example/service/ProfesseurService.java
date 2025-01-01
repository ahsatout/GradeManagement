package org.example.service;

import org.example.annotation.Component;
import org.example.dao.ProfesseurDAO;
import org.example.entity.ElementModule;
import org.example.entity.Professeur;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component
public class ProfesseurService implements CrudService<Professeur> {
    private final ProfesseurDAO professeurDAO;

    public ProfesseurService(ProfesseurDAO professeurDAO) {
        this.professeurDAO = professeurDAO;
    }

    @Override
    public Professeur create(Professeur professeur) throws SQLException {
        return professeurDAO.save(professeur);
    }

    @Override
    public Professeur update(Long id, Professeur professeur) throws SQLException {
        Optional<Professeur> existingProfesseur = professeurDAO.findById(id);
        if (existingProfesseur.isPresent()) {
            return professeurDAO.update(professeur);
        } else {
            throw new SQLException("Professeur non trouvé avec l'ID: " + id);
        }
    }

    @Override
    public void delete(Long id) throws SQLException {
        professeurDAO.delete(id);
    }

    @Override
    public Optional<Professeur> getById(Long id) throws SQLException {
        return professeurDAO.findById(id);
    }

    @Override
    public List<Professeur> getAll() throws SQLException {
        return professeurDAO.findAll();
    }

    public void addElementModuleToProfesseur(Long professeurId, ElementModule elementModule) throws SQLException {
        Optional<Professeur> professeur = professeurDAO.findById(professeurId);
        if (professeur.isPresent()) {
            Professeur existingProfesseur = professeur.get();
            existingProfesseur.getElementModules().add(elementModule);
            professeurDAO.update(existingProfesseur);
        }
    }

    public void removeElementModuleFromProfesseur(Long professeurId, ElementModule elementModule) throws SQLException {
        Optional<Professeur> professeur = professeurDAO.findById(professeurId);
        if (professeur.isPresent()) {
            Professeur existingProfesseur = professeur.get();
            existingProfesseur.getElementModules().remove(elementModule);
            professeurDAO.update(existingProfesseur);
        }
    }
}
