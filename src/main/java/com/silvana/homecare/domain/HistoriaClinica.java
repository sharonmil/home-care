package com.silvana.homecare.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A HistoriaClinica.
 */
@Entity
@Table(name = "historia_clinica")
@JsonIgnoreProperties(value = { "familiar", "medico" })
    public class HistoriaClinica implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 2000)
    @Column(name = "sugerencia", length = 2000, nullable = false)
    private String sugerencia;

    @Size(max = 2000)
    @Column(name = "diagnostico", length = 2000)
    private String diagnostico;

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

    public HistoriaClinica id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSugerencia() {
        return this.sugerencia;
    }

    public HistoriaClinica sugerencia(String sugerencia) {
        this.setSugerencia(sugerencia);
        return this;
    }

    public void setSugerencia(String sugerencia) {
        this.sugerencia = sugerencia;
    }

    public String getDiagnostico() {
        return this.diagnostico;
    }

    public HistoriaClinica diagnostico(String diagnostico) {
        this.setDiagnostico(diagnostico);
        return this;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public Paciente getPaciente() {
        return this.paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public HistoriaClinica paciente(Paciente paciente) {
        this.setPaciente(paciente);
        return this;
    }

    public User getFamiliar() {
        return this.familiar;
    }

    public void setFamiliar(User user) {
        this.familiar = user;
    }

    public HistoriaClinica familiar(User user) {
        this.setFamiliar(user);
        return this;
    }

    public User getMedico() {
        return this.medico;
    }

    public void setMedico(User user) {
        this.medico = user;
    }

    public HistoriaClinica medico(User user) {
        this.setMedico(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HistoriaClinica)) {
            return false;
        }
        return id != null && id.equals(((HistoriaClinica) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HistoriaClinica{" +
            "id=" + getId() +
            ", sugerencia='" + getSugerencia() + "'" +
            ", diagnostico='" + getDiagnostico() + "'" +
            "}";
    }
}
