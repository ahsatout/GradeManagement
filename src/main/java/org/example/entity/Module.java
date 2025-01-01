package org.example.entity;

import java.util.ArrayList;
import java.util.List;

public class Module {
    private Long id;
    private String code;
    private String nom;
    private Filiere filiere;
    private Semestre semestre;
    private List<ElementModule> elementModules;

    // Default constructor
    public Module() {
        this.elementModules = new ArrayList<>();
    }

    // Full constructor
    public Module(Long id, String code, String nom, Filiere filiere,
                  Semestre semestre, List<ElementModule> elementModules) {
        this.id = id;
        this.code = code;
        this.nom = nom;
        this.filiere = filiere;
        this.semestre = semestre;
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

    public Filiere getFiliere() {
        return filiere;
    }

    public void setFiliere(Filiere filiere) {
        this.filiere = filiere;
    }

    public Semestre getSemestre() {
        return semestre;
    }

    public void setSemestre(Semestre semestre) {
        this.semestre = semestre;
    }

    public List<ElementModule> getElementModules() {
        return elementModules;
    }

    public void setElementModules(List<ElementModule> elementModules) {
        this.elementModules = elementModules;
    }

    @Override
    public String toString() {
        return "Module{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", nom='" + nom + '\'' +
                ", filiere=" + filiere +
                ", semestre=" + semestre +
                ", elementModules=" + elementModules +
                '}';
    }

    //Builder Class
    public static class Builder {
        private Long id;
        private String code;
        private String nom;
        private Filiere filiere;
        private Semestre semestre;
        private List<ElementModule> elementModules = new ArrayList<>();

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder code(String code) {
            this.code = code;
            return this;
        }

        public Builder nom(String nom) {
            this.nom = nom;
            return this;
        }

        public Builder filiere(Filiere filiere) {
            this.filiere = filiere;
            return this;
        }

        public Builder semestre(Semestre semestre) {
            this.semestre = semestre;
            return this;
        }

        public Builder elementModules(List<ElementModule> elementModules) {
            this.elementModules = elementModules != null ? elementModules : new ArrayList<>();
            return this;
        }

        public Builder addElementModule(ElementModule elementModule) {
            this.elementModules.add(elementModule);
            return this;
        }

        public Module build() {
            return new Module(id, code, nom, filiere, semestre, elementModules);
        }
    }
}
