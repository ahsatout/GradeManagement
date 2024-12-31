package org.example.entity;

import java.util.ArrayList;
import java.util.List;

public class Professeur {
    private Long id;
    private String code;
    private String nom;
    private String prenom;
    private String specialite;
    private Utilisateur utilisateur;
    private List<ElementModule> elementModules;

    // Default constructor
    public Professeur() {
        this.elementModules = new ArrayList<>();
    }

    // Full constructor
    public Professeur(Long id, String code, String nom, String prenom, String specialite,
                      Utilisateur utilisateur, List<ElementModule> elementModules) {
        this.id = id;
        this.code = code;
        this.nom = nom;
        this.prenom = prenom;
        this.specialite = specialite;
        this.utilisateur = utilisateur;
        this.elementModules = elementModules != null ? elementModules : new ArrayList<>();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public String getSpecialite() {
        return specialite;
    }

    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public List<ElementModule> getElementModules() {
        return elementModules;
    }

    public void setElementModules(List<ElementModule> elementModules) {
        this.elementModules = elementModules;
    }
}
