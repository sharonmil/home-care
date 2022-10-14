package com.silvana.homecare.web.rest;

import com.silvana.homecare.domain.HistoriaClinica;
import com.silvana.homecare.repository.HistoriaClinicaRepository;
import com.silvana.homecare.service.HistoriaClinicaService;
import com.silvana.homecare.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.silvana.homecare.domain.HistoriaClinica}.
 */
@RestController
@RequestMapping("/api")
public class HistoriaClinicaResource {

    private final Logger log = LoggerFactory.getLogger(HistoriaClinicaResource.class);

    private static final String ENTITY_NAME = "historiaClinica";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HistoriaClinicaService historiaClinicaService;

    private final HistoriaClinicaRepository historiaClinicaRepository;

    public HistoriaClinicaResource(HistoriaClinicaService historiaClinicaService, HistoriaClinicaRepository historiaClinicaRepository) {
        this.historiaClinicaService = historiaClinicaService;
        this.historiaClinicaRepository = historiaClinicaRepository;
    }

    /**
     * {@code POST  /historia-clinicas} : Create a new historiaClinica.
     *
     * @param historiaClinica the historiaClinica to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new historiaClinica, or with status {@code 400 (Bad Request)} if the historiaClinica has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/historia-clinicas")
    public ResponseEntity<HistoriaClinica> createHistoriaClinica(@Valid @RequestBody HistoriaClinica historiaClinica)
        throws URISyntaxException {
        log.debug("REST request to save HistoriaClinica : {}", historiaClinica);
        if (historiaClinica.getId() != null) {
            throw new BadRequestAlertException("A new historiaClinica cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HistoriaClinica result = historiaClinicaService.save(historiaClinica);
        return ResponseEntity
            .created(new URI("/api/historia-clinicas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /historia-clinicas/:id} : Updates an existing historiaClinica.
     *
     * @param id the id of the historiaClinica to save.
     * @param historiaClinica the historiaClinica to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated historiaClinica,
     * or with status {@code 400 (Bad Request)} if the historiaClinica is not valid,
     * or with status {@code 500 (Internal Server Error)} if the historiaClinica couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/historia-clinicas/{id}")
    public ResponseEntity<HistoriaClinica> updateHistoriaClinica(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody HistoriaClinica historiaClinica
    ) throws URISyntaxException {
        log.debug("REST request to update HistoriaClinica : {}, {}", id, historiaClinica);
        if (historiaClinica.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, historiaClinica.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!historiaClinicaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        HistoriaClinica result = historiaClinicaService.save(historiaClinica);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, historiaClinica.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /historia-clinicas/:id} : Partial updates given fields of an existing historiaClinica, field will ignore if it is null
     *
     * @param id the id of the historiaClinica to save.
     * @param historiaClinica the historiaClinica to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated historiaClinica,
     * or with status {@code 400 (Bad Request)} if the historiaClinica is not valid,
     * or with status {@code 404 (Not Found)} if the historiaClinica is not found,
     * or with status {@code 500 (Internal Server Error)} if the historiaClinica couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/historia-clinicas/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<HistoriaClinica> partialUpdateHistoriaClinica(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody HistoriaClinica historiaClinica
    ) throws URISyntaxException {
        log.debug("REST request to partial update HistoriaClinica partially : {}, {}", id, historiaClinica);
        if (historiaClinica.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, historiaClinica.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!historiaClinicaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HistoriaClinica> result = historiaClinicaService.partialUpdate(historiaClinica);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, historiaClinica.getId().toString())
        );
    }

    /**
     * {@code GET  /historia-clinicas} : get all the historiaClinicas.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of historiaClinicas in body.
     */
    @GetMapping("/historia-clinicas")
    public List<HistoriaClinica> getAllHistoriaClinicas() {
        log.debug("REST request to get all HistoriaClinicas");
        return historiaClinicaService.findAll();
    }

    /**
     * {@code GET  /historia-clinicas/:id} : get the "id" historiaClinica.
     *
     * @param id the id of the historiaClinica to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the historiaClinica, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/historia-clinicas/{id}")
    public ResponseEntity<HistoriaClinica> getHistoriaClinica(@PathVariable Long id) {
        log.debug("REST request to get HistoriaClinica : {}", id);
        Optional<HistoriaClinica> historiaClinica = historiaClinicaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(historiaClinica);
    }

    /**
     * {@code DELETE  /historia-clinicas/:id} : delete the "id" historiaClinica.
     *
     * @param id the id of the historiaClinica to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/historia-clinicas/{id}")
    public ResponseEntity<Void> deleteHistoriaClinica(@PathVariable Long id) {
        log.debug("REST request to delete HistoriaClinica : {}", id);
        historiaClinicaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
