package org.example.entity;

import java.util.ArrayList;
import java.util.List;

public class Etudiant {
    private Long id;
    private String cne;
    private String nom;
    private String prenom;
    private Filiere filiere;
    private List<Evaluation> evaluations;

    // Default constructor
    public Etudiant() {
        this.evaluations = new ArrayList<>();
    }

    // Full constructor
    public Etudiant(Long id, String cne, String nom, String prenom,
                    Filiere filiere, List<Evaluation> evaluations) {
        this.id = id;
        this.cne = cne;
        this.nom = nom;
        this.prenom = prenom;
        this.filiere = filiere;
        this.evaluations = evaluations != null ? evaluations : new ArrayList<>();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCne() {
        return cne;
    }

    public void setCne(String cne) {
        this.cne = cne;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public Filiere getFiliere() {
        return filiere;
    }

    public void setFiliere(Filiere filiere) {
        this.filiere = filiere;
    }

    public List<Evaluation> getEvaluations() {
        return evaluations;
    }

    public void setEvaluations(List<Evaluation> evaluations) {
        this.evaluations = evaluations;
    }
}
