package org.example.functionality;

import org.example.dao.*;
import org.example.entity.*;
import org.example.entity.Module;
import org.example.service.*;

import java.io.Console;
import java.sql.SQLException;
import java.util.*;

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

        while (true) {
            if (!authenticate(scanner, utilisateurService)) {
                System.out.println("Trois tentatives échouées.");
                System.out.println("Voulez-vous changer votre mot de passe ? (oui/non)");
                String response = scanner.nextLine().trim().toLowerCase();

                if (response.equals("oui")) {
                    changePassword(scanner, utilisateurService);
                } else {
                    System.out.println("Accès refusé. Programme terminé.");
                    break;
                }
            } else {
                ProfesseurService professeurService = new ProfesseurService(professeurDAO);
                FiliereService filiereService = new FiliereService(filiereDAO);
                ModuleService moduleService = new ModuleService(moduleDAO);
                ElementModuleService elementModuleService = new ElementModuleService(elementModuleDAO);
                ModaliteEvaluationService modaliteEvaluationService = new ModaliteEvaluationService(modaliteEvaluationDAO);

                boolean isLoggedIn = true;

                while (isLoggedIn) {
                    System.out.println("\n--- Menu Administrateur ---");
                    System.out.println("1. Gérer les professeurs");
                    System.out.println("2. Gérer les filières");
                    System.out.println("3. Gérer les modules");
                    System.out.println("4. Gérer les éléments");
                    System.out.println("5. Gérer les modalités d'évaluation");
                    System.out.println("6. Gérer les utilisateurs");
                    System.out.println("7. Déconnexion");
                    System.out.println("0. Quitter");
                    System.out.print("Choix : ");
                    int choice = scanner.nextInt();
                    scanner.nextLine();

                    switch (choice) {
                        case 1:
                            manageProfessors(professeurService, utilisateurService, scanner);
                            break;
                        case 2:
                            manageFilieres(filiereService, scanner);
                            break;
                        case 3:
                            manageModules(moduleService, filiereDAO, scanner);
                            break;
                        case 4:
                            manageElementModule(elementModuleService, moduleService, professeurService, scanner);
                            break;
                        case 5:
                            manageModaliteEvaluation(modaliteEvaluationService, elementModuleService, scanner);
                            manageUser(utilisateurService, scanner);
                            break;
                        case 6:
                            manageUser(utilisateurService, scanner);
                            break;
                        case 7:
                            System.out.println("Déconnexion en cours...");
                            isLoggedIn = false;
                            break;
                        case 0:
                            System.out.println("Au revoir !");
                            scanner.close();
                            return;
                        default:
                            System.out.println("Choix invalide !");
                    }
                }
            }
        }
    }

    private static boolean authenticate(Scanner scanner, UtilisateurService utilisateurService) {
        int attempts = 0;

        while (attempts < 3) {
            System.out.println("--- Authentification ---");
            System.out.print("Identifiant : ");
            String inputUsername = scanner.nextLine().trim();
            String inputPassword = readPassword("Mot de passe : ");

            try {
                boolean isAuthenticated = utilisateurService.isValidUser(inputUsername, inputPassword, "ADMIN");
                if (isAuthenticated) {
                    System.out.println("Authentification réussie !");
                    return true;
                } else {
                    System.out.println("Identifiant ou mot de passe incorrect.");
                    attempts++;
                }
            } catch (SQLException e) {
                System.out.println("Erreur lors de l'authentification : " + e.getMessage());
                return false;
            }
        }
        return false;
    }

    private static void changePassword(Scanner scanner, UtilisateurService utilisateurService) {
        System.out.println("--- Changer le mot de passe ---");
        System.out.print("Identifiant : ");
        String username = scanner.nextLine().trim();

        try {
            String oldPassword = readPassword("Ancien mot de passe : ");
            if (utilisateurService.isValidUser(username, oldPassword, "ADMIN")) {
                String newPassword = readPassword("Nouveau mot de passe : ");
                String confirmPassword = readPassword("Confirmer le mot de passe : ");

                if (newPassword.equals(confirmPassword)) {
                    Optional<Utilisateur> utilisateurOpt = utilisateurService.getUtilisateurByLogin(username);
                    if (utilisateurOpt.isPresent()) {
                        Utilisateur utilisateur = utilisateurOpt.get();
                        utilisateurService.updatePassword(utilisateur.getId(), newPassword);
                        System.out.println("Mot de passe changé avec succès !");
                    } else {
                        System.out.println("Utilisateur introuvable.");
                    }
                } else {
                    System.out.println("Les mots de passe ne correspondent pas.");
                }
            } else {
                System.out.println("Ancien mot de passe incorrect.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors du changement de mot de passe : " + e.getMessage());
        }
    }

    private static String readPassword(String prompt) {
        Console console = System.console();
        if (console != null) {
            char[] passwordChars = console.readPassword(prompt);
            return new String(passwordChars).trim();
        } else {
            System.out.print(prompt);
            return new Scanner(System.in).nextLine().trim();
        }
    }

    private static void manageProfessors(ProfesseurService professeurService, UtilisateurService utilisateurService, Scanner scanner) {
        boolean isInProfessorMenu = true;

        while (isInProfessorMenu) {
            System.out.println("\n--- Gestion des Professeurs ---");
            System.out.println("1. Ajouter un professeur");
            System.out.println("2. Mettre à jour un professeur");
            System.out.println("3. Supprimer un professeur");
            System.out.println("4. Lister tous les professeurs");
            System.out.println("5. Retourner au menu principal");
            System.out.print("Choix : ");
            int choice = scanner.nextInt();

            try {
                switch (choice) {
                    case 1:
                        addProfessor(professeurService, utilisateurService, scanner);
                        break;
                    case 2:
                        updateProfessor(professeurService, utilisateurService, scanner);
                        break;
                    case 3:
                        deleteProfessor(professeurService, scanner);
                        break;
                    case 4:
                        listAllProfessors(professeurService);
                        break;
                    case 5:
                        isInProfessorMenu = false;
                        break;
                    default:
                        System.out.println("Choix invalide !");
                }
            } catch (SQLException e) {
                System.out.println("Erreur lors de la gestion des professeurs : " + e.getMessage());
            }
        }
    }
    private static void addProfessor(ProfesseurService professeurService,UtilisateurService utilisateurService, Scanner scanner) {
        try {
            scanner.nextLine();

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


            System.out.print("ID d'utilisateur associé : ");
            Long utilisateurId = scanner.nextLong();
            scanner.nextLine();
            Optional<Utilisateur> optionalUtilisateur = utilisateurService.getById(utilisateurId);
            if (optionalUtilisateur.isEmpty()) {
                System.out.println("Aucun professeur trouvé avec cet ID !");
                return;
            }
            Utilisateur utilisateur = optionalUtilisateur.get();

            Professeur newProf = new Professeur(null,code, nom, prenom, specialite,utilisateur,new ArrayList<ElementModule>());
            professeurService.create(newProf);

            System.out.println("Professeur ajouté avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du professeur : " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Entrée invalide : " + e.getMessage());
        }
    }

    private static void updateProfessor(ProfesseurService professeurService, UtilisateurService utilisateurService, Scanner scanner) throws SQLException {
        System.out.print("------- Laisser vide pour ne pas modifier -------\n");
        System.out.print("ID du professeur à mettre à jour : ");
        Long updateId = scanner.nextLong();
        scanner.nextLine();

        Optional<Professeur> existingProf = professeurService.getById(updateId);
        if (existingProf.isEmpty()) {
            System.out.println("Professeur non trouvé !");
            return;
        }
        Professeur currentProf = existingProf.get();

        System.out.print("Nouveau Code : ");
        String newCode = scanner.nextLine();
        String finalCode = newCode.isEmpty() ? currentProf.getCode() : newCode;

        System.out.print("Nouveau nom : ");
        String newNom = scanner.nextLine();
        String finalNom = newNom.isEmpty() ? currentProf.getNom() : newNom;

        System.out.print("Nouveau prénom : ");
        String newPrenom = scanner.nextLine();
        String finalPrenom = newPrenom.isEmpty() ? currentProf.getPrenom() : newPrenom;

        System.out.print("Nouvelle spécialité : ");
        String newSpecialite = scanner.nextLine();
        String finalSpecialite = newSpecialite.isEmpty() ? currentProf.getSpecialite() : newSpecialite;

        System.out.print("Nouvel ID d'utilisateur associé : ");
        String utilisateurIdInput = scanner.nextLine().trim();
        Utilisateur finalUtilisateur = currentProf.getUtilisateur();

        if (!utilisateurIdInput.isEmpty()) {
            try {
                Long utilisateurId = Long.parseLong(utilisateurIdInput);
                Optional<Utilisateur> optionalUtilisateur = utilisateurService.getById(utilisateurId);
                if (optionalUtilisateur.isPresent()) {
                    finalUtilisateur = optionalUtilisateur.get();
                } else {
                    System.out.println("Utilisateur non trouvé, l'ancien utilisateur sera conservé.");
                }
            } catch (NumberFormatException e) {
                System.out.println("ID d'utilisateur invalide, l'ancien utilisateur sera conservé.");
            }
        }

        Professeur updatedProf = new Professeur(updateId, finalCode, finalNom, finalPrenom, finalSpecialite, finalUtilisateur, new ArrayList<ElementModule>());
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
        boolean isInFiliereMenu = true;

        while (isInFiliereMenu) {
            System.out.println("\n--- Gestion des Filières ---");
            System.out.println("1. Ajouter une filière");
            System.out.println("2. Mettre à jour une filière");
            System.out.println("3. Supprimer une filière");
            System.out.println("4. Lister toutes les filières");
            System.out.println("5. Retourner au menu principal");
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
                    case 5:
                        isInFiliereMenu = false;
                        break;
                    default:
                        System.out.println("Choix invalide !");
                }
            } catch (SQLException e) {
                System.out.println("Erreur lors de la gestion des filières : " + e.getMessage());
            }
        }
    }

    private static void addFiliere ( FiliereService filiereService, Scanner scanner) {
        try {
            scanner.nextLine();

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

            Filiere newFiliere = new Filiere(null, code, nom,new ArrayList<Module>(),new ArrayList<Etudiant>());
            filiereService.create(newFiliere);
            System.out.println("Filière ajoutée avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de la filière : " + e.getMessage());
        }
    }

    private static void updateFiliere(FiliereService filiereService, Scanner scanner) throws SQLException {
        System.out.print("------- Laisser vide pour ne pas modifier -------\n");
        System.out.print("ID de la filière à mettre à jour : ");
        Long updateId = scanner.nextLong();
        scanner.nextLine();


        Optional<Filiere> existingFiliere = filiereService.getById(updateId);
        if (existingFiliere.isEmpty()) {
            System.out.println("Filière non trouvée !");
            return;
        }
        Filiere currentFiliere = existingFiliere.get();

        System.out.print("Nouveau code de la filière : ");
        String newCode = scanner.nextLine();
        String finalCode = newCode.isEmpty() ? currentFiliere.getCode() : newCode;

        System.out.print("Nouveau nom de la filière : ");
        String newNom = scanner.nextLine();
        String finalNom = newNom.isEmpty() ? currentFiliere.getNom() : newNom;


        Filiere updatedFiliere = new Filiere(
                updateId,
                finalCode,
                finalNom,
                currentFiliere.getModules(),
                new ArrayList<Etudiant>()
        );

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



    ///////////////////////////////////////////////////////////////////////////////
    //     ------------------- gestion des Modules ------------------------    ///
    ///////////////////////////////////////////////////////////////////////////////


    private static void manageModules(ModuleService moduleService, FiliereDAO filiereDAO, Scanner scanner) {
        boolean isInModuleMenu = true;

        while (isInModuleMenu) {
            System.out.println("\n--- Gestion des Modules ---");
            System.out.println("1. Ajouter un module");
            System.out.println("2. Mettre à jour un module");
            System.out.println("3. Supprimer un module");
            System.out.println("4. Lister tous les modules");
            System.out.println("5. Retourner au menu principal");
            System.out.print("Choix : ");
            int choice = scanner.nextInt();

            try {
                switch (choice) {
                    case 1:
                        addModule(moduleService, filiereDAO, scanner);
                        break;
                    case 2:
                        updateModule(moduleService, filiereDAO, scanner);
                        break;
                    case 3:
                        deleteModule(moduleService, scanner);
                        break;
                    case 4:
                        listAllModules(moduleService);
                        break;
                    case 5:
                        isInModuleMenu = false;
                        break;
                    default:
                        System.out.println("Choix invalide !");
                }
            } catch (SQLException e) {
                System.out.println("Erreur lors de la gestion des modules : " + e.getMessage());
            }
        }
    }

    private static void addModule(ModuleService moduleService, FiliereDAO filiereDAO, Scanner scanner) throws SQLException {
        scanner.nextLine();

        System.out.print("Code du module : ");
        String code = scanner.nextLine().trim();
        if (code.isEmpty()) {
            System.out.println("Le code ne peut pas être vide !");
            return;
        }

        System.out.print("Nom du module : ");
        String nom = scanner.nextLine().trim();
        if (nom.isEmpty()) {
            System.out.println("Le nom ne peut pas être vide !");
            return;
        }


        System.out.print("Semestre (S1, S2, S3, S4, S5) : ");
        String semestreInput = scanner.nextLine().trim().toUpperCase();
        try {
            Semestre semestre = Semestre.valueOf(semestreInput);

            System.out.print("Code de la filière : ");
            String filiereCode = scanner.nextLine().trim();

            Optional<Filiere> optionalFiliere = filiereDAO.findByCode(filiereCode);
            if (optionalFiliere.isPresent()) {
                Filiere filiere = optionalFiliere.get();
                Module newModule = new Module(null,code, nom, filiere, semestre, null); // ID auto-généré, pas d'éléments pour l'instant
                moduleService.create(newModule);
                System.out.println("Module ajouté avec succès !");
            } else {
                System.out.println("Aucune filière trouvée pour le code fourni !");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Semestre invalide ! Veuillez entrer une valeur valide (S1, S2, S3, S4, S5).");
        }
    }

    private static void updateModule(ModuleService moduleService, FiliereDAO filiereDAO, Scanner scanner) throws SQLException {
        System.out.print("------- Laisser vide pour ne pas modifier -------\n");
        System.out.print("ID du module à mettre à jour : ");
        Long updateId = scanner.nextLong();
        scanner.nextLine();


        Optional<Module> existingModule = moduleService.getById(updateId);
        if (existingModule.isEmpty()) {
            System.out.println("Module non trouvé !");
            return;
        }
        Module currentModule = existingModule.get();

        System.out.print("Nouveau code du module : ");
        String newCode = scanner.nextLine();
        String finalCode = newCode.isEmpty() ? currentModule.getCode() : newCode;

        System.out.print("Nouveau nom du module : ");
        String newName = scanner.nextLine();
        String finalName = newName.isEmpty() ? currentModule.getNom() : newName;

        System.out.print("Nouveau semestre (S1, S2, S3, S4, S5) : ");
        String semestreInput = scanner.nextLine().trim().toUpperCase();
        Semestre finalSemestre = currentModule.getSemestre();

        if (!semestreInput.isEmpty()) {
            try {
                finalSemestre = Semestre.valueOf(semestreInput);
            } catch (IllegalArgumentException e) {
                System.out.println("Semestre invalide ! Le semestre actuel sera conservé.");
            }
        }

        System.out.print("Code de la nouvelle filière : ");
        String newFiliereCode = scanner.nextLine().trim();
        Filiere finalFiliere = currentModule.getFiliere();

        if (!newFiliereCode.isEmpty()) {
            Optional<Filiere> optionalFiliere = filiereDAO.findByCode(newFiliereCode);
            if (optionalFiliere.isPresent()) {
                finalFiliere = optionalFiliere.get();
            } else {
                System.out.println("Filière non trouvée ! La filière actuelle sera conservée.");
            }
        }

        Module updatedModule = new Module(
                updateId,
                finalCode,
                finalName,
                finalFiliere,
                finalSemestre,
                new ArrayList<ElementModule>()
        );

        moduleService.update(updateId, updatedModule);
        System.out.println("Module mis à jour avec succès !");
    }


    private static void deleteModule(ModuleService moduleService, Scanner scanner) throws SQLException {
        System.out.print("ID du module à supprimer : ");
        Long deleteId = scanner.nextLong();
        moduleService.delete(deleteId);
        System.out.println("Module supprimé avec succès.");
    }

    private static void listAllModules(ModuleService moduleService) throws SQLException {
        List<Module> modules = moduleService.getAll();
        if (modules.isEmpty()) {
            System.out.println("Aucun module trouvé.");
        } else {
            modules.forEach(System.out::println);
        }
    }


    ///////////////////////////////////////////////////////////////////////////////
    //     ------------------- gestion des Element Modules ------------------------    ///
    ///////////////////////////////////////////////////////////////////////////////


    private static void manageElementModule(ElementModuleService elementModuleService,
                                            ModuleService moduleService,
                                            ProfesseurService professeurService,
                                            Scanner scanner) {
        boolean isInElementMenu = true;

        try {
            while (isInElementMenu) {
                System.out.println("\n--- Gestion des Éléments de Module ---");
                System.out.println("1. Créer un nouvel élément de module");
                System.out.println("2. Mettre à jour un élément de module");
                System.out.println("3. Supprimer un élément de module");
                System.out.println("4. Afficher tous les éléments de module");
                System.out.println("5. Retourner au menu principal");
                System.out.print("Choix : ");
                int choix = scanner.nextInt();
                scanner.nextLine();

                switch (choix) {
                    case 1:
                        createElementModule(elementModuleService, moduleService, professeurService, scanner);
                        break;
                    case 2:
                        updateElementModule(elementModuleService, moduleService, professeurService, scanner);
                        break;
                    case 3:
                        deleteElementModule(elementModuleService, scanner);
                        break;
                    case 4:
                        displayAllElementModules(elementModuleService);
                        break;
                    case 5:
                        isInElementMenu = false;
                        break;
                    default:
                        System.out.println("Choix invalide ! Veuillez choisir entre 1 et 5.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur SQL : " + e.getMessage());
        } catch (InputMismatchException e) {
            System.out.println("Entrée invalide. Veuillez vérifier vos saisies !");
            scanner.nextLine();
        }
    }

    private static void createElementModule(ElementModuleService elementModuleService,
                                            ModuleService moduleService,
                                            ProfesseurService professeurService,
                                            Scanner scanner) throws SQLException {
        System.out.println("\nCréation d'un nouvel élément de module :");

        System.out.print("Nom de l'élément de module : ");
        String nom = scanner.nextLine().trim();

        System.out.print("Coefficient de l'élément de module : ");
        Float coefficient = scanner.nextFloat();
        scanner.nextLine();


        System.out.print("ID du module associé : ");
        Long moduleId = scanner.nextLong();
        scanner.nextLine();
        Optional<Module> optionalModule = moduleService.getById(moduleId);
        if (optionalModule.isEmpty()) {
            System.out.println("Aucun module trouvé avec cet ID !");
            return;
        }
        Module module = optionalModule.get();


        System.out.print("ID du professeur associé : ");
        Long professeurId = scanner.nextLong();
        scanner.nextLine();
        Optional<Professeur> optionalProfesseur = professeurService.getById(professeurId);
        if (optionalProfesseur.isEmpty()) {
            System.out.println("Aucun professeur trouvé avec cet ID !");
            return;
        }
        Professeur professeur = optionalProfesseur.get();


        ElementModule newElementModule = new ElementModule(null, nom, coefficient, module, professeur, new ArrayList<>());
        elementModuleService.create(newElementModule);

        System.out.println("Élément de module créé avec succès !");
    }


    private static void updateElementModule(ElementModuleService elementModuleService,
                                            ModuleService moduleService,
                                            ProfesseurService professeurService,
                                            Scanner scanner) throws SQLException {
        System.out.print("------- Laisser vide pour ne pas modifier -------\n");
        System.out.println("\nMise à jour d'un élément de module :");

        System.out.print("ID de l'élément de module à mettre à jour : ");
        Long updateId = scanner.nextLong();
        scanner.nextLine();


        Optional<ElementModule> optionalElementModule = elementModuleService.getById(updateId);
        if (optionalElementModule.isEmpty()) {
            System.out.println("Aucun élément de module trouvé avec cet ID !");
            return;
        }


        System.out.print("Nouveau nom de l'élément de module : ");
        String nom = scanner.nextLine().trim();

        System.out.print("Nouveau coefficient : ");
        String coefficientInput = scanner.nextLine().trim();
        Float coefficient = coefficientInput.isEmpty() ? null : Float.parseFloat(coefficientInput);


        System.out.print("Nouvel ID du module associé : ");
        String moduleIdInput = scanner.nextLine().trim();
        Module module = null;
        if (!moduleIdInput.isEmpty()) {
            Long moduleId = Long.parseLong(moduleIdInput);
            Optional<Module> optionalModule = moduleService.getById(moduleId);
            if (optionalModule.isEmpty()) {
                System.out.println("Aucun module trouvé avec cet ID !");
                return;
            }
            module = optionalModule.get();
        }


        System.out.print("Nouvel ID du professeur associé : ");
        String professeurIdInput = scanner.nextLine().trim();
        Professeur professeur = null;
        if (!professeurIdInput.isEmpty()) {
            Long professeurId = Long.parseLong(professeurIdInput);
            Optional<Professeur> optionalProfesseur = professeurService.getById(professeurId);
            if (optionalProfesseur.isEmpty()) {
                System.out.println("Aucun professeur trouvé avec cet ID !");
                return;
            }
            professeur = optionalProfesseur.get();
        }


        ElementModule updatedElementModule = optionalElementModule.get();
        if (!nom.isEmpty()) updatedElementModule.setNom(nom);
        if (coefficient != null) updatedElementModule.setCoefficient(coefficient);
        if (module != null) updatedElementModule.setModule(module);
        if (professeur != null) updatedElementModule.setProfesseur(professeur);

        elementModuleService.update(updateId, updatedElementModule);

        System.out.println("Élément de module mis à jour avec succès !");
    }

    private static void deleteElementModule(ElementModuleService elementModuleService, Scanner scanner) throws SQLException {
        System.out.println("\nSuppression d'un élément de module :");

        System.out.print("ID de l'élément de module à supprimer : ");
        Long deleteId = scanner.nextLong();
        scanner.nextLine();


        Optional<ElementModule> optionalElementModule = elementModuleService.getById(deleteId);
        if (optionalElementModule.isEmpty()) {
            System.out.println("Aucun élément de module trouvé avec cet ID !");
            return;
        }


        elementModuleService.delete(deleteId);
        System.out.println("Élément de module supprimé avec succès !");
    }


    private static void displayAllElementModules(ElementModuleService elementModuleService) throws SQLException {
        System.out.println("\nListe des éléments de module :");

        List<ElementModule> elementModules = elementModuleService.getAll();
        if (elementModules.isEmpty()) {
            System.out.println("Aucun élément de module trouvé.");
        } else {
            for (ElementModule elementModule : elementModules) {
                System.out.println(elementModule);
            }
        }
    }





















    ///////////////////////////////////////////////////////////////////////////////
    //     -----------  gestion des Les comptes des utilisateur  -----------    ///
    ///////////////////////////////////////////////////////////////////////////////



    private static void manageUser(UtilisateurService utilisateurService, Scanner scanner) {
        boolean isInUserMenu = true;

        while (isInUserMenu) {
            System.out.println("\n--- Gestion des Utilisateurs ---");
            System.out.println("1. Ajouter un Utilisateur");
            System.out.println("2. Mettre à jour un Utilisateur");
            System.out.println("3. Supprimer un Utilisateur");
            System.out.println("4. Lister tous les Utilisateurs");
            System.out.println("5. Retourner au menu principal");
            System.out.print("Choix : ");
            int choice = scanner.nextInt();

            try {
                switch (choice) {
                    case 1:
                        addUtilisateur(utilisateurService, scanner);
                        break;
                    case 2:
                        updateUtilisateur(utilisateurService, scanner);
                        break;
                    case 3:
                        deleteUtilisateur(utilisateurService, scanner);
                        break;
                    case 4:
                        listAllUtilisateurs(utilisateurService);
                        break;
                    case 5:
                        isInUserMenu = false;
                        break;
                    default:
                        System.out.println("Choix invalide !");
                }
            } catch (SQLException e) {
                System.out.println("Erreur lors de la gestion des utilisateurs : " + e.getMessage());
            }
        }
    }
    private static void addUtilisateur(UtilisateurService utilisateurService, Scanner scanner) {
        try {
            scanner.nextLine();

            System.out.print("Login : ");
            String login = scanner.nextLine().trim();
            if (login.isEmpty()) {
                System.out.println("Le code ne peut pas être vide !");
                return;
            }

            System.out.print("Password : ");
            String password = scanner.nextLine().trim();
            if (password.isEmpty()) {
                System.out.println("Le password ne peut pas être vide !");
                return;
            }

            System.out.print("role : ");
            String roleInput = scanner.nextLine().trim().toUpperCase();
            Role role = Role.valueOf(roleInput);


            Utilisateur newUtil = new Utilisateur(null,login, password, role,null);
            utilisateurService.create(newUtil);

            System.out.println("Utilisateur ajouté avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout d'utilisateur : " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Entrée invalide : " + e.getMessage());
        }
    }

    private static void updateUtilisateur(UtilisateurService utilisateurService, Scanner scanner) throws SQLException {
        System.out.print("------- Laisser vide pour ne pas modifier -------\n");
        System.out.print("ID d'Utilisateur : ");
        Long updateId = scanner.nextLong();
        scanner.nextLine();


        Optional<Utilisateur> existingUser = utilisateurService.getById(updateId);
        if (existingUser.isEmpty()) {
            System.out.println("Utilisateur non trouvé !");
            return;
        }
        Utilisateur currentUser = existingUser.get();

        System.out.print("Nouveau login : ");
        String newLogin = scanner.nextLine();
        String finalLogin = newLogin.isEmpty() ? currentUser.getLogin() : newLogin;

        System.out.print("Nouveau password : ");
        String newPassword = scanner.nextLine();
        String finalPassword = newPassword.isEmpty() ? currentUser.getPassword() : newPassword;

        System.out.print("Nouveau role : ");
        String newRoleInput = scanner.nextLine().trim().toUpperCase();
        Role finalRole = currentUser.getRole();

        if (!newRoleInput.isEmpty()) {
            try {
                finalRole = Role.valueOf(newRoleInput);
            } catch (IllegalArgumentException e) {
                System.out.println("Role invalide ! Le rôle actuel sera conservé.");
            }
        }

        Utilisateur updatedUtil = new Utilisateur(updateId, finalLogin, finalPassword, finalRole, null);
        utilisateurService.update(updateId, updatedUtil);
        System.out.println("Utilisateur mis à jour avec succès.");
    }

    private static void deleteUtilisateur(UtilisateurService utilisateurService, Scanner scanner) throws SQLException {
        System.out.print("ID d'utilisateur à supprimer : ");
        Long deleteId = scanner.nextLong();
        utilisateurService.delete(deleteId);
        System.out.println("Professeur supprimé avec succès.");
    }

    private static void listAllUtilisateurs(UtilisateurService utilisateurService) throws SQLException {
        List<Utilisateur> utilisateurs = utilisateurService.getAll();
        if (utilisateurs.isEmpty()) {
            System.out.println("Aucun professeur trouvé.");
        } else {
            utilisateurs.forEach(System.out::println);
        }
    }


    ///////////////////////////////////////////////////////////////////////////////
    //      ----------- gestion des Evaluation modules --------------------    ///
    ///////////////////////////////////////////////////////////////////////////////

    private static void manageModaliteEvaluation(ModaliteEvaluationService modaliteEvaluationService,
                                                 ElementModuleService elementModuleService,
                                                 Scanner scanner) {
        boolean isInModaliteMenu = true;

        while (isInModaliteMenu) {
            System.out.println("\n--- Gestion des Modalités d'Évaluation ---");
            System.out.println("1. Ajouter une modalité d'évaluation");
            System.out.println("2. Mettre à jour une modalité d'évaluation");
            System.out.println("3. Supprimer une modalité d'évaluation");
            System.out.println("4. Lister toutes les modalités d'évaluation");
            System.out.println("5. Retourner au menu principal");
            System.out.print("Choix : ");
            int choice = scanner.nextInt();

            try {
                switch (choice) {
                    case 1:
                        addModaliteEvaluation(modaliteEvaluationService, elementModuleService, scanner);
                        break;
                    case 2:
                        updateModaliteEvaluation(modaliteEvaluationService, elementModuleService, scanner);
                        break;
                    case 3:
                        deleteModaliteEvaluation(modaliteEvaluationService, scanner);
                        break;
                    case 4:
                        listAllModaliteEvaluations(modaliteEvaluationService);
                        break;
                    case 5:
                        isInModaliteMenu = false;
                        break;
                    default:
                        System.out.println("Choix invalide !");
                }
            } catch (SQLException e) {
                System.out.println("Erreur lors de la gestion des modalités d'évaluation : " + e.getMessage());
            }
        }
    }

    private static void addModaliteEvaluation(ModaliteEvaluationService modaliteEvaluationService,
                                              ElementModuleService elementModuleService,
                                              Scanner scanner) {
        try {
            scanner.nextLine();

            System.out.print("Type de modalité (ex : CC, TP, PROJET, PRESENTATION) : ");
            String typeInput = scanner.nextLine().trim();
            Type type;
            try {
                type = Type.valueOf(typeInput);
            } catch (IllegalArgumentException e) {
                System.out.println("Type invalide ! Les types valides sont : CC, TP, PROJET, PRESENTATION.");
                return;
            }


            System.out.print("Coefficient (ex : 0.3, 0.5) : ");
            float coefficient = scanner.nextFloat();
            scanner.nextLine(); // Consommer la nouvelle ligne restante


            System.out.print("ID de l'élément de module : ");
            Long elementModuleId = scanner.nextLong();
            scanner.nextLine(); // Consommer la nouvelle ligne restante


            Optional<ElementModule> optionalElementModule = elementModuleService.getById(elementModuleId);
            if (optionalElementModule.isEmpty()) {
                System.out.println("Aucun élément de module trouvé avec cet ID !");
                return;
            }
            ElementModule elementModule = optionalElementModule.get();


            ModaliteEvaluation newModalite = new ModaliteEvaluation(null, type, coefficient, elementModule, null);
            modaliteEvaluationService.create(newModalite);

            System.out.println("Modalité d'évaluation ajoutée avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de la modalité d'évaluation : " + e.getMessage());
        } catch (InputMismatchException e) {
            System.out.println("Entrée invalide : " + e.getMessage());
            scanner.nextLine();
        }
    }


    private static void updateModaliteEvaluation(ModaliteEvaluationService modaliteEvaluationService,
                                                 ElementModuleService elementModuleService,
                                                 Scanner scanner) throws SQLException {
        try {
            System.out.print("------- Laisser vide pour ne pas modifier -------\n");
            System.out.print("ID de la modalité à mettre à jour : ");
            Long updateId = scanner.nextLong();
            scanner.nextLine();

            Optional<ModaliteEvaluation> optionalModalite = modaliteEvaluationService.getById(updateId);
            if (optionalModalite.isEmpty()) {
                System.out.println("Aucune modalité d'évaluation trouvée avec cet ID !");
                return;
            }
            ModaliteEvaluation currentModalite = optionalModalite.get();

            System.out.print("Nouveau type de modalité (CC, TP, PROJET, PRESENTATION) : ");
            String typeInput = scanner.nextLine().trim();
            Type finalType = currentModalite.getType();

            if (!typeInput.isEmpty()) {
                try {
                    finalType = Type.valueOf(typeInput.toUpperCase());
                } catch (IllegalArgumentException e) {
                    System.out.println("Type invalide ! Le type actuel sera conservé.");
                }
            }

            System.out.print("Nouveau coefficient (ex : 0.3, 0.5) : ");
            String coefficientInput = scanner.nextLine().trim();
            Float finalCoefficient = currentModalite.getCoefficient();

            if (!coefficientInput.isEmpty()) {
                try {
                    finalCoefficient = Float.parseFloat(coefficientInput);
                    if (finalCoefficient <= 0 || finalCoefficient > 1) {
                        System.out.println("Coefficient invalide ! Le coefficient doit être entre 0 et 1. Le coefficient actuel sera conservé.");
                        finalCoefficient = currentModalite.getCoefficient();
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Coefficient invalide ! Le coefficient actuel sera conservé.");
                }
            }

            System.out.print("Nouvel ID de l'élément de module associé : ");
            String elementModuleInput = scanner.nextLine().trim();
            ElementModule finalElementModule = currentModalite.getElementModule();

            if (!elementModuleInput.isEmpty()) {
                try {
                    Long newElementModuleId = Long.parseLong(elementModuleInput);
                    Optional<ElementModule> optionalElementModule = elementModuleService.getById(newElementModuleId);
                    if (optionalElementModule.isPresent()) {
                        finalElementModule = optionalElementModule.get();
                    } else {
                        System.out.println("Élément de module non trouvé ! L'élément actuel sera conservé.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("ID invalide ! L'élément actuel sera conservé.");
                }
            }

            ModaliteEvaluation updatedModalite = new ModaliteEvaluation(
                    updateId,
                    finalType,
                    finalCoefficient,
                    finalElementModule,
                    new ArrayList<Evaluation>()
            );

            modaliteEvaluationService.update(updateId, updatedModalite);
            System.out.println("Modalité d'évaluation mise à jour avec succès !");

        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour de la modalité d'évaluation : " + e.getMessage());
        } catch (InputMismatchException e) {
            System.out.println("Entrée invalide. Veuillez vérifier vos saisies !");
            scanner.nextLine();
        }
    }


    private static void deleteModaliteEvaluation(ModaliteEvaluationService modaliteEvaluationService, Scanner scanner) throws SQLException {
        System.out.print("ID de la modalité à supprimer : ");
        Long deleteId = scanner.nextLong();
        modaliteEvaluationService.delete(deleteId);
        System.out.println("Modalité d'évaluation supprimée avec succès.");
    }

    private static void listAllModaliteEvaluations(ModaliteEvaluationService modaliteEvaluationService) throws SQLException {
        List<ModaliteEvaluation> modalites = modaliteEvaluationService.getAll();
        if (modalites.isEmpty()) {
            System.out.println("Aucune modalité d'évaluation trouvée.");
        } else {
            modalites.forEach(System.out::println);
        }
    }











}
