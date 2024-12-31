package org.example.service;

import org.example.annotation.Component;
import org.example.dao.UtilisateurDAO;
import org.example.entity.Utilisateur;
import org.example.entity.Role;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;


public class UtilisateurService {
    private final UtilisateurDAO utilisateurDAO;

    public UtilisateurService(UtilisateurDAO utilisateurDAO) {
        this.utilisateurDAO = utilisateurDAO;
    }

    public Utilisateur createUtilisateur(String login, String password, Role role) throws SQLException {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setLogin(login);
        utilisateur.setPassword(password);
        utilisateur.setRole(role);
        return utilisateurDAO.save(utilisateur);
    }

    public Utilisateur updateUtilisateur(Long id, String login, String password, Role role) throws SQLException {
        Optional<Utilisateur> optionalUtilisateur = utilisateurDAO.findById(id);
        if (optionalUtilisateur.isPresent()) {
            Utilisateur utilisateur = optionalUtilisateur.get();
            utilisateur.setLogin(login);
            utilisateur.setPassword(password);
            utilisateur.setRole(role);
            return utilisateurDAO.update(utilisateur);
        } else {
            throw new IllegalArgumentException("Utilisateur non trouvé avec l'ID: " + id);
        }
    }

    public void deleteUtilisateur(Long id) throws SQLException {
        utilisateurDAO.delete(id);
    }

    public Optional<Utilisateur> getUtilisateurById(Long id) throws SQLException {
        return utilisateurDAO.findById(id);
    }

    public List<Utilisateur> getAllUtilisateurs() throws SQLException {
        return utilisateurDAO.findAll();
    }

    public Optional<Utilisateur> getUtilisateurByLogin(String login) throws SQLException {
        return utilisateurDAO.findByLogin(login);
    }
}
