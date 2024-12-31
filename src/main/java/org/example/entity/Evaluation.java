package org.example.entity;

public class Evaluation {
    private Long id;
    private Float note;
    private Boolean absent;
    private Etudiant etudiant;
    private ModaliteEvaluation modaliteEvaluation;

    // Default constructor
    public Evaluation() {}

    // Full constructor
    public Evaluation(Long id, Float note, Boolean absent,
                      Etudiant etudiant, ModaliteEvaluation modaliteEvaluation) {
        this.id = id;
        this.note = note;
        this.absent = absent;
        this.etudiant = etudiant;
        this.modaliteEvaluation = modaliteEvaluation;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getNote() {
        return note;
    }

    public void setNote(Float note) {
        this.note = note;
    }

    public Boolean getAbsent() {
        return absent;
    }

    public void setAbsent(Boolean absent) {
        this.absent = absent;
    }

    public Etudiant getEtudiant() {
        return etudiant;
    }

    public void setEtudiant(Etudiant etudiant) {
        this.etudiant = etudiant;
    }

    public ModaliteEvaluation getModaliteEvaluation() {
        return modaliteEvaluation;
    }

    public void setModaliteEvaluation(ModaliteEvaluation modaliteEvaluation) {
        this.modaliteEvaluation = modaliteEvaluation;
    }

    @Override
    public String toString() {
        return "Evaluation{" +
                "id=" + id +
                ", note=" + note +
                ", absent=" + absent +
                ", etudiant=" + etudiant +
                ", modaliteEvaluation=" + modaliteEvaluation +
                '}';
    }
}
