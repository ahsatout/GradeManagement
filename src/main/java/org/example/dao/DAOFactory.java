package org.example.dao;


public class DAOFactory {
    private static final DAOFactory INSTANCE = new DAOFactory();

    private DAOFactory() {
    }

    public static DAOFactory getInstance() {
        return INSTANCE;
    }

    public <T extends AbstractDAO<?>> T getDAO(Class<T> entityClass) {
        if (entityClass == EtudiantDAO.class) {
            return (T) new EtudiantDAO();
        } else if (entityClass == ProfesseurDAO.class) {
            return (T) new ProfesseurDAO();
        } else if (entityClass == ModuleDAO.class) {
            return (T) new ModuleDAO();
        } else if (entityClass == FiliereDAO.class) {
            return (T) new FiliereDAO();
        } else if (entityClass == EvaluationDAO.class) {
            return (T) new EvaluationDAO();
        } else if (entityClass == ElementModuleDAO.class) {
            return (T) new ElementModuleDAO();
        } else if (entityClass == ModaliteEvaluationDAO.class) {
            return (T) new ModaliteEvaluationDAO();
        } else if (entityClass == UtilisateurDAO.class) {
            return (T) new UtilisateurDAO(new ProfesseurDAO());
        } else {
            throw new IllegalArgumentException("Unknown DAO class: " + entityClass);
        }
    }
}
