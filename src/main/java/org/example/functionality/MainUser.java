package org.example.functionality;

import org.example.dao.*;
import org.example.entity.Filiere;
import org.example.entity.Professeur;
import org.example.service.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class MainUser {

    public static void main(String[] args) {

        ProfesseurDAO professeurDAO = new ProfesseurDAO();
        FiliereDAO filiereDAO = new FiliereDAO();
        ModuleDAO moduleDAO = new ModuleDAO();
        ElementModuleDAO elementModuleDAO = new ElementModuleDAO();
        ModaliteEvaluationDAO modaliteEvaluationDAO = new ModaliteEvaluationDAO();

        Scanner scanner = new Scanner(System.in);
        UtilisateurDAO utilisateurDAO = new UtilisateurDAO(professeurDAO);
        UtilisateurService utilisateurService = new UtilisateurService(utilisateurDAO);

        // Authentification avant d'afficher le menu
        if (!authenticate(scanner, utilisateurService)) {
            System.out.println("Authentification échouée. Programme terminé.");
            return;
        }

        ProfesseurService professeurService = new ProfesseurService(professeurDAO);
        FiliereService filiereService = new FiliereService(filiereDAO);
        ModuleService moduleService = new ModuleService(moduleDAO);
        ElementModuleService elementModuleService = new ElementModuleService(elementModuleDAO);
        ModaliteEvaluationService modaliteEvaluationService = new ModaliteEvaluationService(modaliteEvaluationDAO);

        int choice;
        do {
            System.out.println("\n--- Menu Administrateur ---");
            System.out.println("1. Gérer les professeurs");
            System.out.println("2. Gérer les filières");
            System.out.println("3. Gérer les modules");
            System.out.println("4. Gérer les modalités d'évaluation");
            System.out.println("5. Affecter des éléments aux professeurs");
            System.out.println("0. Quitter");
            System.out.print("Choix : ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    manageProfessors(professeurService, scanner);
                    break;
                case 2:
                    manageFilieres(filiereService, scanner);
                    break;
                case 0:
                    System.out.println("Au revoir !");
                    break;
                default:
                    System.out.println("Choix invalide !");
            }
        } while (choice != 0);

        scanner.close();
    }

    private static boolean authenticate(Scanner scanner, UtilisateurService utilisateurService) {
        System.out.println("--- Authentification ---");
        System.out.print("Identifiant : ");
        String inputUsername = scanner.nextLine().trim();
        System.out.print("Mot de passe : ");
        String inputPassword = scanner.nextLine().trim();


        try {
            boolean isAuthenticated = utilisateurService.isValidUser(inputUsername, inputPassword,"ADMIN");
            if (isAuthenticated) {
                System.out.println("Authentification réussie !");
                return true;
            } else {
                System.out.println("Identifiant ou mot de passe incorrect ou role non authorisé");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'authentification : " + e.getMessage());
            return false;
        }
    }

    private static void manageProfessors(ProfesseurService professeurService, Scanner scanner) {
        System.out.println("\n--- Gestion des Professeurs ---");
        System.out.println("1. Ajouter un professeur");
        System.out.println("2. Mettre à jour un professeur");
        System.out.println("3. Supprimer un professeur");
        System.out.println("4. Lister tous les professeurs");
        System.out.print("Choix : ");
        int choice = scanner.nextInt();

        try {
            switch (choice) {
                case 1:
                    addProfessor(professeurService, scanner);
                    break;
                case 2:
                    updateProfessor(professeurService, scanner);
                    break;
                case 3:
                    deleteProfessor(professeurService, scanner);
                    break;
                case 4:
                    listAllProfessors(professeurService);
                    break;
                default:
                    System.out.println("Choix invalide !");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la gestion des professeurs : " + e.getMessage());
        }
    }
    private static void addProfessor(ProfesseurService professeurService, Scanner scanner) {
        try {
            scanner.nextLine(); // Consommer la nouvelle ligne restante

            System.out.print("Code : ");
            String code = scanner.nextLine().trim();
            if (code.isEmpty()) {
                System.out.println("Le code ne peut pas être vide !");
                return;
            }

            System.out.print("Nom : ");
            String nom = scanner.nextLine().trim();
            if (nom.isEmpty()) {
                System.out.println("Le nom ne peut pas être vide !");
                return;
            }

            System.out.print("Prénom : ");
            String prenom = scanner.nextLine().trim();
            if (prenom.isEmpty()) {
                System.out.println("Le prénom ne peut pas être vide !");
                return;
            }

            System.out.print("Spécialité : ");
            String specialite = scanner.nextLine().trim();
            if (specialite.isEmpty()) {
                System.out.println("La spécialité ne peut pas être vide !");
                return;
            }

            // Création d'un nouvel objet Professeur avec les données fournies
            Professeur newProf = new Professeur(null,code, nom, prenom, specialite,null,null); // null pour l'ID auto-généré
            professeurService.create(newProf);

            System.out.println("Professeur ajouté avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du professeur : " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Entrée invalide : " + e.getMessage());
        }
    }

    private static void updateProfessor(ProfesseurService professeurService, Scanner scanner) throws SQLException {
        System.out.print("ID du professeur à mettre à jour : ");
        Long updateId = scanner.nextLong();
        scanner.nextLine(); // Consommer la nouvelle ligne restante
        System.out.print("Nouveau Code : ");
        String newCode = scanner.nextLine();
        System.out.print("Nouveau nom : ");
        String newNom = scanner.nextLine();
        System.out.print("Nouveau prénom : ");
        String newPrenom = scanner.nextLine();
        System.out.print("Nouvelle spécialité : ");
        String newSpecialite = scanner.nextLine();

        Professeur updatedProf = new Professeur(updateId,newCode,newNom, newPrenom, newSpecialite,null,null); // Remplacez par le constructeur correct
        professeurService.update(updateId, updatedProf);
        System.out.println("Professeur mis à jour avec succès.");
    }

    private static void deleteProfessor(ProfesseurService professeurService, Scanner scanner) throws SQLException {
        System.out.print("ID du professeur à supprimer : ");
        Long deleteId = scanner.nextLong();
        professeurService.delete(deleteId);
        System.out.println("Professeur supprimé avec succès.");
    }

    private static void listAllProfessors(ProfesseurService professeurService) throws SQLException {
        List<Professeur> professeurs = professeurService.getAll();
        if (professeurs.isEmpty()) {
            System.out.println("Aucun professeur trouvé.");
        } else {
            professeurs.forEach(System.out::println);
        }
    }


    ///////////////////////////////////////////////////////////////////////////////
    //     ------------------- gestion des filieres ------------------------    ///
    ///////////////////////////////////////////////////////////////////////////////


    private static void manageFilieres(FiliereService filiereService, Scanner scanner) {
        System.out.println("\n--- Gestion des Filières ---");
        System.out.println("1. Ajouter une filière");
        System.out.println("2. Mettre à jour une filière");
        System.out.println("3. Supprimer une filière");
        System.out.println("4. Lister toutes les filières");
        System.out.print("Choix : ");
        int choice = scanner.nextInt();

        try {
            switch (choice) {
                case 1:
                    addFiliere(filiereService, scanner);
                    break;

                case 2:
                    updateFiliere(filiereService, scanner);
                    break;

                case 3:
                    deleteFiliere(filiereService, scanner);
                    break;
                case 4:
                    listAllFilieres(filiereService);
                    break;
                default:
                    System.out.println("Choix invalide !");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la gestion des filières : " + e.getMessage());
        }}

    private static void addFiliere ( FiliereService filiereService, Scanner scanner) {
        try {
            scanner.nextLine(); // Consommer la nouvelle ligne restante

            System.out.print("Code de la filière : ");
            String code = scanner.nextLine().trim();
            if (code.isEmpty()) {
                System.out.println("Le code ne peut pas être vide !");
                return;
            }

            System.out.print("Nom de la filière : ");
            String nom = scanner.nextLine().trim();
            if (nom.isEmpty()) {
                System.out.println("Le nom ne peut pas être vide !");
                return;
            }

            Filiere newFiliere = new Filiere(null, code, nom,null,null); // null pour l'ID auto-généré
            filiereService.create(newFiliere);
            System.out.println("Filière ajoutée avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de la filière : " + e.getMessage());
        }
    }

    private static void updateFiliere(FiliereService filiereService, Scanner scanner) throws SQLException {
        System.out.print("ID de la filière à mettre à jour : ");
        Long updateId = scanner.nextLong();
        scanner.nextLine(); // Consommer la nouvelle ligne restante

        System.out.print("Nouveau code de la filière : ");
        String newCode = scanner.nextLine();
        System.out.print("Nouveau nom de la filière : ");
        String newNom = scanner.nextLine();

        Filiere updatedFiliere = new Filiere(updateId, newCode, newNom, null,null); // Remplacez par le constructeur correct
        filiereService.update(updateId, updatedFiliere);
        System.out.println("Filière mise à jour avec succès.");
    }

    private static void deleteFiliere(FiliereService filiereService, Scanner scanner) throws SQLException {
        System.out.print("ID de la filière à supprimer : ");
        Long deleteId = scanner.nextLong();
        filiereService.delete(deleteId);
        System.out.println("Filière supprimée avec succès.");
    }

    private static void listAllFilieres(FiliereService filiereService) throws SQLException {
        List<Filiere> filieres = filiereService.getAll();
        if (filieres.isEmpty()) {
            System.out.println("Aucune filière trouvée.");
        } else {
            filieres.forEach(System.out::println);
        }
    }







}
