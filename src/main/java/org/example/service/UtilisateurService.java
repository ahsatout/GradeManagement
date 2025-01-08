package org.example.service;

import org.example.annotation.Component;
import org.example.dao.UtilisateurDAO;
import org.example.entity.Utilisateur;
import org.example.entity.Role;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component
public class UtilisateurService implements CrudService<Utilisateur> {
    private final UtilisateurDAO utilisateurDAO;

    public UtilisateurService(UtilisateurDAO utilisateurDAO) {
        this.utilisateurDAO = utilisateurDAO;
    }

    @Override
    public Utilisateur create(Utilisateur utilisateur) throws SQLException {
        return utilisateurDAO.save(utilisateur);
    }

    @Override
    public Utilisateur update(Long id, Utilisateur utilisateur) throws SQLException {
        Optional<Utilisateur> existingUtilisateur = utilisateurDAO.findById(id);
        if (existingUtilisateur.isPresent()) {
            return utilisateurDAO.update(utilisateur);
        } else {
            throw new IllegalArgumentException("Utilisateur non trouvé avec l'ID: " + id);
        }
    }

    @Override
    public void delete(Long id) throws SQLException {
        utilisateurDAO.delete(id);
    }

    @Override
    public Optional<Utilisateur> getById(Long id) throws SQLException {
        return utilisateurDAO.findById(id);
    }

    @Override
    public List<Utilisateur> getAll() throws SQLException {
        return utilisateurDAO.findAll();
    }

    public Optional<Utilisateur> getUtilisateurByLogin(String login) throws SQLException {
        return utilisateurDAO.findByLogin(login);
    }

    public boolean isValidUser(String username, String password,String role) throws SQLException {
        return utilisateurDAO.isValidUser(username, password, role);
    }

    public void updatePassword(Long id, String newPassword) throws SQLException {
        Optional<Utilisateur> utilisateur = utilisateurDAO.findById(id);
        if (utilisateur.isPresent()) {
            utilisateurDAO.updatePassword(id, newPassword);
        } else {
            throw new IllegalArgumentException("Utilisateur non trouvé avec l'ID: " + id);
        }
    }
}