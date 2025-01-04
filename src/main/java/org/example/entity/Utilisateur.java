package org.example.entity;

public class Utilisateur {
    private Long id;
    private String login;
    private String password;
    private Role role;
    private Professeur professeur;



    // Default constructor
    public Utilisateur() {}

    // Full constructor
    public Utilisateur(Long id, String login, String password, Role role, Professeur professeur) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.role = role;
        this.professeur = professeur;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Professeur getProfesseur() {
        return professeur;
    }

    public void setProfesseur(Professeur professeur) {
        this.professeur = professeur;
    }

    @Override
    public String toString() {
        return "Utilisateur{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                ", professeur=" + professeur+
                '}';
    }
}
