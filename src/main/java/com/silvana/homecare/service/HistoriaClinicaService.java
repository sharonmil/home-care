package com.silvana.homecare.service;

import com.silvana.homecare.domain.HistoriaClinica;
import com.silvana.homecare.repository.HistoriaClinicaRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link HistoriaClinica}.
 */
@Service
@Transactional
public class HistoriaClinicaService {

    private final Logger log = LoggerFactory.getLogger(HistoriaClinicaService.class);

    private final HistoriaClinicaRepository historiaClinicaRepository;

    private final MailService mailService;

    public HistoriaClinicaService(HistoriaClinicaRepository historiaClinicaRepository, MailService mailService) {
        this.historiaClinicaRepository = historiaClinicaRepository;
        this.mailService = mailService;
    }

    /**
     * Save a historiaClinica.
     *
     * @param historiaClinica the entity to save.
     * @return the persisted entity.
     */
    public HistoriaClinica save(HistoriaClinica historiaClinica) {
        log.debug("Request to save HistoriaClinica : {}", historiaClinica);
        HistoriaClinica historia = historiaClinicaRepository.save(historiaClinica);
        new Thread(() -> mailService.sendSugerenciaCreationEmail(historiaClinica)).start();
        return historia;
    }

    /**
     * Partially update a historiaClinica.
     *
     * @param historiaClinica the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<HistoriaClinica> partialUpdate(HistoriaClinica historiaClinica) {
        log.debug("Request to partially update HistoriaClinica : {}", historiaClinica);

        return historiaClinicaRepository
            .findById(historiaClinica.getId())
            .map(existingHistoriaClinica -> {
                if (historiaClinica.getSugerencia() != null) {
                    existingHistoriaClinica.setSugerencia(historiaClinica.getSugerencia());
                }
                if (historiaClinica.getDiagnostico() != null) {
                    existingHistoriaClinica.setDiagnostico(historiaClinica.getDiagnostico());
                }

                return existingHistoriaClinica;
            })
            .map(historiaClinicaRepository::save);
    }

    /**
     * Get all the historiaClinicas.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<HistoriaClinica> findAll() {
        log.debug("Request to get all HistoriaClinicas");
        return historiaClinicaRepository.findAll();
    }

    /**
     * Get one historiaClinica by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<HistoriaClinica> findOne(Long id) {
        log.debug("Request to get HistoriaClinica : {}", id);
        return historiaClinicaRepository.findById(id);
    }

    /**
     * Delete the historiaClinica by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete HistoriaClinica : {}", id);
        historiaClinicaRepository.deleteById(id);
    }
}
