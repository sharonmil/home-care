package com.silvana.homecare.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.silvana.homecare.domain.enumeration.Parentesco;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Familiar.
 */
@Entity
@Table(name = "familiar")
public class Familiar implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 5, max = 20)
    @Column(name = "cedula", length = 20, nullable = false)
    private String cedula;

    @NotNull
    @Size(min = 5, max = 20)
    @Column(name = "ciudad", length = 20, nullable = false)
    private String ciudad;

    @NotNull
    @Size(min = 5)
    @Column(name = "direccion", nullable = false)
    private String direccion;

    @NotNull
    @Size(min = 7)
    @Column(name = "telefono", nullable = false)
    private String telefono;

    @NotNull
    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "parentesco", nullable = false)
    private Parentesco parentesco;

    @OneToOne
    @JoinColumn(unique = true)
    private User username;

    @JsonIgnoreProperties(value = { "familiar", "historias", "historiaSignos" }, allowSetters = true)
    @OneToOne(mappedBy = "familiar")
    private Paciente paciente;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Familiar id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCedula() {
        return this.cedula;
    }

    public Familiar cedula(String cedula) {
        this.setCedula(cedula);
        return this;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getCiudad() {
        return this.ciudad;
    }

    public Familiar ciudad(String ciudad) {
        this.setCiudad(ciudad);
        return this;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getDireccion() {
        return this.direccion;
    }

    public Familiar direccion(String direccion) {
        this.setDireccion(direccion);
        return this;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return this.telefono;
    }

    public Familiar telefono(String telefono) {
        this.setTelefono(telefono);
        return this;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public LocalDate getFechaNacimiento() {
        return this.fechaNacimiento;
    }

    public Familiar fechaNacimiento(LocalDate fechaNacimiento) {
        this.setFechaNacimiento(fechaNacimiento);
        return this;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Parentesco getParentesco() {
        return this.parentesco;
    }

    public Familiar parentesco(Parentesco parentesco) {
        this.setParentesco(parentesco);
        return this;
    }

    public void setParentesco(Parentesco parentesco) {
        this.parentesco = parentesco;
    }

    public User getUsername() {
        return this.username;
    }

    public void setUsername(User user) {
        this.username = user;
    }

    public Familiar username(User user) {
        this.setUsername(user);
        return this;
    }

    public Paciente getPaciente() {
        return this.paciente;
    }

    public void setPaciente(Paciente paciente) {
        if (this.paciente != null) {
            this.paciente.setFamiliar(null);
        }
        if (paciente != null) {
            paciente.setFamiliar(this);
        }
        this.paciente = paciente;
    }

    public Familiar paciente(Paciente paciente) {
        this.setPaciente(paciente);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Familiar)) {
            return false;
        }
        return id != null && id.equals(((Familiar) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Familiar{" +
            "id=" + getId() +
            ", cedula='" + getCedula() + "'" +
            ", ciudad='" + getCiudad() + "'" +
            ", direccion='" + getDireccion() + "'" +
            ", telefono='" + getTelefono() + "'" +
            ", fechaNacimiento='" + getFechaNacimiento() + "'" +
            ", parentesco='" + getParentesco() + "'" +
            "}";
    }
}
