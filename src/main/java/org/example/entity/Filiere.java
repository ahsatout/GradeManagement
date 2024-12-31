package org.example.entity;

import java.util.ArrayList;
import java.util.List;

public class Filiere {
    private Long id;
    private String code;
    private String nom;
    private List<Module> modules;
    private List<Etudiant> etudiants;

    // Default constructor
    public Filiere() {
        this.modules = new ArrayList<>();
        this.etudiants = new ArrayList<>();
    }

    // Full constructor
    public Filiere(Long id, String code, String nom, List<Module> modules, List<Etudiant> etudiants) {
        this.id = id;
        this.code = code;
        this.nom = nom;
        this.modules = modules != null ? modules : new ArrayList<>();
        this.etudiants = etudiants != null ? etudiants : new ArrayList<>();
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

    public List<Module> getModules() {
        return modules;
    }

    public void setModules(List<Module> modules) {
        this.modules = modules;
    }

    public List<Etudiant> getEtudiants() {
        return etudiants;
    }

    public void setEtudiants(List<Etudiant> etudiants) {
        this.etudiants = etudiants;
    }

    @Override
    public String toString() {
        return "Filiere{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", nom='" + nom + '\'' +
                ", modules=" + modules +
                ", etudiants=" + etudiants +
                '}';
    }
}
