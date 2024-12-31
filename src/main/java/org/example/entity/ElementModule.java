package org.example.entity;

import java.util.ArrayList;
import java.util.List;

public class ElementModule {
    private Long id;
    private String nom;
    private Float coefficient;
    private Module module;
    private Professeur professeur;
    private List<ModaliteEvaluation> modaliteEvaluations;

    // Default constructor
    public ElementModule() {
        this.modaliteEvaluations = new ArrayList<>();
    }

    // Full constructor
    public ElementModule(Long id, String nom, Float coefficient, Module module,
                         Professeur professeur, List<ModaliteEvaluation> modaliteEvaluations) {
        this.id = id;
        this.nom = nom;
        this.coefficient = coefficient;
        this.module = module;
        this.professeur = professeur;
        this.modaliteEvaluations = modaliteEvaluations != null ? modaliteEvaluations : new ArrayList<>();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Float getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(Float coefficient) {
        this.coefficient = coefficient;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public Professeur getProfesseur() {
        return professeur;
    }

    public void setProfesseur(Professeur professeur) {
        this.professeur = professeur;
    }

    public List<ModaliteEvaluation> getModaliteEvaluations() {
        return modaliteEvaluations;
    }

    public void setModaliteEvaluations(List<ModaliteEvaluation> modaliteEvaluations) {
        this.modaliteEvaluations = modaliteEvaluations;
    }
}
