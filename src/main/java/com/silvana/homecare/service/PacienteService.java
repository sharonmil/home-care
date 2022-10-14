package com.silvana.homecare.service;

import com.silvana.homecare.domain.Paciente;
import com.silvana.homecare.repository.PacienteRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Paciente}.
 */
@Service
@Transactional
public class PacienteService {

    private final Logger log = LoggerFactory.getLogger(PacienteService.class);

    private final PacienteRepository pacienteRepository;

    public PacienteService(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    /**
     * Save a paciente.
     *
     * @param paciente the entity to save.
     * @return the persisted entity.
     */
    public Paciente save(Paciente paciente) {
        log.debug("Request to save Paciente : {}", paciente);
        return pacienteRepository.save(paciente);
    }

    /**
     * Partially update a paciente.
     *
     * @param paciente the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Paciente> partialUpdate(Paciente paciente) {
        log.debug("Request to partially update Paciente : {}", paciente);

        return pacienteRepository
            .findById(paciente.getId())
            .map(existingPaciente -> {
                if (paciente.getCedula() != null) {
                    existingPaciente.setCedula(paciente.getCedula());
                }
                if (paciente.getCiudad() != null) {
                    existingPaciente.setCiudad(paciente.getCiudad());
                }
                if (paciente.getDireccion() != null) {
                    existingPaciente.setDireccion(paciente.getDireccion());
                }
                if (paciente.getTelefono() != null) {
                    existingPaciente.setTelefono(paciente.getTelefono());
                }
                if (paciente.getFechaNacimiento() != null) {
                    existingPaciente.setFechaNacimiento(paciente.getFechaNacimiento());
                }

                return existingPaciente;
            })
            .map(pacienteRepository::save);
    }

    /**
     * Get all the pacientes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Paciente> findAll() {
        log.debug("Request to get all Pacientes");
        return pacienteRepository.findAll();
    }

    /**
     * Get one paciente by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Paciente> findOne(Long id) {
        log.debug("Request to get Paciente : {}", id);
        return pacienteRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Paciente> findByEmail(String email) {
        log.debug("Request to find paciente by email");
        return pacienteRepository.findByUsername_Email(email);
    }
    /**
     * Delete the paciente by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Paciente : {}", id);
        pacienteRepository.deleteById(id);
    }
}
