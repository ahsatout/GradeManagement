package org.example.test;

import org.example.dao.ModuleDAO;
import org.example.entity.Filiere;
import org.example.entity.Module;
import org.example.entity.Semestre;
import org.example.service.ModuleService;

import java.sql.SQLException;
import java.util.Optional;

public class ModuleServiceTest {
    public static void main(String[] args) {
        ModuleDAO moduleDAO = new ModuleDAO();
        ModuleService moduleService = new ModuleService(moduleDAO);
        Filiere filiere = new Filiere();
        filiere.setId(1L);
        filiere.setCode("INFO");
        filiere.setNom("Informatique");

        try {
            // Ajouter un module
            org.example.entity.Module newModule = new org.example.entity.Module();
            newModule.setCode("MATH101");
            newModule.setNom("Mathematics");
            newModule.setSemestre(Semestre.S1);
            // Assurez-vous de définir une filière valide
            newModule.setFiliere(filiere);

            org.example.entity.Module addedModule = moduleService.addModule(newModule);
            System.out.println("Module ajouté : " + addedModule);

            // Récupérer un module
            Optional<Module> fetchedModule = moduleService.getModuleByCode("MATH101");
            fetchedModule.ifPresent(module -> System.out.println("Module récupéré : " + module));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}