package com.silvana.homecare.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Medico.
 */
@Entity
@Table(name = "medico")
public class Medico implements Serializable {

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

    @Size(min = 2)
    @Column(name = "especialidad", nullable = true)
    private String especialidad;

    @OneToOne
    @JoinColumn(unique = true)
    private User username;

    @OneToMany(mappedBy = "medico")
    @JsonIgnoreProperties(value = { "username", "medico", "historias", "historiaSignos" }, allowSetters = true)
    private Set<Paciente> pacientes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Medico id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCedula() {
        return this.cedula;
    }

    public Medico cedula(String cedula) {
        this.setCedula(cedula);
        return this;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getCiudad() {
        return this.ciudad;
    }

    public Medico ciudad(String ciudad) {
        this.setCiudad(ciudad);
        return this;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getDireccion() {
        return this.direccion;
    }

    public Medico direccion(String direccion) {
        this.setDireccion(direccion);
        return this;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return this.telefono;
    }

    public Medico telefono(String telefono) {
        this.setTelefono(telefono);
        return this;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public LocalDate getFechaNacimiento() {
        return this.fechaNacimiento;
    }

    public Medico fechaNacimiento(LocalDate fechaNacimiento) {
        this.setFechaNacimiento(fechaNacimiento);
        return this;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getEspecialidad() {
        return this.especialidad;
    }

    public Medico especialidad(String especialidad) {
        this.setEspecialidad(especialidad);
        return this;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public User getUsername() {
        return this.username;
    }

    public void setUsername(User user) {
        this.username = user;
    }

    public Medico username(User user) {
        this.setUsername(user);
        return this;
    }

    public Set<Paciente> getPacientes() {
        return this.pacientes;
    }

    public void setPacientes(Set<Paciente> pacientes) {
        if (this.pacientes != null) {
            this.pacientes.forEach(i -> i.setMedico(null));
        }
        if (pacientes != null) {
            pacientes.forEach(i -> i.setMedico(this));
        }
        this.pacientes = pacientes;
    }

    public Medico pacientes(Set<Paciente> pacientes) {
        this.setPacientes(pacientes);
        return this;
    }

    public Medico addPacientes(Paciente paciente) {
        this.pacientes.add(paciente);
        paciente.setMedico(this);
        return this;
    }

    public Medico removePacientes(Paciente paciente) {
        this.pacientes.remove(paciente);
        paciente.setMedico(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Medico)) {
            return false;
        }
        return id != null && id.equals(((Medico) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Medico{" +
            "id=" + getId() +
            ", cedula='" + getCedula() + "'" +
            ", ciudad='" + getCiudad() + "'" +
            ", direccion='" + getDireccion() + "'" +
            ", telefono='" + getTelefono() + "'" +
            ", fechaNacimiento='" + getFechaNacimiento() + "'" +
            ", especialidad='" + getEspecialidad() + "'" +
            "}";
    }
}
