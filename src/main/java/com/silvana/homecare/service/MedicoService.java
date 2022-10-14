package com.silvana.homecare.service;

import com.silvana.homecare.domain.Medico;
import com.silvana.homecare.repository.MedicoRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Medico}.
 */
@Service
@Transactional
public class MedicoService {

    private final Logger log = LoggerFactory.getLogger(MedicoService.class);

    private final MedicoRepository medicoRepository;

    public MedicoService(MedicoRepository medicoRepository) {
        this.medicoRepository = medicoRepository;
    }

    /**
     * Save a medico.
     *
     * @param medico the entity to save.
     * @return the persisted entity.
     */
    public Medico save(Medico medico) {
        log.debug("Request to save Medico : {}", medico);
        return medicoRepository.save(medico);
    }

    /**
     * Partially update a medico.
     *
     * @param medico the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Medico> partialUpdate(Medico medico) {
        log.debug("Request to partially update Medico : {}", medico);

        return medicoRepository
            .findById(medico.getId())
            .map(existingMedico -> {
                if (medico.getCedula() != null) {
                    existingMedico.setCedula(medico.getCedula());
                }
                if (medico.getCiudad() != null) {
                    existingMedico.setCiudad(medico.getCiudad());
                }
                if (medico.getDireccion() != null) {
                    existingMedico.setDireccion(medico.getDireccion());
                }
                if (medico.getTelefono() != null) {
                    existingMedico.setTelefono(medico.getTelefono());
                }
                if (medico.getFechaNacimiento() != null) {
                    existingMedico.setFechaNacimiento(medico.getFechaNacimiento());
                }
                if (medico.getEspecialidad() != null) {
                    existingMedico.setEspecialidad(medico.getEspecialidad());
                }

                return existingMedico;
            })
            .map(medicoRepository::save);
    }

    /**
     * Get all the medicos.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Medico> findAll() {
        log.debug("Request to get all Medicos");
        return medicoRepository.findAll();
    }

    /**
     * Get one medico by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Medico> findOne(Long id) {
        log.debug("Request to get Medico : {}", id);
        return medicoRepository.findById(id);
    }

    /**
     * Delete the medico by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Medico : {}", id);
        medicoRepository.deleteById(id);
    }
}
