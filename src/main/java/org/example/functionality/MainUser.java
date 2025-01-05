package org.example.functionality;

import org.example.dao.*;
import org.example.entity.*;
import org.example.entity.Module;
import org.example.service.*;

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
            // Authentification avant d'afficher le menu
            if (!authenticate(scanner, utilisateurService)) {
                System.out.println("Authentification échouée. Programme terminé.");
                break;
            }

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
                System.out.println("6. Gérer les comptes utilisateurs");
                System.out.println("7. Déconnexion");
                System.out.println("0. Quitter");
                System.out.print("Choix : ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consommer la nouvelle ligne

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

    private static boolean authenticate(Scanner scanner, UtilisateurService utilisateurService) {
        System.out.println("--- Authentification ---");
        System.out.print("Identifiant : ");
        String inputUsername = scanner.nextLine().trim();
        System.out.print("Mot de passe : ");
        String inputPassword = scanner.nextLine().trim();

        try {
            boolean isAuthenticated = utilisateurService.isValidUser(inputUsername, inputPassword, "ADMIN");
            if (isAuthenticated) {
                System.out.println("Authentification réussie !");
                return true;
            } else {
                System.out.println("Identifiant ou mot de passe incorrect ou rôle non autorisé");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'authentification : " + e.getMessage());
            return false;
        }
    }

    private static void manageProfessors(ProfesseurService professeurService, UtilisateurService utilisateurService, Scanner scanner) {
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
                    addProfessor(professeurService,utilisateurService, scanner);
                    break;
                case 2:
                    updateProfessor(professeurService,utilisateurService, scanner);
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
    private static void addProfessor(ProfesseurService professeurService,UtilisateurService utilisateurService, Scanner scanner) {
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

            // Sélection du professeur associé
            System.out.print("ID d'utilisateur associé : ");
            Long utilisateurId = scanner.nextLong();
            scanner.nextLine(); // Consommer la nouvelle ligne restante
            Optional<Utilisateur> optionalUtilisateur = utilisateurService.getById(utilisateurId);
            if (optionalUtilisateur.isEmpty()) {
                System.out.println("Aucun professeur trouvé avec cet ID !");
                return;
            }
            Utilisateur utilisateur = optionalUtilisateur.get();

            // Création d'un nouvel objet Professeur avec les données fournies
            Professeur newProf = new Professeur(null,code, nom, prenom, specialite,utilisateur,new ArrayList<>()); // null pour l'ID auto-généré
            professeurService.create(newProf);

            System.out.println("Professeur ajouté avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du professeur : " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Entrée invalide : " + e.getMessage());
        }
    }

    private static void updateProfessor(ProfesseurService professeurService,UtilisateurService utilisateurService, Scanner scanner) throws SQLException {
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

        System.out.print("Nouvel ID d'utilisateur associé (laisser vide pour ne pas modifier) : ");
        String utilisateurIdInput = scanner.nextLine().trim();
        Utilisateur utilisateur = null;
        if (!utilisateurIdInput.isEmpty()) {
            Long utilisateurId = Long.parseLong(utilisateurIdInput);
            Optional<Utilisateur> optionalUtilisateur = utilisateurService.getById(utilisateurId);
            if (optionalUtilisateur.isEmpty()) {
                System.out.println("Aucun professeur trouvé avec cet ID !");
                return;
            }
            utilisateur = optionalUtilisateur.get();
        }

        Professeur updatedProf = new Professeur(updateId,newCode,newNom, newPrenom, newSpecialite,utilisateur,new ArrayList<>()); // Remplacez par le constructeur correct
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



    ///////////////////////////////////////////////////////////////////////////////
    //     ------------------- gestion des Modules ------------------------    ///
    ///////////////////////////////////////////////////////////////////////////////


    private static void manageModules(ModuleService moduleService,FiliereDAO filiereDAO, Scanner scanner) {
        System.out.println("\n--- Gestion des Modules ---");
        System.out.println("1. Ajouter un module");
        System.out.println("2. Mettre à jour un module");
        System.out.println("3. Supprimer un module");
        System.out.println("4. Lister tous les modules");
        System.out.print("Choix : ");
        int choice = scanner.nextInt();

        try {
            switch (choice) {
                case 1:
                    addModule(moduleService,filiereDAO, scanner);
                    break;
                case 2:
                    updateModule(moduleService,filiereDAO, scanner);
                    break;
                case 3:
                    deleteModule(moduleService, scanner);
                    break;
                case 4:
                    listAllModules(moduleService);
                    break;
                default:
                    System.out.println("Choix invalide !");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la gestion des modules : " + e.getMessage());
        }
    }

    private static void addModule(ModuleService moduleService, FiliereDAO filiereDAO, Scanner scanner) throws SQLException {
        scanner.nextLine(); // Consommer la nouvelle ligne restante

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
        System.out.print("ID du module à mettre à jour : ");
        Long updateId = scanner.nextLong();
        scanner.nextLine(); // Consommer la nouvelle ligne restante

        System.out.print("Nouveau code du module : ");
        String newCode = scanner.nextLine();

        System.out.print("Nouveau nom du module : ");
        String newName = scanner.nextLine();


        System.out.print("Nouveau semestre (S1, S2, S3, S4, S5) : ");
        String semestreInput = scanner.nextLine().trim().toUpperCase();

        try {
            Semestre newSemestre = Semestre.valueOf(semestreInput);

            System.out.print("Code de la nouvelle filière : ");
            String newFiliereCode = scanner.nextLine().trim();

            Optional<Filiere> optionalFiliere = filiereDAO.findByCode(newFiliereCode);
            if (optionalFiliere.isPresent()) {
                Filiere newFiliere = optionalFiliere.get();
                Module updatedModule = new Module(updateId,newCode ,newName, newFiliere, newSemestre, null);
                moduleService.update(updateId, updatedModule);
                System.out.println("Module mis à jour avec succès !");
            } else {
                System.out.println("Aucune filière trouvée pour le code fourni !");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Semestre invalide ! Veuillez entrer une valeur valide (S1, S2, S3, S4, S5).");
        }
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
    //     ------------------- gestion des Modules ------------------------    ///
    ///////////////////////////////////////////////////////////////////////////////


    private static void manageElementModule(ElementModuleService elementModuleService,
                                            ModuleService moduleService,
                                            ProfesseurService professeurService,
                                            Scanner scanner)  {
        try {
            while (true) {
                System.out.println("\nGestion des éléments de module :");
                System.out.println("1. Créer un nouvel élément de module");
                System.out.println("2. Mettre à jour un élément de module");
                System.out.println("3. Supprimer un élément de module");
                System.out.println("4. Afficher tous les éléments de module");
                System.out.println("5. Quitter");
                System.out.print("Choisissez une option : ");
                int choix = scanner.nextInt();
                scanner.nextLine(); // Consommer la nouvelle ligne restante

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
                        System.out.println("Retour au menu principal...");
                        return;
                    default:
                        System.out.println("Option invalide. Veuillez choisir entre 1 et 5.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur SQL : " + e.getMessage());
        } catch (InputMismatchException e) {
            System.out.println("Entrée invalide. Veuillez vérifier vos saisies !");
            scanner.nextLine(); // Consommer la mauvaise entrée
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
        scanner.nextLine(); // Consommer la nouvelle ligne restante

        // Sélection du module associé
        System.out.print("ID du module associé : ");
        Long moduleId = scanner.nextLong();
        scanner.nextLine(); // Consommer la nouvelle ligne restante
        Optional<Module> optionalModule = moduleService.getById(moduleId);
        if (optionalModule.isEmpty()) {
            System.out.println("Aucun module trouvé avec cet ID !");
            return;
        }
        Module module = optionalModule.get();

        // Sélection du professeur associé
        System.out.print("ID du professeur associé : ");
        Long professeurId = scanner.nextLong();
        scanner.nextLine(); // Consommer la nouvelle ligne restante
        Optional<Professeur> optionalProfesseur = professeurService.getById(professeurId);
        if (optionalProfesseur.isEmpty()) {
            System.out.println("Aucun professeur trouvé avec cet ID !");
            return;
        }
        Professeur professeur = optionalProfesseur.get();

        // Création de l'élément de module
        ElementModule newElementModule = new ElementModule(null, nom, coefficient, module, professeur, new ArrayList<>());
        elementModuleService.create(newElementModule);

        System.out.println("Élément de module créé avec succès !");
    }


    private static void updateElementModule(ElementModuleService elementModuleService,
                                            ModuleService moduleService,
                                            ProfesseurService professeurService,
                                            Scanner scanner) throws SQLException {
        System.out.println("\nMise à jour d'un élément de module :");

        System.out.print("ID de l'élément de module à mettre à jour : ");
        Long updateId = scanner.nextLong();
        scanner.nextLine(); // Consommer la nouvelle ligne restante

        // Vérification de l'existence de l'élément de module
        Optional<ElementModule> optionalElementModule = elementModuleService.getById(updateId);
        if (optionalElementModule.isEmpty()) {
            System.out.println("Aucun élément de module trouvé avec cet ID !");
            return;
        }

        // Lecture des nouvelles données
        System.out.print("Nouveau nom de l'élément de module (laisser vide pour ne pas modifier) : ");
        String nom = scanner.nextLine().trim();

        System.out.print("Nouveau coefficient (laisser vide pour ne pas modifier) : ");
        String coefficientInput = scanner.nextLine().trim();
        Float coefficient = coefficientInput.isEmpty() ? null : Float.parseFloat(coefficientInput);

        // Mise à jour du module associé
        System.out.print("Nouvel ID du module associé (laisser vide pour ne pas modifier) : ");
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

        // Mise à jour du professeur associé
        System.out.print("Nouvel ID du professeur associé (laisser vide pour ne pas modifier) : ");
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

        // Mise à jour de l'élément de module
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
        scanner.nextLine(); // Consommer la nouvelle ligne restante

        // Vérification de l'existence de l'élément de module
        Optional<ElementModule> optionalElementModule = elementModuleService.getById(deleteId);
        if (optionalElementModule.isEmpty()) {
            System.out.println("Aucun élément de module trouvé avec cet ID !");
            return;
        }

        // Suppression
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
        System.out.println("\n--- Gestion des Utilisateurs ---");
        System.out.println("1. Ajouter un Utilisateur");
        System.out.println("2. Mettre à jour un Utilisateur");
        System.out.println("3. Supprimer un Utilisateur");
        System.out.println("4. Lister tous les Utilisateurs");
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
                default:
                    System.out.println("Choix invalide !");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la gestion des utilisateurs : " + e.getMessage());
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


            Utilisateur newUtil = new Utilisateur(null,login, password, role,null); // null pour l'ID auto-généré
            utilisateurService.create(newUtil);

            System.out.println("Utilisateur ajouté avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout d'utilisateur : " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Entrée invalide : " + e.getMessage());
        }
    }

    private static void updateUtilisateur(UtilisateurService utilisateurService, Scanner scanner) throws SQLException {
        System.out.print("ID d'Utilisateur : ");
        Long updateId = scanner.nextLong();
        scanner.nextLine(); // Consommer la nouvelle ligne restante
        System.out.print("Nouveau login : ");
        String newLogin = scanner.nextLine();
        System.out.print("Nouveau password : ");
        String newPassword = scanner.nextLine();
        System.out.print("Nouveau role : ");
        String newRoleInput = scanner.nextLine().trim().toUpperCase();
        Role role = Role.valueOf(newRoleInput);


        Utilisateur updatedUtil = new Utilisateur(updateId,newLogin,newPassword, role,null); // Remplacez par le constructeur correct
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
        System.out.println("\n--- Gestion des Modalités d'Évaluation ---");
        System.out.println("1. Ajouter une modalité d'évaluation");
        System.out.println("2. Mettre à jour une modalité d'évaluation");
        System.out.println("3. Supprimer une modalité d'évaluation");
        System.out.println("4. Lister toutes les modalités d'évaluation");
        System.out.print("Choix : ");
        int choice = scanner.nextInt();

        try {
            switch (choice) {
                case 1:
                    addModaliteEvaluation(modaliteEvaluationService, elementModuleService, scanner);
                    break;
                case 2:
                    updateModaliteEvaluation(modaliteEvaluationService,elementModuleService, scanner);
                    break;
                case 3:
                    deleteModaliteEvaluation(modaliteEvaluationService, scanner);
                    break;
                case 4:
                    listAllModaliteEvaluations(modaliteEvaluationService);
                    break;
                default:
                    System.out.println("Choix invalide !");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la gestion des modalités d'évaluation : " + e.getMessage());
        }
    }

    private static void addModaliteEvaluation(ModaliteEvaluationService modaliteEvaluationService,
                                              ElementModuleService elementModuleService,
                                              Scanner scanner) {
        try {
            scanner.nextLine(); // Consommer la nouvelle ligne restante

            // Lecture du type de modalité
            System.out.print("Type de modalité (ex : CC, TP, PROJET, PRESENTATION) : ");
            String typeInput = scanner.nextLine().trim();
            Type type;
            try {
                type = Type.valueOf(typeInput);
            } catch (IllegalArgumentException e) {
                System.out.println("Type invalide ! Les types valides sont : CC, TP, PROJET, PRESENTATION.");
                return;
            }

            // Lecture du coefficient
            System.out.print("Coefficient (ex : 0.3, 0.5) : ");
            float coefficient = scanner.nextFloat();
            scanner.nextLine(); // Consommer la nouvelle ligne restante

            // Lecture de l'ID de l'élément de module
            System.out.print("ID de l'élément de module : ");
            Long elementModuleId = scanner.nextLong();
            scanner.nextLine(); // Consommer la nouvelle ligne restante

            // Recherche de l'élément de module
            Optional<ElementModule> optionalElementModule = elementModuleService.getById(elementModuleId);
            if (optionalElementModule.isEmpty()) {
                System.out.println("Aucun élément de module trouvé avec cet ID !");
                return;
            }
            ElementModule elementModule = optionalElementModule.get();

            // Création d'un nouvel objet ModaliteEvaluation
            ModaliteEvaluation newModalite = new ModaliteEvaluation(null, type, coefficient, elementModule, null);
            modaliteEvaluationService.create(newModalite);

            System.out.println("Modalité d'évaluation ajoutée avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de la modalité d'évaluation : " + e.getMessage());
        } catch (InputMismatchException e) {
            System.out.println("Entrée invalide : " + e.getMessage());
            scanner.nextLine(); // Consommer la mauvaise entrée
        }
    }


    private static void updateModaliteEvaluation(ModaliteEvaluationService modaliteEvaluationService,
                                                 ElementModuleService elementModuleService,
                                                 Scanner scanner) throws SQLException {
        try {
            // Lecture de l'ID de la modalité à mettre à jour
            System.out.print("ID de la modalité à mettre à jour : ");
            Long updateId = scanner.nextLong();
            scanner.nextLine(); // Consommer la nouvelle ligne restante

            // Vérification de l'existence de la modalité
            Optional<ModaliteEvaluation> optionalModalite = modaliteEvaluationService.getById(updateId);
            if (optionalModalite.isEmpty()) {
                System.out.println("Aucune modalité d'évaluation trouvée avec cet ID !");
                return;
            }

            // Lecture du nouveau type de modalité
            System.out.print("Nouveau type de modalité (ex : CC, TP, PROJET, PRESENTATION) : ");
            String typeInput = scanner.nextLine().trim();
            Type newType;
            try {
                newType = Type.valueOf(typeInput.toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Type invalide ! Les types valides sont : CC, TP, PROJET, PRESENTATION.");
                return;
            }

            // Lecture du nouveau coefficient
            System.out.print("Nouveau coefficient (ex : 0.3, 0.5) : ");
            Float newCoefficient = scanner.nextFloat();
            scanner.nextLine(); // Consommer la nouvelle ligne restante

            // Lecture du nouvel ID de l'élément de module associé
            System.out.print("Nouvel ID de l'élément de module associé : ");
            Long newElementModuleId = scanner.nextLong();
            scanner.nextLine(); // Consommer la nouvelle ligne restante

            // Vérification de l'existence de l'élément de module
            Optional<ElementModule> optionalElementModule = elementModuleService.getById(newElementModuleId);
            if (optionalElementModule.isEmpty()) {
                System.out.println("Aucun élément de module trouvé avec cet ID !");
                return;
            }
            ElementModule newElementModule = optionalElementModule.get();

            // Mise à jour de la modalité d'évaluation
            ModaliteEvaluation updatedModalite = new ModaliteEvaluation(
                    updateId, newType, newCoefficient, newElementModule, null
            );
            modaliteEvaluationService.update(updateId, updatedModalite);

            System.out.println("Modalité d'évaluation mise à jour avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour de la modalité d'évaluation : " + e.getMessage());
        } catch (InputMismatchException e) {
            System.out.println("Entrée invalide. Veuillez vérifier vos saisies !");
            scanner.nextLine(); // Consommer la mauvaise entrée
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
