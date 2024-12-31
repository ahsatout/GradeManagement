package org.example;

import org.example.entity.Role;
import org.example.entity.Utilisateur;
import org.example.service.UtilisateurService;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class App 
{


    public static void main( String[] args ) throws SQLException {

        UtilisateurService utilisateurService = new UtilisateurService();

        try {
            // Création d'un nouvel utilisateur
            System.out.println("Création d'un utilisateur ADMIN...");
            Utilisateur admin = utilisateurService.createUtilisateur("Nasri", "password123", Role.PROFESSEUR);
            System.out.println("Utilisateur créé : " + admin);

            // Création d'un autre utilisateur
            System.out.println("Création d'un utilisateur PROFESSOR...");
            Utilisateur professor = utilisateurService.createUtilisateur("professor5", "pass456", Role.PROFESSEUR);
            System.out.println("Utilisateur créé : " + professor);

            // Récupération par ID
            System.out.println("\nRécupération de l'utilisateur avec ID: " + admin.getId());
            Optional<Utilisateur> retrievedAdmin = utilisateurService.getUtilisateurById(admin.getId());
            retrievedAdmin.ifPresentOrElse(
                    user -> System.out.println("Utilisateur récupéré : " + user),
                    () -> System.out.println("Utilisateur non trouvé.")
            );

            // Mise à jour d'un utilisateur
            System.out.println("\nMise à jour de l'utilisateur PROFESSOR...");
            Utilisateur updatedProfessor = utilisateurService.updateUtilisateur(professor.getId(), "professor_updated", "newpass789", Role.ADMIN);
            System.out.println("Utilisateur mis à jour : " + updatedProfessor);

            // Récupération de tous les utilisateurs
            System.out.println("\nListe de tous les utilisateurs :");
            List<Utilisateur> utilisateurs = utilisateurService.getAllUtilisateurs();
            utilisateurs.forEach(System.out::println);

            // Suppression d'un utilisateur
            System.out.println("\nSuppression de l'utilisateur ADMIN...");
            utilisateurService.deleteUtilisateur(admin.getId());
            System.out.println("Utilisateur supprimé.");

            // Vérification de la suppression
            System.out.println("\nVérification de l'utilisateur supprimé...");
            Optional<Utilisateur> deletedUser = utilisateurService.getUtilisateurById(admin.getId());
            System.out.println(deletedUser.isPresent() ? "Utilisateur trouvé : " + deletedUser.get() : "Utilisateur non trouvé.");

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Erreur lors des opérations sur les utilisateurs : " + e.getMessage());
        }
    }
    }

