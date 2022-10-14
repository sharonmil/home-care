package com.silvana.homecare.service;

import com.silvana.homecare.domain.SignosVitales;
import com.silvana.homecare.repository.SignosVitalesRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SignosVitales}.
 */
@Service
@Transactional
public class SignosVitalesService {

    private final Logger log = LoggerFactory.getLogger(SignosVitalesService.class);

    private final SignosVitalesRepository signosVitalesRepository;

    public SignosVitalesService(SignosVitalesRepository signosVitalesRepository) {
        this.signosVitalesRepository = signosVitalesRepository;
    }

    /**
     * Save a signosVitales.
     *
     * @param signosVitales the entity to save.
     * @return the persisted entity.
     */
    public SignosVitales save(SignosVitales signosVitales) {
        log.debug("Request to save SignosVitales : {}", signosVitales);
        return signosVitalesRepository.save(signosVitales);
    }

    /**
     * Partially update a signosVitales.
     *
     * @param signosVitales the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SignosVitales> partialUpdate(SignosVitales signosVitales) {
        log.debug("Request to partially update SignosVitales : {}", signosVitales);

        return signosVitalesRepository
            .findById(signosVitales.getId())
            .map(existingSignosVitales -> {
                if (signosVitales.getOximetria() != null) {
                    existingSignosVitales.setOximetria(signosVitales.getOximetria());
                }
                if (signosVitales.getFrecuenciaRespiratoria() != null) {
                    existingSignosVitales.setFrecuenciaRespiratoria(signosVitales.getFrecuenciaRespiratoria());
                }
                if (signosVitales.getFrecuenciaCardiaca() != null) {
                    existingSignosVitales.setFrecuenciaCardiaca(signosVitales.getFrecuenciaCardiaca());
                }
                if (signosVitales.getPresion() != null) {
                    existingSignosVitales.setPresion(signosVitales.getPresion());
                }
                if (signosVitales.getGlicemia() != null) {
                    existingSignosVitales.setGlicemia(signosVitales.getGlicemia());
                }
                if (signosVitales.getCreatedDate() != null) {
                    existingSignosVitales.setCreatedDate(signosVitales.getCreatedDate());
                }

                return existingSignosVitales;
            })
            .map(signosVitalesRepository::save);
    }

    /**
     * Get all the signosVitales.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<SignosVitales> findAll() {
        log.debug("Request to get all SignosVitales");
        return signosVitalesRepository.findAll();
    }

    /**
     * Get one signosVitales by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SignosVitales> findOne(Long id) {
        log.debug("Request to get SignosVitales : {}", id);
        return signosVitalesRepository.findById(id);
    }

    /**
     * Delete the signosVitales by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SignosVitales : {}", id);
        signosVitalesRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<SignosVitales> findSignosVitalesFromRange(LocalDate from, LocalDate to) {
        log.debug("Request to get SignosVitales from range : {} {}", from, to);
        return signosVitalesRepository.findSignosVitalesByCreatedDateBetween(from, to);
    }
}
