package com.silvana.homecare.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.silvana.homecare.IntegrationTest;
import com.silvana.homecare.domain.Medico;
import com.silvana.homecare.repository.MedicoRepository;
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
 * Integration tests for the {@link MedicoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MedicoResourceIT {

    private static final String DEFAULT_CEDULA = "AAAAAAAAAA";
    private static final String UPDATED_CEDULA = "BBBBBBBBBB";

    private static final String DEFAULT_CIUDAD = "AAAAAAAAAA";
    private static final String UPDATED_CIUDAD = "BBBBBBBBBB";

    private static final String DEFAULT_DIRECCION = "AAAAAAAAAA";
    private static final String UPDATED_DIRECCION = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONO = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONO = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_FECHA_NACIMIENTO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_NACIMIENTO = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_ESPECIALIDAD = "AAAAAAAAAA";
    private static final String UPDATED_ESPECIALIDAD = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/medicos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMedicoMockMvc;

    private Medico medico;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Medico createEntity(EntityManager em) {
        Medico medico = new Medico()
            .cedula(DEFAULT_CEDULA)
            .ciudad(DEFAULT_CIUDAD)
            .direccion(DEFAULT_DIRECCION)
            .telefono(DEFAULT_TELEFONO)
            .fechaNacimiento(DEFAULT_FECHA_NACIMIENTO)
            .especialidad(DEFAULT_ESPECIALIDAD);
        return medico;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Medico createUpdatedEntity(EntityManager em) {
        Medico medico = new Medico()
            .cedula(UPDATED_CEDULA)
            .ciudad(UPDATED_CIUDAD)
            .direccion(UPDATED_DIRECCION)
            .telefono(UPDATED_TELEFONO)
            .fechaNacimiento(UPDATED_FECHA_NACIMIENTO);
        return medico;
    }

    @BeforeEach
    public void initTest() {
        medico = createEntity(em);
    }

    @Test
    @Transactional
    void createMedico() throws Exception {
        int databaseSizeBeforeCreate = medicoRepository.findAll().size();
        // Create the Medico
        restMedicoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(medico)))
            .andExpect(status().isCreated());

        // Validate the Medico in the database
        List<Medico> medicoList = medicoRepository.findAll();
        assertThat(medicoList).hasSize(databaseSizeBeforeCreate + 1);
        Medico testMedico = medicoList.get(medicoList.size() - 1);
        assertThat(testMedico.getCedula()).isEqualTo(DEFAULT_CEDULA);
        assertThat(testMedico.getCiudad()).isEqualTo(DEFAULT_CIUDAD);
        assertThat(testMedico.getDireccion()).isEqualTo(DEFAULT_DIRECCION);
        assertThat(testMedico.getTelefono()).isEqualTo(DEFAULT_TELEFONO);
        assertThat(testMedico.getFechaNacimiento()).isEqualTo(DEFAULT_FECHA_NACIMIENTO);
    }

    @Test
    @Transactional
    void createMedicoWithExistingId() throws Exception {
        // Create the Medico with an existing ID
        medico.setId(1L);

        int databaseSizeBeforeCreate = medicoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMedicoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(medico)))
            .andExpect(status().isBadRequest());

        // Validate the Medico in the database
        List<Medico> medicoList = medicoRepository.findAll();
        assertThat(medicoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCedulaIsRequired() throws Exception {
        int databaseSizeBeforeTest = medicoRepository.findAll().size();
        // set the field null
        medico.setCedula(null);

        // Create the Medico, which fails.

        restMedicoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(medico)))
            .andExpect(status().isBadRequest());

        List<Medico> medicoList = medicoRepository.findAll();
        assertThat(medicoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCiudadIsRequired() throws Exception {
        int databaseSizeBeforeTest = medicoRepository.findAll().size();
        // set the field null
        medico.setCiudad(null);

        // Create the Medico, which fails.

        restMedicoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(medico)))
            .andExpect(status().isBadRequest());

        List<Medico> medicoList = medicoRepository.findAll();
        assertThat(medicoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDireccionIsRequired() throws Exception {
        int databaseSizeBeforeTest = medicoRepository.findAll().size();
        // set the field null
        medico.setDireccion(null);

        // Create the Medico, which fails.

        restMedicoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(medico)))
            .andExpect(status().isBadRequest());

        List<Medico> medicoList = medicoRepository.findAll();
        assertThat(medicoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTelefonoIsRequired() throws Exception {
        int databaseSizeBeforeTest = medicoRepository.findAll().size();
        // set the field null
        medico.setTelefono(null);

        // Create the Medico, which fails.

        restMedicoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(medico)))
            .andExpect(status().isBadRequest());

        List<Medico> medicoList = medicoRepository.findAll();
        assertThat(medicoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaNacimientoIsRequired() throws Exception {
        int databaseSizeBeforeTest = medicoRepository.findAll().size();
        // set the field null
        medico.setFechaNacimiento(null);

        // Create the Medico, which fails.

        restMedicoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(medico)))
            .andExpect(status().isBadRequest());

        List<Medico> medicoList = medicoRepository.findAll();
        assertThat(medicoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMedicos() throws Exception {
        // Initialize the database
        medicoRepository.saveAndFlush(medico);

        // Get all the medicoList
        restMedicoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(medico.getId().intValue())))
            .andExpect(jsonPath("$.[*].cedula").value(hasItem(DEFAULT_CEDULA)))
            .andExpect(jsonPath("$.[*].ciudad").value(hasItem(DEFAULT_CIUDAD)))
            .andExpect(jsonPath("$.[*].direccion").value(hasItem(DEFAULT_DIRECCION)))
            .andExpect(jsonPath("$.[*].telefono").value(hasItem(DEFAULT_TELEFONO)))
            .andExpect(jsonPath("$.[*].fechaNacimiento").value(hasItem(DEFAULT_FECHA_NACIMIENTO.toString())));
    }

    @Test
    @Transactional
    void getMedico() throws Exception {
        // Initialize the database
        medicoRepository.saveAndFlush(medico);

        // Get the medico
        restMedicoMockMvc
            .perform(get(ENTITY_API_URL_ID, medico.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(medico.getId().intValue()))
            .andExpect(jsonPath("$.cedula").value(DEFAULT_CEDULA))
            .andExpect(jsonPath("$.ciudad").value(DEFAULT_CIUDAD))
            .andExpect(jsonPath("$.direccion").value(DEFAULT_DIRECCION))
            .andExpect(jsonPath("$.telefono").value(DEFAULT_TELEFONO))
            .andExpect(jsonPath("$.fechaNacimiento").value(DEFAULT_FECHA_NACIMIENTO.toString()));
    }

    @Test
    @Transactional
    void getNonExistingMedico() throws Exception {
        // Get the medico
        restMedicoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMedico() throws Exception {
        // Initialize the database
        medicoRepository.saveAndFlush(medico);

        int databaseSizeBeforeUpdate = medicoRepository.findAll().size();

        // Update the medico
        Medico updatedMedico = medicoRepository.findById(medico.getId()).get();
        // Disconnect from session so that the updates on updatedMedico are not directly saved in db
        em.detach(updatedMedico);
        updatedMedico
            .cedula(UPDATED_CEDULA)
            .ciudad(UPDATED_CIUDAD)
            .direccion(UPDATED_DIRECCION)
            .telefono(UPDATED_TELEFONO)
            .fechaNacimiento(UPDATED_FECHA_NACIMIENTO)
            .especialidad(UPDATED_ESPECIALIDAD);

        restMedicoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMedico.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMedico))
            )
            .andExpect(status().isOk());

        // Validate the Medico in the database
        List<Medico> medicoList = medicoRepository.findAll();
        assertThat(medicoList).hasSize(databaseSizeBeforeUpdate);
        Medico testMedico = medicoList.get(medicoList.size() - 1);
        assertThat(testMedico.getCedula()).isEqualTo(UPDATED_CEDULA);
        assertThat(testMedico.getCiudad()).isEqualTo(UPDATED_CIUDAD);
        assertThat(testMedico.getDireccion()).isEqualTo(UPDATED_DIRECCION);
        assertThat(testMedico.getTelefono()).isEqualTo(UPDATED_TELEFONO);
        assertThat(testMedico.getFechaNacimiento()).isEqualTo(UPDATED_FECHA_NACIMIENTO);
        assertThat(testMedico.getEspecialidad()).isEqualTo(UPDATED_ESPECIALIDAD);
    }

    @Test
    @Transactional
    void putNonExistingMedico() throws Exception {
        int databaseSizeBeforeUpdate = medicoRepository.findAll().size();
        medico.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMedicoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, medico.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(medico))
            )
            .andExpect(status().isBadRequest());

        // Validate the Medico in the database
        List<Medico> medicoList = medicoRepository.findAll();
        assertThat(medicoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMedico() throws Exception {
        int databaseSizeBeforeUpdate = medicoRepository.findAll().size();
        medico.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(medico))
            )
            .andExpect(status().isBadRequest());

        // Validate the Medico in the database
        List<Medico> medicoList = medicoRepository.findAll();
        assertThat(medicoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMedico() throws Exception {
        int databaseSizeBeforeUpdate = medicoRepository.findAll().size();
        medico.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(medico)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Medico in the database
        List<Medico> medicoList = medicoRepository.findAll();
        assertThat(medicoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMedicoWithPatch() throws Exception {
        // Initialize the database
        medicoRepository.saveAndFlush(medico);

        int databaseSizeBeforeUpdate = medicoRepository.findAll().size();

        // Update the medico using partial update
        Medico partialUpdatedMedico = new Medico();
        partialUpdatedMedico.setId(medico.getId());

        partialUpdatedMedico.direccion(UPDATED_DIRECCION).especialidad(UPDATED_ESPECIALIDAD);

        restMedicoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMedico.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMedico))
            )
            .andExpect(status().isOk());

        // Validate the Medico in the database
        List<Medico> medicoList = medicoRepository.findAll();
        assertThat(medicoList).hasSize(databaseSizeBeforeUpdate);
        Medico testMedico = medicoList.get(medicoList.size() - 1);
        assertThat(testMedico.getCedula()).isEqualTo(DEFAULT_CEDULA);
        assertThat(testMedico.getCiudad()).isEqualTo(DEFAULT_CIUDAD);
        assertThat(testMedico.getDireccion()).isEqualTo(UPDATED_DIRECCION);
        assertThat(testMedico.getTelefono()).isEqualTo(DEFAULT_TELEFONO);
        assertThat(testMedico.getFechaNacimiento()).isEqualTo(DEFAULT_FECHA_NACIMIENTO);
        assertThat(testMedico.getEspecialidad()).isEqualTo(UPDATED_ESPECIALIDAD);
    }

    @Test
    @Transactional
    void fullUpdateMedicoWithPatch() throws Exception {
        // Initialize the database
        medicoRepository.saveAndFlush(medico);

        int databaseSizeBeforeUpdate = medicoRepository.findAll().size();

        // Update the medico using partial update
        Medico partialUpdatedMedico = new Medico();
        partialUpdatedMedico.setId(medico.getId());

        partialUpdatedMedico
            .cedula(UPDATED_CEDULA)
            .ciudad(UPDATED_CIUDAD)
            .direccion(UPDATED_DIRECCION)
            .telefono(UPDATED_TELEFONO)
            .fechaNacimiento(UPDATED_FECHA_NACIMIENTO)
            .especialidad(UPDATED_ESPECIALIDAD);

        restMedicoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMedico.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMedico))
            )
            .andExpect(status().isOk());

        // Validate the Medico in the database
        List<Medico> medicoList = medicoRepository.findAll();
        assertThat(medicoList).hasSize(databaseSizeBeforeUpdate);
        Medico testMedico = medicoList.get(medicoList.size() - 1);
        assertThat(testMedico.getCedula()).isEqualTo(UPDATED_CEDULA);
        assertThat(testMedico.getCiudad()).isEqualTo(UPDATED_CIUDAD);
        assertThat(testMedico.getDireccion()).isEqualTo(UPDATED_DIRECCION);
        assertThat(testMedico.getTelefono()).isEqualTo(UPDATED_TELEFONO);
        assertThat(testMedico.getFechaNacimiento()).isEqualTo(UPDATED_FECHA_NACIMIENTO);
        assertThat(testMedico.getEspecialidad()).isEqualTo(UPDATED_ESPECIALIDAD);
    }

    @Test
    @Transactional
    void patchNonExistingMedico() throws Exception {
        int databaseSizeBeforeUpdate = medicoRepository.findAll().size();
        medico.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMedicoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, medico.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(medico))
            )
            .andExpect(status().isBadRequest());

        // Validate the Medico in the database
        List<Medico> medicoList = medicoRepository.findAll();
        assertThat(medicoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMedico() throws Exception {
        int databaseSizeBeforeUpdate = medicoRepository.findAll().size();
        medico.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(medico))
            )
            .andExpect(status().isBadRequest());

        // Validate the Medico in the database
        List<Medico> medicoList = medicoRepository.findAll();
        assertThat(medicoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMedico() throws Exception {
        int databaseSizeBeforeUpdate = medicoRepository.findAll().size();
        medico.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(medico)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Medico in the database
        List<Medico> medicoList = medicoRepository.findAll();
        assertThat(medicoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMedico() throws Exception {
        // Initialize the database
        medicoRepository.saveAndFlush(medico);

        int databaseSizeBeforeDelete = medicoRepository.findAll().size();

        // Delete the medico
        restMedicoMockMvc
            .perform(delete(ENTITY_API_URL_ID, medico.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Medico> medicoList = medicoRepository.findAll();
        assertThat(medicoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
