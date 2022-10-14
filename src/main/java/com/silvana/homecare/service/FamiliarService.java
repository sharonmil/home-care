package com.silvana.homecare.service;

import com.silvana.homecare.domain.Familiar;
import com.silvana.homecare.repository.FamiliarRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Familiar}.
 */
@Service
@Transactional
public class FamiliarService {

    private final Logger log = LoggerFactory.getLogger(FamiliarService.class);

    private final FamiliarRepository familiarRepository;

    public FamiliarService(FamiliarRepository familiarRepository) {
        this.familiarRepository = familiarRepository;
    }

    /**
     * Save a familiar.
     *
     * @param familiar the entity to save.
     * @return the persisted entity.
     */
    public Familiar save(Familiar familiar) {
        log.debug("Request to save Familiar : {}", familiar);
        return familiarRepository.save(familiar);
    }

    /**
     * Partially update a familiar.
     *
     * @param familiar the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Familiar> partialUpdate(Familiar familiar) {
        log.debug("Request to partially update Familiar : {}", familiar);

        return familiarRepository
            .findById(familiar.getId())
            .map(existingFamiliar -> {
                if (familiar.getCedula() != null) {
                    existingFamiliar.setCedula(familiar.getCedula());
                }
                if (familiar.getCiudad() != null) {
                    existingFamiliar.setCiudad(familiar.getCiudad());
                }
                if (familiar.getDireccion() != null) {
                    existingFamiliar.setDireccion(familiar.getDireccion());
                }
                if (familiar.getTelefono() != null) {
                    existingFamiliar.setTelefono(familiar.getTelefono());
                }
                if (familiar.getFechaNacimiento() != null) {
                    existingFamiliar.setFechaNacimiento(familiar.getFechaNacimiento());
                }
                if (familiar.getParentesco() != null) {
                    existingFamiliar.setParentesco(familiar.getParentesco());
                }

                return existingFamiliar;
            })
            .map(familiarRepository::save);
    }

    /**
     * Get all the familiars.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Familiar> findAll() {
        log.debug("Request to get all Familiars");
        return familiarRepository.findAll();
    }

    /**
     *  Get all the familiars where Paciente is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Familiar> findAllWherePacienteIsNull() {
        log.debug("Request to get all familiars where Paciente is null");
        return StreamSupport
            .stream(familiarRepository.findAll().spliterator(), false)
            .filter(familiar -> familiar.getPaciente() == null)
            .collect(Collectors.toList());
    }

    /**
     * Get one familiar by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Familiar> findOne(Long id) {
        log.debug("Request to get Familiar : {}", id);
        return familiarRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Familiar> findOneByEmailPaciente(String mail) {
        log.debug("Request to get Familiar by Paciente Email: {}", mail);
        return familiarRepository.findByPaciente_Username_Email(mail);
    }

    /**
     * Delete the familiar by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Familiar : {}", id);
        familiarRepository.deleteById(id);
    }
}
