package org.example.test;

import org.example.dao.ProfesseurDAO;
import org.example.entity.ElementModule;
import org.example.entity.Professeur;
import org.example.entity.Utilisateur;
import org.example.service.ProfesseurService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProfesseurServiceTest {
    public static void main(String[] args) {
        // Création d'un DAO et injection dans le service
        ProfesseurDAO professeurDAO = new ProfesseurDAO();
        ProfesseurService professeurService = new ProfesseurService(professeurDAO);

        try {
            // 1. Tester l'ajout d'un professeur
            Professeur newProfesseur = new Professeur();
            newProfesseur.setCode("T001");
            newProfesseur.setNom("Lamghari");
            newProfesseur.setPrenom("Nidal");
            newProfesseur.setSpecialite("L'ingenierie des connaissances");

            Utilisateur utilisateur = new Utilisateur();
            utilisateur.setId(1L); // Remplacez par un ID utilisateur valide
            newProfesseur.setUtilisateur(utilisateur);

            Professeur addedProfesseur = professeurService.addProfesseur(newProfesseur);
            System.out.println("Professeur ajouté : " + addedProfesseur);

            // 2. Récupérer un professeur par ID
            Optional<Professeur> fetchedProfesseur = professeurService.getProfesseurById(addedProfesseur.getId());
            fetchedProfesseur.ifPresent(prof -> System.out.println("Professeur récupéré : " + prof));

            // 3. Mettre à jour un professeur
            if (fetchedProfesseur.isPresent()) {
                Professeur profToUpdate = fetchedProfesseur.get();
                profToUpdate.setSpecialite("Physique");
                Professeur updatedProfesseur = professeurService.updateProfesseur(profToUpdate.getId(), profToUpdate);
                System.out.println("Professeur mis à jour : " + updatedProfesseur);
            }

            // 4. Ajouter un module à un professeur
            ElementModule newModule = new ElementModule();
            newModule.setId(1L); // Remplacez par un ID valide
            newModule.setNom("Algèbre");
            newModule.setCoefficient(3.0f);

            professeurService.addElementModuleToProfesseur(addedProfesseur.getId(), newModule);
            System.out.println("Module ajouté au professeur.");

            // 5. Supprimer un professeur
            professeurService.deleteProfesseur(addedProfesseur.getId());
            System.out.println("Professeur supprimé.");

        } catch (SQLException e) {
            System.err.println("Erreur lors du test du service : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
