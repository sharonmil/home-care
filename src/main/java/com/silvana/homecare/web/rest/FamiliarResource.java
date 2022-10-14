package com.silvana.homecare.web.rest;

import com.silvana.homecare.domain.Familiar;
import com.silvana.homecare.repository.FamiliarRepository;
import com.silvana.homecare.service.FamiliarService;
import com.silvana.homecare.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
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
 * REST controller for managing {@link com.silvana.homecare.domain.Familiar}.
 */
@RestController
@RequestMapping("/api")
public class FamiliarResource {

    private final Logger log = LoggerFactory.getLogger(FamiliarResource.class);

    private static final String ENTITY_NAME = "familiar";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FamiliarService familiarService;

    private final FamiliarRepository familiarRepository;

    public FamiliarResource(FamiliarService familiarService, FamiliarRepository familiarRepository) {
        this.familiarService = familiarService;
        this.familiarRepository = familiarRepository;
    }

    /**
     * {@code POST  /familiars} : Create a new familiar.
     *
     * @param familiar the familiar to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new familiar, or with status {@code 400 (Bad Request)} if the familiar has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/familiars")
    public ResponseEntity<Familiar> createFamiliar(@Valid @RequestBody Familiar familiar) throws URISyntaxException {
        log.debug("REST request to save Familiar : {}", familiar);
        if (familiar.getId() != null) {
            throw new BadRequestAlertException("A new familiar cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Familiar result = familiarService.save(familiar);
        return ResponseEntity
            .created(new URI("/api/familiars/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /familiars/:id} : Updates an existing familiar.
     *
     * @param id the id of the familiar to save.
     * @param familiar the familiar to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated familiar,
     * or with status {@code 400 (Bad Request)} if the familiar is not valid,
     * or with status {@code 500 (Internal Server Error)} if the familiar couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/familiars/{id}")
    public ResponseEntity<Familiar> updateFamiliar(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Familiar familiar
    ) throws URISyntaxException {
        log.debug("REST request to update Familiar : {}, {}", id, familiar);
        if (familiar.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, familiar.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!familiarRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Familiar result = familiarService.save(familiar);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, familiar.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /familiars/:id} : Partial updates given fields of an existing familiar, field will ignore if it is null
     *
     * @param id the id of the familiar to save.
     * @param familiar the familiar to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated familiar,
     * or with status {@code 400 (Bad Request)} if the familiar is not valid,
     * or with status {@code 404 (Not Found)} if the familiar is not found,
     * or with status {@code 500 (Internal Server Error)} if the familiar couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/familiars/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Familiar> partialUpdateFamiliar(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Familiar familiar
    ) throws URISyntaxException {
        log.debug("REST request to partial update Familiar partially : {}, {}", id, familiar);
        if (familiar.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, familiar.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!familiarRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Familiar> result = familiarService.partialUpdate(familiar);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, familiar.getId().toString())
        );
    }

    /**
     * {@code GET  /familiars} : get all the familiars.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of familiars in body.
     */
    @GetMapping("/familiars")
    public List<Familiar> getAllFamiliars(@RequestParam(required = false) String filter) {
        if ("paciente-is-null".equals(filter)) {
            log.debug("REST request to get all Familiars where paciente is null");
            return familiarService.findAllWherePacienteIsNull();
        }
        log.debug("REST request to get all Familiars");
        return familiarService.findAll();
    }

    /**
     * {@code GET  /familiars/:id} : get the "id" familiar.
     *
     * @param id the id of the familiar to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the familiar, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/familiars/{id}")
    public ResponseEntity<Familiar> getFamiliar(@PathVariable Long id) {
        log.debug("REST request to get Familiar : {}", id);
        Optional<Familiar> familiar = familiarService.findOne(id);
        return ResponseUtil.wrapOrNotFound(familiar);
    }

    /**
     * {@code DELETE  /familiars/:id} : delete the "id" familiar.
     *
     * @param id the id of the familiar to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/familiars/{id}")
    public ResponseEntity<Void> deleteFamiliar(@PathVariable Long id) {
        log.debug("REST request to delete Familiar : {}", id);
        familiarService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
