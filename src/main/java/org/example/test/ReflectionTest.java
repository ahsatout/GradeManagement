package org.example.test;

import org.example.dao.*;
import org.example.reflection.DIContainer;
import org.example.service.ModaliteEvaluationService;
import org.example.service.ModuleService;
import org.example.service.ProfesseurService;
import org.example.service.UtilisateurService;

public class ReflectionTest {
    public static void main(String[] args) throws Exception {

        DIContainer container = new DIContainer("org.example.dao","org.example.service");

        // Testing UtilisateurService
        UtilisateurService utilisateurService1 = container.getBean(UtilisateurService.class);
        UtilisateurService utilisateurService2 = container.getBean(UtilisateurService.class);
        System.out.println("UtilisateurService singleton: " + (utilisateurService1 == utilisateurService2)); // Should print true

        // Testing ProfesseurService
        ProfesseurService professeurService1 = container.getBean(ProfesseurService.class);
        ProfesseurService professeurService2 = container.getBean(ProfesseurService.class);
        System.out.println("ProfesseurService singleton: " + (professeurService1 == professeurService2)); // Should print true

        // Testing ModuleService
        ModuleService moduleService1 = container.getBean(ModuleService.class);
        ModuleService moduleService2 = container.getBean(ModuleService.class);
        System.out.println("ModuleService singleton: " + (moduleService1 == moduleService2)); // Should print true

        // Testing ModaliteEvaluationService
        ModaliteEvaluationService modaliteEvaluationService1 = container.getBean(ModaliteEvaluationService.class);
        ModaliteEvaluationService modaliteEvaluationService2 = container.getBean(ModaliteEvaluationService.class);
        System.out.println("ModaliteEvaluationService singleton: " + (modaliteEvaluationService1 == modaliteEvaluationService2)); // Should print true

        // Testing DAOs
        ElementModuleDAO elementModuleDAO1 = container.getBean(ElementModuleDAO.class);
        ElementModuleDAO elementModuleDAO2 = container.getBean(ElementModuleDAO.class);
        System.out.println("ElementModuleDAO singleton: " + (elementModuleDAO1 == elementModuleDAO2)); // Should print true

        EtudiantDAO etudiantDAO1 = container.getBean(EtudiantDAO.class);
        EtudiantDAO etudiantDAO2 = container.getBean(EtudiantDAO.class);
        System.out.println("EtudiantDAO singleton: " + (etudiantDAO1 == etudiantDAO2)); // Should print true

        EvaluationDAO evaluationDAO1 = container.getBean(EvaluationDAO.class);
        EvaluationDAO evaluationDAO2 = container.getBean(EvaluationDAO.class);
        System.out.println("EvaluationDAO singleton: " + (evaluationDAO1 == evaluationDAO2)); // Should print true

        FiliereDAO filiereDAO1 = container.getBean(FiliereDAO.class);
        FiliereDAO filiereDAO2 = container.getBean(FiliereDAO.class);
        System.out.println("FiliereDAO singleton: " + (filiereDAO1 == filiereDAO2)); // Should print true

        ModaliteEvaluationDAO modaliteEvaluationDAO1 = container.getBean(ModaliteEvaluationDAO.class);
        ModaliteEvaluationDAO modaliteEvaluationDAO2 = container.getBean(ModaliteEvaluationDAO.class);
        System.out.println("ModaliteEvaluationDAO singleton: " + (modaliteEvaluationDAO1 == modaliteEvaluationDAO2)); // Should print true

        ModuleDAO moduleDAO1 = container.getBean(ModuleDAO.class);
        ModuleDAO moduleDAO2 = container.getBean(ModuleDAO.class);
        System.out.println("ModuleDAO singleton: " + (moduleDAO1 == moduleDAO2)); // Should print true

        ProfesseurDAO professeurDAO1 = container.getBean(ProfesseurDAO.class);
        ProfesseurDAO professeurDAO2 = container.getBean(ProfesseurDAO.class);
        System.out.println("ProfesseurDAO singleton: " + (professeurDAO1 == professeurDAO2)); // Should print true

        UtilisateurDAO utilisateurDAO1 = container.getBean(UtilisateurDAO.class);
        UtilisateurDAO utilisateurDAO2 = container.getBean(UtilisateurDAO.class);
        System.out.println("UtilisateurDAO singleton: " + (utilisateurDAO1 == utilisateurDAO2)); // Should print true

    }
}
