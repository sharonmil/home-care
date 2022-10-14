package com.silvana.homecare.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A SignosVitales.
 */
@Entity
@Table(name = "signos_vitales")
public class SignosVitales implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Min(value = 1)
    @Max(value = 100)
    @Column(name = "oximetria")
    private Integer oximetria;

    @Min(value = 1)
    @Max(value = 300)
    @Column(name = "frecuencia_respiratoria")
    private Integer frecuenciaRespiratoria;

    @Min(value = 5)
    @Max(value = 300)
    @Column(name = "frecuencia_cardiaca")
    private Integer frecuenciaCardiaca;

    @Size(min = 2)
    @Column(name = "presion")
    private String presion;

    @Min(value = 1)
    @Max(value = 600)
    @Column(name = "glicemia")
    private Integer glicemia;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @ManyToOne
    @JsonIgnoreProperties(value = { "historias", "historiaSignos" }, allowSetters = true)
    private Paciente paciente;

    @ManyToOne
    private User familiar;

    @ManyToOne
    private User medico;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SignosVitales id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getOximetria() {
        return this.oximetria;
    }

    public SignosVitales oximetria(Integer oximetria) {
        this.setOximetria(oximetria);
        return this;
    }

    public void setOximetria(Integer oximetria) {
        this.oximetria = oximetria;
    }

    public Integer getFrecuenciaRespiratoria() {
        return this.frecuenciaRespiratoria;
    }

    public SignosVitales frecuenciaRespiratoria(Integer frecuenciaRespiratoria) {
        this.setFrecuenciaRespiratoria(frecuenciaRespiratoria);
        return this;
    }

    public void setFrecuenciaRespiratoria(Integer frecuenciaRespiratoria) {
        this.frecuenciaRespiratoria = frecuenciaRespiratoria;
    }

    public Integer getFrecuenciaCardiaca() {
        return this.frecuenciaCardiaca;
    }

    public SignosVitales frecuenciaCardiaca(Integer frecuenciaCardiaca) {
        this.setFrecuenciaCardiaca(frecuenciaCardiaca);
        return this;
    }

    public void setFrecuenciaCardiaca(Integer frecuenciaCardiaca) {
        this.frecuenciaCardiaca = frecuenciaCardiaca;
    }

    public String getPresion() {
        return this.presion;
    }

    public SignosVitales presion(String presion) {
        this.setPresion(presion);
        return this;
    }

    public void setPresion(String presion) {
        this.presion = presion;
    }

    public Integer getGlicemia() {
        return this.glicemia;
    }

    public SignosVitales glicemia(Integer glicemia) {
        this.setGlicemia(glicemia);
        return this;
    }

    public void setGlicemia(Integer glicemia) {
        this.glicemia = glicemia;
    }

    public LocalDate getCreatedDate() {
        return this.createdDate;
    }

    public SignosVitales createdDate(LocalDate createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public Paciente getPaciente() {
        return this.paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public SignosVitales paciente(Paciente paciente) {
        this.setPaciente(paciente);
        return this;
    }

    public User getFamiliar() {
        return this.familiar;
    }

    public void setFamiliar(User user) {
        this.familiar = user;
    }

    public SignosVitales familiar(User user) {
        this.setFamiliar(user);
        return this;
    }

    public User getMedico() {
        return this.medico;
    }

    public void setMedico(User user) {
        this.medico = user;
    }

    public SignosVitales medico(User user) {
        this.setMedico(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SignosVitales)) {
            return false;
        }
        return id != null && id.equals(((SignosVitales) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SignosVitales{" +
            "id=" + getId() +
            ", oximetria=" + getOximetria() +
            ", frecuenciaRespiratoria=" + getFrecuenciaRespiratoria() +
            ", frecuenciaCardiaca=" + getFrecuenciaCardiaca() +
            ", presion='" + getPresion() + "'" +
            ", glicemia=" + getGlicemia() +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }
}
