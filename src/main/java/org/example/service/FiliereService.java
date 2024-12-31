package org.example.service;

import org.example.dao.FiliereDAO;
import org.example.entity.Filiere;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class FiliereService {
    private final FiliereDAO filiereDAO;

    public FiliereService(FiliereDAO filiereDAO) {
        this.filiereDAO = filiereDAO;
    }

    /**
     * Crée une nouvelle filière.
     *
     * @param filiere La filière à ajouter.
     * @return La filière ajoutée avec son ID généré.
     * @throws SQLException Si une erreur se produit lors de l'insertion.
     */
    public Filiere createFiliere(Filiere filiere) throws SQLException {
        return filiereDAO.save(filiere);
    }

    /**
     * Met à jour une filière existante.
     *
     * @param filiere La filière avec les modifications.
     * @return La filière mise à jour.
     * @throws SQLException Si une erreur se produit lors de la mise à jour.
     */
    public Filiere updateFiliere(Filiere filiere) throws SQLException {
        return filiereDAO.update(filiere);
    }

    /**
     * Supprime une filière par son ID.
     *
     * @param id L'ID de la filière à supprimer.
     * @throws SQLException Si une erreur se produit lors de la suppression.
     */
    public void deleteFiliere(Long id) throws SQLException {
        filiereDAO.delete(id);
    }

    /**
     * Récupère une filière par son ID.
     *
     * @param id L'ID de la filière.
     * @return Une filière si trouvée, sinon un `Optional.empty`.
     * @throws SQLException Si une erreur se produit lors de la recherche.
     */
    public Optional<Filiere> getFiliereById(Long id) throws SQLException {
        return filiereDAO.findById(id);
    }

    /**
     * Récupère une filière par son code.
     *
     * @param code Le code de la filière.
     * @return Une filière si trouvée, sinon un `Optional.empty`.
     * @throws SQLException Si une erreur se produit lors de la recherche.
     */
    public Optional<Filiere> getFiliereByCode(String code) throws SQLException {
        return filiereDAO.findByCode(code);
    }

    /**
     * Récupère toutes les filières.
     *
     * @return La liste de toutes les filières.
     * @throws SQLException Si une erreur se produit lors de la recherche.
     */
    public List<Filiere> getAllFilieres() throws SQLException {
        return filiereDAO.findAll();
    }
}
