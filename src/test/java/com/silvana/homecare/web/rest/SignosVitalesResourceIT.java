package com.silvana.homecare.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.silvana.homecare.IntegrationTest;
import com.silvana.homecare.domain.SignosVitales;
import com.silvana.homecare.repository.SignosVitalesRepository;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link SignosVitalesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SignosVitalesResourceIT {

    private static final Integer DEFAULT_OXIMETRIA = 1;
    private static final Integer UPDATED_OXIMETRIA = 2;

    private static final Integer DEFAULT_FRECUENCIA_RESPIRATORIA = 1;
    private static final Integer UPDATED_FRECUENCIA_RESPIRATORIA = 2;

    private static final Integer DEFAULT_FRECUENCIA_CARDIACA = 80;
    private static final Integer UPDATED_FRECUENCIA_CARDIACA = 120;

    private static final String DEFAULT_PRESION = "AAAAAAAAAA";
    private static final String UPDATED_PRESION = "BBBBBBBBBB";

    private static final Integer DEFAULT_GLICEMIA = 1;
    private static final Integer UPDATED_GLICEMIA = 2;

    private static final LocalDate DEFAULT_CREATED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/signos-vitales";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SignosVitalesRepository signosVitalesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSignosVitalesMockMvc;

    private SignosVitales signosVitales;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SignosVitales createEntity(EntityManager em) {
        SignosVitales signosVitales = new SignosVitales()
            .oximetria(DEFAULT_OXIMETRIA)
            .frecuenciaRespiratoria(DEFAULT_FRECUENCIA_RESPIRATORIA)
            .frecuenciaCardiaca(DEFAULT_FRECUENCIA_CARDIACA)
            .presion(DEFAULT_PRESION)
            .glicemia(DEFAULT_GLICEMIA)
            .createdDate(DEFAULT_CREATED_DATE);
        return signosVitales;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SignosVitales createUpdatedEntity(EntityManager em) {
        SignosVitales signosVitales = new SignosVitales()
            .oximetria(UPDATED_OXIMETRIA)
            .frecuenciaRespiratoria(UPDATED_FRECUENCIA_RESPIRATORIA)
            .frecuenciaCardiaca(UPDATED_FRECUENCIA_CARDIACA)
            .presion(UPDATED_PRESION)
            .glicemia(UPDATED_GLICEMIA)
            .createdDate(UPDATED_CREATED_DATE);
        return signosVitales;
    }

    @BeforeEach
    public void initTest() {
        signosVitales = createEntity(em);
    }

    @Test
    @Transactional
    void createSignosVitales() throws Exception {
        int databaseSizeBeforeCreate = signosVitalesRepository.findAll().size();
        // Create the SignosVitales
        restSignosVitalesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(signosVitales)))
            .andExpect(status().isCreated());

        // Validate the SignosVitales in the database
        List<SignosVitales> signosVitalesList = signosVitalesRepository.findAll();
        assertThat(signosVitalesList).hasSize(databaseSizeBeforeCreate + 1);
        SignosVitales testSignosVitales = signosVitalesList.get(signosVitalesList.size() - 1);
        assertThat(testSignosVitales.getOximetria()).isEqualTo(DEFAULT_OXIMETRIA);
        assertThat(testSignosVitales.getFrecuenciaRespiratoria()).isEqualTo(DEFAULT_FRECUENCIA_RESPIRATORIA);
        assertThat(testSignosVitales.getFrecuenciaCardiaca()).isEqualTo(DEFAULT_FRECUENCIA_CARDIACA);
        assertThat(testSignosVitales.getPresion()).isEqualTo(DEFAULT_PRESION);
        assertThat(testSignosVitales.getGlicemia()).isEqualTo(DEFAULT_GLICEMIA);
        assertThat(testSignosVitales.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
    }

    @Test
    @Transactional
    void createSignosVitalesWithExistingId() throws Exception {
        // Create the SignosVitales with an existing ID
        signosVitales.setId(1L);

        int databaseSizeBeforeCreate = signosVitalesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSignosVitalesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(signosVitales)))
            .andExpect(status().isBadRequest());

        // Validate the SignosVitales in the database
        List<SignosVitales> signosVitalesList = signosVitalesRepository.findAll();
        assertThat(signosVitalesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSignosVitales() throws Exception {
        // Initialize the database
        signosVitalesRepository.saveAndFlush(signosVitales);

        // Get all the signosVitalesList
        restSignosVitalesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(signosVitales.getId().intValue())))
            .andExpect(jsonPath("$.[*].oximetria").value(hasItem(DEFAULT_OXIMETRIA)))
            .andExpect(jsonPath("$.[*].frecuenciaRespiratoria").value(hasItem(DEFAULT_FRECUENCIA_RESPIRATORIA)))
            .andExpect(jsonPath("$.[*].frecuenciaCardiaca").value(hasItem(DEFAULT_FRECUENCIA_CARDIACA)))
            .andExpect(jsonPath("$.[*].presion").value(hasItem(DEFAULT_PRESION)))
            .andExpect(jsonPath("$.[*].glicemia").value(hasItem(DEFAULT_GLICEMIA)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }

    @Test
    @Transactional
    void getSignosVitales() throws Exception {
        // Initialize the database
        signosVitalesRepository.saveAndFlush(signosVitales);

        // Get the signosVitales
        restSignosVitalesMockMvc
            .perform(get(ENTITY_API_URL_ID, signosVitales.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(signosVitales.getId().intValue()))
            .andExpect(jsonPath("$.oximetria").value(DEFAULT_OXIMETRIA))
            .andExpect(jsonPath("$.frecuenciaRespiratoria").value(DEFAULT_FRECUENCIA_RESPIRATORIA))
            .andExpect(jsonPath("$.frecuenciaCardiaca").value(DEFAULT_FRECUENCIA_CARDIACA))
            .andExpect(jsonPath("$.presion").value(DEFAULT_PRESION))
            .andExpect(jsonPath("$.glicemia").value(DEFAULT_GLICEMIA))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingSignosVitales() throws Exception {
        // Get the signosVitales
        restSignosVitalesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSignosVitales() throws Exception {
        // Initialize the database
        signosVitalesRepository.saveAndFlush(signosVitales);

        int databaseSizeBeforeUpdate = signosVitalesRepository.findAll().size();

        // Update the signosVitales
        SignosVitales updatedSignosVitales = signosVitalesRepository.findById(signosVitales.getId()).get();
        // Disconnect from session so that the updates on updatedSignosVitales are not directly saved in db
        em.detach(updatedSignosVitales);
        updatedSignosVitales
            .oximetria(UPDATED_OXIMETRIA)
            .frecuenciaRespiratoria(UPDATED_FRECUENCIA_RESPIRATORIA)
            .frecuenciaCardiaca(UPDATED_FRECUENCIA_CARDIACA)
            .presion(UPDATED_PRESION)
            .glicemia(UPDATED_GLICEMIA)
            .createdDate(UPDATED_CREATED_DATE);

        restSignosVitalesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSignosVitales.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSignosVitales))
            )
            .andExpect(status().isOk());

        // Validate the SignosVitales in the database
        List<SignosVitales> signosVitalesList = signosVitalesRepository.findAll();
        assertThat(signosVitalesList).hasSize(databaseSizeBeforeUpdate);
        SignosVitales testSignosVitales = signosVitalesList.get(signosVitalesList.size() - 1);
        assertThat(testSignosVitales.getOximetria()).isEqualTo(UPDATED_OXIMETRIA);
        assertThat(testSignosVitales.getFrecuenciaRespiratoria()).isEqualTo(UPDATED_FRECUENCIA_RESPIRATORIA);
        assertThat(testSignosVitales.getFrecuenciaCardiaca()).isEqualTo(UPDATED_FRECUENCIA_CARDIACA);
        assertThat(testSignosVitales.getPresion()).isEqualTo(UPDATED_PRESION);
        assertThat(testSignosVitales.getGlicemia()).isEqualTo(UPDATED_GLICEMIA);
        assertThat(testSignosVitales.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingSignosVitales() throws Exception {
        int databaseSizeBeforeUpdate = signosVitalesRepository.findAll().size();
        signosVitales.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSignosVitalesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, signosVitales.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(signosVitales))
            )
            .andExpect(status().isBadRequest());

        // Validate the SignosVitales in the database
        List<SignosVitales> signosVitalesList = signosVitalesRepository.findAll();
        assertThat(signosVitalesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSignosVitales() throws Exception {
        int databaseSizeBeforeUpdate = signosVitalesRepository.findAll().size();
        signosVitales.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSignosVitalesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(signosVitales))
            )
            .andExpect(status().isBadRequest());

        // Validate the SignosVitales in the database
        List<SignosVitales> signosVitalesList = signosVitalesRepository.findAll();
        assertThat(signosVitalesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSignosVitales() throws Exception {
        int databaseSizeBeforeUpdate = signosVitalesRepository.findAll().size();
        signosVitales.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSignosVitalesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(signosVitales)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SignosVitales in the database
        List<SignosVitales> signosVitalesList = signosVitalesRepository.findAll();
        assertThat(signosVitalesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSignosVitalesWithPatch() throws Exception {
        // Initialize the database
        signosVitalesRepository.saveAndFlush(signosVitales);

        int databaseSizeBeforeUpdate = signosVitalesRepository.findAll().size();

        // Update the signosVitales using partial update
        SignosVitales partialUpdatedSignosVitales = new SignosVitales();
        partialUpdatedSignosVitales.setId(signosVitales.getId());

        partialUpdatedSignosVitales.glicemia(UPDATED_GLICEMIA);

        restSignosVitalesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSignosVitales.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSignosVitales))
            )
            .andExpect(status().isOk());

        // Validate the SignosVitales in the database
        List<SignosVitales> signosVitalesList = signosVitalesRepository.findAll();
        assertThat(signosVitalesList).hasSize(databaseSizeBeforeUpdate);
        SignosVitales testSignosVitales = signosVitalesList.get(signosVitalesList.size() - 1);
        assertThat(testSignosVitales.getOximetria()).isEqualTo(DEFAULT_OXIMETRIA);
        assertThat(testSignosVitales.getFrecuenciaRespiratoria()).isEqualTo(DEFAULT_FRECUENCIA_RESPIRATORIA);
        assertThat(testSignosVitales.getFrecuenciaCardiaca()).isEqualTo(DEFAULT_FRECUENCIA_CARDIACA);
        assertThat(testSignosVitales.getPresion()).isEqualTo(DEFAULT_PRESION);
        assertThat(testSignosVitales.getGlicemia()).isEqualTo(UPDATED_GLICEMIA);
        assertThat(testSignosVitales.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateSignosVitalesWithPatch() throws Exception {
        // Initialize the database
        signosVitalesRepository.saveAndFlush(signosVitales);

        int databaseSizeBeforeUpdate = signosVitalesRepository.findAll().size();

        // Update the signosVitales using partial update
        SignosVitales partialUpdatedSignosVitales = new SignosVitales();
        partialUpdatedSignosVitales.setId(signosVitales.getId());

        partialUpdatedSignosVitales
            .oximetria(UPDATED_OXIMETRIA)
            .frecuenciaRespiratoria(UPDATED_FRECUENCIA_RESPIRATORIA)
            .frecuenciaCardiaca(UPDATED_FRECUENCIA_CARDIACA)
            .presion(UPDATED_PRESION)
            .glicemia(UPDATED_GLICEMIA)
            .createdDate(UPDATED_CREATED_DATE);

        restSignosVitalesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSignosVitales.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSignosVitales))
            )
            .andExpect(status().isOk());

        // Validate the SignosVitales in the database
        List<SignosVitales> signosVitalesList = signosVitalesRepository.findAll();
        assertThat(signosVitalesList).hasSize(databaseSizeBeforeUpdate);
        SignosVitales testSignosVitales = signosVitalesList.get(signosVitalesList.size() - 1);
        assertThat(testSignosVitales.getOximetria()).isEqualTo(UPDATED_OXIMETRIA);
        assertThat(testSignosVitales.getFrecuenciaRespiratoria()).isEqualTo(UPDATED_FRECUENCIA_RESPIRATORIA);
        assertThat(testSignosVitales.getFrecuenciaCardiaca()).isEqualTo(UPDATED_FRECUENCIA_CARDIACA);
        assertThat(testSignosVitales.getPresion()).isEqualTo(UPDATED_PRESION);
        assertThat(testSignosVitales.getGlicemia()).isEqualTo(UPDATED_GLICEMIA);
        assertThat(testSignosVitales.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingSignosVitales() throws Exception {
        int databaseSizeBeforeUpdate = signosVitalesRepository.findAll().size();
        signosVitales.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSignosVitalesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, signosVitales.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(signosVitales))
            )
            .andExpect(status().isBadRequest());

        // Validate the SignosVitales in the database
        List<SignosVitales> signosVitalesList = signosVitalesRepository.findAll();
        assertThat(signosVitalesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSignosVitales() throws Exception {
        int databaseSizeBeforeUpdate = signosVitalesRepository.findAll().size();
        signosVitales.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSignosVitalesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(signosVitales))
            )
            .andExpect(status().isBadRequest());

        // Validate the SignosVitales in the database
        List<SignosVitales> signosVitalesList = signosVitalesRepository.findAll();
        assertThat(signosVitalesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSignosVitales() throws Exception {
        int databaseSizeBeforeUpdate = signosVitalesRepository.findAll().size();
        signosVitales.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSignosVitalesMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(signosVitales))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SignosVitales in the database
        List<SignosVitales> signosVitalesList = signosVitalesRepository.findAll();
        assertThat(signosVitalesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSignosVitales() throws Exception {
        // Initialize the database
        signosVitalesRepository.saveAndFlush(signosVitales);

        int databaseSizeBeforeDelete = signosVitalesRepository.findAll().size();

        // Delete the signosVitales
        restSignosVitalesMockMvc
            .perform(delete(ENTITY_API_URL_ID, signosVitales.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SignosVitales> signosVitalesList = signosVitalesRepository.findAll();
        assertThat(signosVitalesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
