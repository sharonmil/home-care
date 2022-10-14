package com.silvana.homecare.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.silvana.homecare.IntegrationTest;
import com.silvana.homecare.domain.HistoriaClinica;
import com.silvana.homecare.repository.HistoriaClinicaRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link HistoriaClinicaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HistoriaClinicaResourceIT {

    private static final String DEFAULT_SUGERENCIA = "AAAAAAAAAA";
    private static final String UPDATED_SUGERENCIA = "BBBBBBBBBB";

    private static final String DEFAULT_DIAGNOSTICO = "AAAAAAAAAA";
    private static final String UPDATED_DIAGNOSTICO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/historia-clinicas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private HistoriaClinicaRepository historiaClinicaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHistoriaClinicaMockMvc;

    private HistoriaClinica historiaClinica;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HistoriaClinica createEntity(EntityManager em) {
        HistoriaClinica historiaClinica = new HistoriaClinica().sugerencia(DEFAULT_SUGERENCIA).diagnostico(DEFAULT_DIAGNOSTICO);
        return historiaClinica;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HistoriaClinica createUpdatedEntity(EntityManager em) {
        HistoriaClinica historiaClinica = new HistoriaClinica().sugerencia(UPDATED_SUGERENCIA).diagnostico(UPDATED_DIAGNOSTICO);
        return historiaClinica;
    }

    @BeforeEach
    public void initTest() {
        historiaClinica = createEntity(em);
    }

    @Test
    @Transactional
    void createHistoriaClinica() throws Exception {
        int databaseSizeBeforeCreate = historiaClinicaRepository.findAll().size();
        // Create the HistoriaClinica
        restHistoriaClinicaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(historiaClinica))
            )
            .andExpect(status().isCreated());

        // Validate the HistoriaClinica in the database
        List<HistoriaClinica> historiaClinicaList = historiaClinicaRepository.findAll();
        assertThat(historiaClinicaList).hasSize(databaseSizeBeforeCreate + 1);
        HistoriaClinica testHistoriaClinica = historiaClinicaList.get(historiaClinicaList.size() - 1);
        assertThat(testHistoriaClinica.getSugerencia()).isEqualTo(DEFAULT_SUGERENCIA);
        assertThat(testHistoriaClinica.getDiagnostico()).isEqualTo(DEFAULT_DIAGNOSTICO);
    }

    @Test
    @Transactional
    void createHistoriaClinicaWithExistingId() throws Exception {
        // Create the HistoriaClinica with an existing ID
        historiaClinica.setId(1L);

        int databaseSizeBeforeCreate = historiaClinicaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHistoriaClinicaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(historiaClinica))
            )
            .andExpect(status().isBadRequest());

        // Validate the HistoriaClinica in the database
        List<HistoriaClinica> historiaClinicaList = historiaClinicaRepository.findAll();
        assertThat(historiaClinicaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSugerenciaIsRequired() throws Exception {
        int databaseSizeBeforeTest = historiaClinicaRepository.findAll().size();
        // set the field null
        historiaClinica.setSugerencia(null);

        // Create the HistoriaClinica, which fails.

        restHistoriaClinicaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(historiaClinica))
            )
            .andExpect(status().isBadRequest());

        List<HistoriaClinica> historiaClinicaList = historiaClinicaRepository.findAll();
        assertThat(historiaClinicaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllHistoriaClinicas() throws Exception {
        // Initialize the database
        historiaClinicaRepository.saveAndFlush(historiaClinica);

        // Get all the historiaClinicaList
        restHistoriaClinicaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(historiaClinica.getId().intValue())))
            .andExpect(jsonPath("$.[*].sugerencia").value(hasItem(DEFAULT_SUGERENCIA)))
            .andExpect(jsonPath("$.[*].diagnostico").value(hasItem(DEFAULT_DIAGNOSTICO)));
    }

    @Test
    @Transactional
    void getHistoriaClinica() throws Exception {
        // Initialize the database
        historiaClinicaRepository.saveAndFlush(historiaClinica);

        // Get the historiaClinica
        restHistoriaClinicaMockMvc
            .perform(get(ENTITY_API_URL_ID, historiaClinica.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(historiaClinica.getId().intValue()))
            .andExpect(jsonPath("$.sugerencia").value(DEFAULT_SUGERENCIA))
            .andExpect(jsonPath("$.diagnostico").value(DEFAULT_DIAGNOSTICO));
    }

    @Test
    @Transactional
    void getNonExistingHistoriaClinica() throws Exception {
        // Get the historiaClinica
        restHistoriaClinicaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewHistoriaClinica() throws Exception {
        // Initialize the database
        historiaClinicaRepository.saveAndFlush(historiaClinica);

        int databaseSizeBeforeUpdate = historiaClinicaRepository.findAll().size();

        // Update the historiaClinica
        HistoriaClinica updatedHistoriaClinica = historiaClinicaRepository.findById(historiaClinica.getId()).get();
        // Disconnect from session so that the updates on updatedHistoriaClinica are not directly saved in db
        em.detach(updatedHistoriaClinica);
        updatedHistoriaClinica.sugerencia(UPDATED_SUGERENCIA).diagnostico(UPDATED_DIAGNOSTICO);

        restHistoriaClinicaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedHistoriaClinica.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedHistoriaClinica))
            )
            .andExpect(status().isOk());

        // Validate the HistoriaClinica in the database
        List<HistoriaClinica> historiaClinicaList = historiaClinicaRepository.findAll();
        assertThat(historiaClinicaList).hasSize(databaseSizeBeforeUpdate);
        HistoriaClinica testHistoriaClinica = historiaClinicaList.get(historiaClinicaList.size() - 1);
        assertThat(testHistoriaClinica.getSugerencia()).isEqualTo(UPDATED_SUGERENCIA);
        assertThat(testHistoriaClinica.getDiagnostico()).isEqualTo(UPDATED_DIAGNOSTICO);
    }

    @Test
    @Transactional
    void putNonExistingHistoriaClinica() throws Exception {
        int databaseSizeBeforeUpdate = historiaClinicaRepository.findAll().size();
        historiaClinica.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHistoriaClinicaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, historiaClinica.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(historiaClinica))
            )
            .andExpect(status().isBadRequest());

        // Validate the HistoriaClinica in the database
        List<HistoriaClinica> historiaClinicaList = historiaClinicaRepository.findAll();
        assertThat(historiaClinicaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHistoriaClinica() throws Exception {
        int databaseSizeBeforeUpdate = historiaClinicaRepository.findAll().size();
        historiaClinica.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistoriaClinicaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(historiaClinica))
            )
            .andExpect(status().isBadRequest());

        // Validate the HistoriaClinica in the database
        List<HistoriaClinica> historiaClinicaList = historiaClinicaRepository.findAll();
        assertThat(historiaClinicaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHistoriaClinica() throws Exception {
        int databaseSizeBeforeUpdate = historiaClinicaRepository.findAll().size();
        historiaClinica.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistoriaClinicaMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(historiaClinica))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HistoriaClinica in the database
        List<HistoriaClinica> historiaClinicaList = historiaClinicaRepository.findAll();
        assertThat(historiaClinicaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHistoriaClinicaWithPatch() throws Exception {
        // Initialize the database
        historiaClinicaRepository.saveAndFlush(historiaClinica);

        int databaseSizeBeforeUpdate = historiaClinicaRepository.findAll().size();

        // Update the historiaClinica using partial update
        HistoriaClinica partialUpdatedHistoriaClinica = new HistoriaClinica();
        partialUpdatedHistoriaClinica.setId(historiaClinica.getId());

        partialUpdatedHistoriaClinica.diagnostico(UPDATED_DIAGNOSTICO);

        restHistoriaClinicaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHistoriaClinica.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHistoriaClinica))
            )
            .andExpect(status().isOk());

        // Validate the HistoriaClinica in the database
        List<HistoriaClinica> historiaClinicaList = historiaClinicaRepository.findAll();
        assertThat(historiaClinicaList).hasSize(databaseSizeBeforeUpdate);
        HistoriaClinica testHistoriaClinica = historiaClinicaList.get(historiaClinicaList.size() - 1);
        assertThat(testHistoriaClinica.getSugerencia()).isEqualTo(DEFAULT_SUGERENCIA);
        assertThat(testHistoriaClinica.getDiagnostico()).isEqualTo(UPDATED_DIAGNOSTICO);
    }

    @Test
    @Transactional
    void fullUpdateHistoriaClinicaWithPatch() throws Exception {
        // Initialize the database
        historiaClinicaRepository.saveAndFlush(historiaClinica);

        int databaseSizeBeforeUpdate = historiaClinicaRepository.findAll().size();

        // Update the historiaClinica using partial update
        HistoriaClinica partialUpdatedHistoriaClinica = new HistoriaClinica();
        partialUpdatedHistoriaClinica.setId(historiaClinica.getId());

        partialUpdatedHistoriaClinica.sugerencia(UPDATED_SUGERENCIA).diagnostico(UPDATED_DIAGNOSTICO);

        restHistoriaClinicaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHistoriaClinica.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHistoriaClinica))
            )
            .andExpect(status().isOk());

        // Validate the HistoriaClinica in the database
        List<HistoriaClinica> historiaClinicaList = historiaClinicaRepository.findAll();
        assertThat(historiaClinicaList).hasSize(databaseSizeBeforeUpdate);
        HistoriaClinica testHistoriaClinica = historiaClinicaList.get(historiaClinicaList.size() - 1);
        assertThat(testHistoriaClinica.getSugerencia()).isEqualTo(UPDATED_SUGERENCIA);
        assertThat(testHistoriaClinica.getDiagnostico()).isEqualTo(UPDATED_DIAGNOSTICO);
    }

    @Test
    @Transactional
    void patchNonExistingHistoriaClinica() throws Exception {
        int databaseSizeBeforeUpdate = historiaClinicaRepository.findAll().size();
        historiaClinica.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHistoriaClinicaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, historiaClinica.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(historiaClinica))
            )
            .andExpect(status().isBadRequest());

        // Validate the HistoriaClinica in the database
        List<HistoriaClinica> historiaClinicaList = historiaClinicaRepository.findAll();
        assertThat(historiaClinicaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHistoriaClinica() throws Exception {
        int databaseSizeBeforeUpdate = historiaClinicaRepository.findAll().size();
        historiaClinica.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistoriaClinicaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(historiaClinica))
            )
            .andExpect(status().isBadRequest());

        // Validate the HistoriaClinica in the database
        List<HistoriaClinica> historiaClinicaList = historiaClinicaRepository.findAll();
        assertThat(historiaClinicaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHistoriaClinica() throws Exception {
        int databaseSizeBeforeUpdate = historiaClinicaRepository.findAll().size();
        historiaClinica.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistoriaClinicaMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(historiaClinica))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HistoriaClinica in the database
        List<HistoriaClinica> historiaClinicaList = historiaClinicaRepository.findAll();
        assertThat(historiaClinicaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHistoriaClinica() throws Exception {
        // Initialize the database
        historiaClinicaRepository.saveAndFlush(historiaClinica);

        int databaseSizeBeforeDelete = historiaClinicaRepository.findAll().size();

        // Delete the historiaClinica
        restHistoriaClinicaMockMvc
            .perform(delete(ENTITY_API_URL_ID, historiaClinica.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<HistoriaClinica> historiaClinicaList = historiaClinicaRepository.findAll();
        assertThat(historiaClinicaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
