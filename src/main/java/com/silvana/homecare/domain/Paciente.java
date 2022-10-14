package com.silvana.homecare.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Paciente.
 */
@Entity
@Table(name = "paciente")
public class Paciente implements Serializable {

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

    @OneToOne
    @JoinColumn(unique = true)
    private User username;

    @JsonIgnoreProperties(value = { "paciente" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Familiar familiar;

    @ManyToOne
    @JsonIgnoreProperties(value = { "pacientes" }, allowSetters = true)
    private Medico medico;

    @OneToMany(mappedBy = "paciente")
    @JsonIgnoreProperties(value = { "paciente", "familiar", "medico" }, allowSetters = true)
    private Set<HistoriaClinica> historias = new HashSet<>();

    @OneToMany(mappedBy = "paciente")
    @JsonIgnoreProperties(value = { "paciente", "familiar", "medico" }, allowSetters = true)
    private Set<SignosVitales> historiaSignos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Paciente id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCedula() {
        return this.cedula;
    }

    public Paciente cedula(String cedula) {
        this.setCedula(cedula);
        return this;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getCiudad() {
        return this.ciudad;
    }

    public Paciente ciudad(String ciudad) {
        this.setCiudad(ciudad);
        return this;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getDireccion() {
        return this.direccion;
    }

    public Paciente direccion(String direccion) {
        this.setDireccion(direccion);
        return this;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return this.telefono;
    }

    public Paciente telefono(String telefono) {
        this.setTelefono(telefono);
        return this;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public LocalDate getFechaNacimiento() {
        return this.fechaNacimiento;
    }

    public Paciente fechaNacimiento(LocalDate fechaNacimiento) {
        this.setFechaNacimiento(fechaNacimiento);
        return this;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public User getUsername() {
        return this.username;
    }

    public void setUsername(User user) {
        this.username = user;
    }

    public Paciente username(User user) {
        this.setUsername(user);
        return this;
    }

    public Familiar getFamiliar() {
        return this.familiar;
    }

    public void setFamiliar(Familiar familiar) {
        this.familiar = familiar;
    }

    public Paciente familiar(Familiar familiar) {
        this.setFamiliar(familiar);
        return this;
    }

    public Medico getMedico() {
        return this.medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public Paciente medico(Medico medico) {
        this.setMedico(medico);
        return this;
    }

    public Set<HistoriaClinica> getHistorias() {
        return this.historias;
    }

    public void setHistorias(Set<HistoriaClinica> historiaClinicas) {
        if (this.historias != null) {
            this.historias.forEach(i -> i.setPaciente(null));
        }
        if (historiaClinicas != null) {
            historiaClinicas.forEach(i -> i.setPaciente(this));
        }
        this.historias = historiaClinicas;
    }

    public Paciente historias(Set<HistoriaClinica> historiaClinicas) {
        this.setHistorias(historiaClinicas);
        return this;
    }

    public Paciente addHistorias(HistoriaClinica historiaClinica) {
        this.historias.add(historiaClinica);
        historiaClinica.setPaciente(this);
        return this;
    }

    public Paciente removeHistorias(HistoriaClinica historiaClinica) {
        this.historias.remove(historiaClinica);
        historiaClinica.setPaciente(null);
        return this;
    }

    public Set<SignosVitales> getHistoriaSignos() {
        return this.historiaSignos;
    }

    public void setHistoriaSignos(Set<SignosVitales> signosVitales) {
        if (this.historiaSignos != null) {
            this.historiaSignos.forEach(i -> i.setPaciente(null));
        }
        if (signosVitales != null) {
            signosVitales.forEach(i -> i.setPaciente(this));
        }
        this.historiaSignos = signosVitales;
    }

    public Paciente historiaSignos(Set<SignosVitales> signosVitales) {
        this.setHistoriaSignos(signosVitales);
        return this;
    }

    public Paciente addHistoriaSignos(SignosVitales signosVitales) {
        this.historiaSignos.add(signosVitales);
        signosVitales.setPaciente(this);
        return this;
    }

    public Paciente removeHistoriaSignos(SignosVitales signosVitales) {
        this.historiaSignos.remove(signosVitales);
        signosVitales.setPaciente(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Paciente)) {
            return false;
        }
        return id != null && id.equals(((Paciente) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Paciente{" +
            "id=" + getId() +
            ", cedula='" + getCedula() + "'" +
            ", ciudad='" + getCiudad() + "'" +
            ", direccion='" + getDireccion() + "'" +
            ", telefono='" + getTelefono() + "'" +
            ", fechaNacimiento='" + getFechaNacimiento() + "'" +
            "}";
    }
}
