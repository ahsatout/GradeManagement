package org.example.functionality;

import org.example.dao.EvaluationDAO;
import org.example.service.EvaluationService;
import org.example.entity.Evaluation;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class MainEvaluation {

    private static void manageEvaluations(EvaluationService evaluationService, Scanner scanner) {
        System.out.println("\n--- Gestion des Évaluations ---");
        System.out.println("1. Ajouter une évaluation");
        System.out.println("2. Mettre à jour une évaluation");
        System.out.println("3. Supprimer une évaluation");
        System.out.println("4. Lister toutes les évaluations");
        System.out.print("Choix : ");
        int choice = scanner.nextInt();

        try {
            switch (choice) {
                case 1:
                    addEvaluation(evaluationService, scanner);
                    break;
                case 2:
                    updateEvaluation(evaluationService, scanner);
                    break;
                case 3:
                    deleteEvaluation(evaluationService, scanner);
                    break;
                case 4:
                    listAllEvaluations(evaluationService);
                    break;
                default:
                    System.out.println("Choix invalide !");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la gestion des évaluations : " + e.getMessage());
        }
    }

    private static void addEvaluation(EvaluationService evaluationService, Scanner scanner) {
        try {
            scanner.nextLine(); // Consommer la nouvelle ligne restante

            System.out.print("Nom de l'évaluation : ");
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                System.out.println("Le nom ne peut pas être vide !");
                return;
            }

            System.out.print("Description de l'évaluation : ");
            String description = scanner.nextLine().trim();
            if (description.isEmpty()) {
                System.out.println("La description ne peut pas être vide !");
                return;
            }

            // Créer l'évaluation avec un exemple d'objet
            Evaluation newEvaluation = new Evaluation(null, 0f, false, null, null); // Remplacer par des valeurs réelles
            evaluationService.create(newEvaluation);

            System.out.println("Évaluation ajoutée avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de l'évaluation : " + e.getMessage());
        }
    }

    private static void updateEvaluation(EvaluationService evaluationService, Scanner scanner) throws SQLException {
        System.out.print("ID de l'évaluation à mettre à jour : ");
        Long updateId = scanner.nextLong();
        scanner.nextLine(); // Consommer la nouvelle ligne restante

        System.out.print("Nouveau nom de l'évaluation : ");
        String newName = scanner.nextLine();
        System.out.print("Nouvelle description : ");
        String newDescription = scanner.nextLine();

        // Créer l'évaluation mise à jour
        Evaluation updatedEvaluation = new Evaluation(updateId, 0f, false, null, null); // Mettre des valeurs valides
        evaluationService.update(updateId, updatedEvaluation);
        System.out.println("Évaluation mise à jour avec succès.");
    }

    private static void deleteEvaluation(EvaluationService evaluationService, Scanner scanner) throws SQLException {
        System.out.print("ID de l'évaluation à supprimer : ");
        Long deleteId = scanner.nextLong();
        evaluationService.delete(deleteId);
        System.out.println("Évaluation supprimée avec succès.");
    }

    private static void listAllEvaluations(EvaluationService evaluationService) throws SQLException {
        List<Evaluation> evaluations = evaluationService.getAll();
        if (evaluations.isEmpty()) {
            System.out.println("Aucune évaluation trouvée.");
        } else {
            evaluations.forEach(System.out::println);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Initialiser EvaluationDAO et EvaluationService
        EvaluationDAO evaluationDAO = new EvaluationDAO();
        EvaluationService evaluationService = new EvaluationService(evaluationDAO); // Passer l'instance d'EvaluationDAO

        while (true) {
            manageEvaluations(evaluationService, scanner);
        }
    }
}
