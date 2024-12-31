package org.example.entity;

import java.util.ArrayList;
import java.util.List;

public class ModaliteEvaluation {
    private Long id;
    private Type type;
    private Float coefficient;
    private ElementModule elementModule;
    private List<Evaluation> evaluations;



    // Default constructor
    public ModaliteEvaluation() {
        this.evaluations = new ArrayList<>();
    }

    // Full constructor
    public ModaliteEvaluation(Long id, Type type, Float coefficient,
                              ElementModule elementModule, List<Evaluation> evaluations) {
        this.id = id;
        this.type = type;
        this.coefficient = coefficient;
        this.elementModule = elementModule;
        this.evaluations = evaluations != null ? evaluations : new ArrayList<>();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Float getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(Float coefficient) {
        this.coefficient = coefficient;
    }

    public ElementModule getElementModule() {
        return elementModule;
    }

    public void setElementModule(ElementModule elementModule) {
        this.elementModule = elementModule;
    }

    public List<Evaluation> getEvaluations() {
        return evaluations;
    }

    public void setEvaluations(List<Evaluation> evaluations) {
        this.evaluations = evaluations;
    }
}
