package com.silvana.homecare.web.rest;

import com.silvana.homecare.domain.SignosVitales;
import com.silvana.homecare.repository.SignosVitalesRepository;
import com.silvana.homecare.service.SignosVitalesService;
import com.silvana.homecare.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.silvana.homecare.domain.SignosVitales}.
 */
@RestController
@RequestMapping("/api")
public class SignosVitalesResource {

    private final Logger log = LoggerFactory.getLogger(SignosVitalesResource.class);

    private static final String ENTITY_NAME = "signosVitales";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SignosVitalesService signosVitalesService;

    private final SignosVitalesRepository signosVitalesRepository;

    public SignosVitalesResource(SignosVitalesService signosVitalesService, SignosVitalesRepository signosVitalesRepository) {
        this.signosVitalesService = signosVitalesService;
        this.signosVitalesRepository = signosVitalesRepository;
    }

    /**
     * {@code POST  /signos-vitales} : Create a new signosVitales.
     *
     * @param signosVitales the signosVitales to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new signosVitales, or with status {@code 400 (Bad Request)} if the signosVitales has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/signos-vitales")
    public ResponseEntity<SignosVitales> createSignosVitales(@Valid @RequestBody SignosVitales signosVitales) throws URISyntaxException {
        log.debug("REST request to save SignosVitales : {}", signosVitales);
        if (signosVitales.getId() != null) {
            throw new BadRequestAlertException("A new signosVitales cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SignosVitales result = signosVitalesService.save(signosVitales);
        return ResponseEntity
            .created(new URI("/api/signos-vitales/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /signos-vitales/:id} : Updates an existing signosVitales.
     *
     * @param id the id of the signosVitales to save.
     * @param signosVitales the signosVitales to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated signosVitales,
     * or with status {@code 400 (Bad Request)} if the signosVitales is not valid,
     * or with status {@code 500 (Internal Server Error)} if the signosVitales couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/signos-vitales/{id}")
    public ResponseEntity<SignosVitales> updateSignosVitales(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SignosVitales signosVitales
    ) throws URISyntaxException {
        log.debug("REST request to update SignosVitales : {}, {}", id, signosVitales);
        if (signosVitales.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, signosVitales.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!signosVitalesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SignosVitales result = signosVitalesService.save(signosVitales);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, signosVitales.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /signos-vitales/:id} : Partial updates given fields of an existing signosVitales, field will ignore if it is null
     *
     * @param id the id of the signosVitales to save.
     * @param signosVitales the signosVitales to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated signosVitales,
     * or with status {@code 400 (Bad Request)} if the signosVitales is not valid,
     * or with status {@code 404 (Not Found)} if the signosVitales is not found,
     * or with status {@code 500 (Internal Server Error)} if the signosVitales couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/signos-vitales/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SignosVitales> partialUpdateSignosVitales(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SignosVitales signosVitales
    ) throws URISyntaxException {
        log.debug("REST request to partial update SignosVitales partially : {}, {}", id, signosVitales);
        if (signosVitales.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, signosVitales.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!signosVitalesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SignosVitales> result = signosVitalesService.partialUpdate(signosVitales);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, signosVitales.getId().toString())
        );
    }

    /**
     * {@code GET  /signos-vitales} : get all the signosVitales.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of signosVitales in body.
     */
    @GetMapping("/signos-vitales")
    public List<SignosVitales> getAllSignosVitales() {
        log.debug("REST request to get all SignosVitales");
        return signosVitalesService.findAll();
    }

    /**
     * {@code GET  /signos-vitales/:id} : get the "id" signosVitales.
     *
     * @param id the id of the signosVitales to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the signosVitales, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/signos-vitales/{id}")
    public ResponseEntity<SignosVitales> getSignosVitales(@PathVariable Long id) {
        log.debug("REST request to get SignosVitales : {}", id);
        Optional<SignosVitales> signosVitales = signosVitalesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(signosVitales);
    }

    /**
     * {@code DELETE  /signos-vitales/:id} : delete the "id" signosVitales.
     *
     * @param id the id of the signosVitales to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/signos-vitales/{id}")
    public ResponseEntity<Void> deleteSignosVitales(@PathVariable Long id) {
        log.debug("REST request to delete SignosVitales : {}", id);
        signosVitalesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }



    /**
     * {@code GET  /signos-vitales/range/:from/:to} : Obtener signos vitales dentro de una rango de fecha especificado
     *
     * @param from the initial date of the range to retrieve signosVitales from.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the signosVitales, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/signos-vitales/{from}/{to}")
    @Transactional(readOnly = true)
    public List<SignosVitales> getSignosVitales(@PathVariable LocalDate from,  @PathVariable(value = "to", required = false)  LocalDate to) {
        log.debug("REST request to get SignosVitales : {}, {}", from, to);
        return signosVitalesService.findSignosVitalesFromRange(from, to);
    }
}
