package com.silvana.homecare.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.silvana.homecare.IntegrationTest;
import com.silvana.homecare.domain.Familiar;
import com.silvana.homecare.domain.enumeration.Parentesco;
import com.silvana.homecare.repository.FamiliarRepository;
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
 * Integration tests for the {@link FamiliarResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FamiliarResourceIT {

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

    private static final Parentesco DEFAULT_PARENTESCO = Parentesco.PADRE_O_MADRE;
    private static final Parentesco UPDATED_PARENTESCO = Parentesco.PAREJA;

    private static final String ENTITY_API_URL = "/api/familiars";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FamiliarRepository familiarRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFamiliarMockMvc;

    private Familiar familiar;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Familiar createEntity(EntityManager em) {
        Familiar familiar = new Familiar()
            .cedula(DEFAULT_CEDULA)
            .ciudad(DEFAULT_CIUDAD)
            .direccion(DEFAULT_DIRECCION)
            .telefono(DEFAULT_TELEFONO)
            .fechaNacimiento(DEFAULT_FECHA_NACIMIENTO)
            .parentesco(DEFAULT_PARENTESCO);
        return familiar;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Familiar createUpdatedEntity(EntityManager em) {
        Familiar familiar = new Familiar()
            .cedula(UPDATED_CEDULA)
            .ciudad(UPDATED_CIUDAD)
            .direccion(UPDATED_DIRECCION)
            .telefono(UPDATED_TELEFONO)
            .fechaNacimiento(UPDATED_FECHA_NACIMIENTO)
            .parentesco(UPDATED_PARENTESCO);
        return familiar;
    }

    @BeforeEach
    public void initTest() {
        familiar = createEntity(em);
    }

    @Test
    @Transactional
    void createFamiliar() throws Exception {
        int databaseSizeBeforeCreate = familiarRepository.findAll().size();
        // Create the Familiar
        restFamiliarMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(familiar)))
            .andExpect(status().isCreated());

        // Validate the Familiar in the database
        List<Familiar> familiarList = familiarRepository.findAll();
        assertThat(familiarList).hasSize(databaseSizeBeforeCreate + 1);
        Familiar testFamiliar = familiarList.get(familiarList.size() - 1);
        assertThat(testFamiliar.getCedula()).isEqualTo(DEFAULT_CEDULA);
        assertThat(testFamiliar.getCiudad()).isEqualTo(DEFAULT_CIUDAD);
        assertThat(testFamiliar.getDireccion()).isEqualTo(DEFAULT_DIRECCION);
        assertThat(testFamiliar.getTelefono()).isEqualTo(DEFAULT_TELEFONO);
        assertThat(testFamiliar.getFechaNacimiento()).isEqualTo(DEFAULT_FECHA_NACIMIENTO);
        assertThat(testFamiliar.getParentesco()).isEqualTo(DEFAULT_PARENTESCO);
    }

    @Test
    @Transactional
    void createFamiliarWithExistingId() throws Exception {
        // Create the Familiar with an existing ID
        familiar.setId(1L);

        int databaseSizeBeforeCreate = familiarRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFamiliarMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(familiar)))
            .andExpect(status().isBadRequest());

        // Validate the Familiar in the database
        List<Familiar> familiarList = familiarRepository.findAll();
        assertThat(familiarList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCedulaIsRequired() throws Exception {
        int databaseSizeBeforeTest = familiarRepository.findAll().size();
        // set the field null
        familiar.setCedula(null);

        // Create the Familiar, which fails.

        restFamiliarMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(familiar)))
            .andExpect(status().isBadRequest());

        List<Familiar> familiarList = familiarRepository.findAll();
        assertThat(familiarList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCiudadIsRequired() throws Exception {
        int databaseSizeBeforeTest = familiarRepository.findAll().size();
        // set the field null
        familiar.setCiudad(null);

        // Create the Familiar, which fails.

        restFamiliarMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(familiar)))
            .andExpect(status().isBadRequest());

        List<Familiar> familiarList = familiarRepository.findAll();
        assertThat(familiarList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDireccionIsRequired() throws Exception {
        int databaseSizeBeforeTest = familiarRepository.findAll().size();
        // set the field null
        familiar.setDireccion(null);

        // Create the Familiar, which fails.

        restFamiliarMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(familiar)))
            .andExpect(status().isBadRequest());

        List<Familiar> familiarList = familiarRepository.findAll();
        assertThat(familiarList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTelefonoIsRequired() throws Exception {
        int databaseSizeBeforeTest = familiarRepository.findAll().size();
        // set the field null
        familiar.setTelefono(null);

        // Create the Familiar, which fails.

        restFamiliarMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(familiar)))
            .andExpect(status().isBadRequest());

        List<Familiar> familiarList = familiarRepository.findAll();
        assertThat(familiarList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaNacimientoIsRequired() throws Exception {
        int databaseSizeBeforeTest = familiarRepository.findAll().size();
        // set the field null
        familiar.setFechaNacimiento(null);

        // Create the Familiar, which fails.

        restFamiliarMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(familiar)))
            .andExpect(status().isBadRequest());

        List<Familiar> familiarList = familiarRepository.findAll();
        assertThat(familiarList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkParentescoIsRequired() throws Exception {
        int databaseSizeBeforeTest = familiarRepository.findAll().size();
        // set the field null
        familiar.setParentesco(null);

        // Create the Familiar, which fails.

        restFamiliarMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(familiar)))
            .andExpect(status().isBadRequest());

        List<Familiar> familiarList = familiarRepository.findAll();
        assertThat(familiarList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFamiliars() throws Exception {
        // Initialize the database
        familiarRepository.saveAndFlush(familiar);

        // Get all the familiarList
        restFamiliarMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(familiar.getId().intValue())))
            .andExpect(jsonPath("$.[*].cedula").value(hasItem(DEFAULT_CEDULA)))
            .andExpect(jsonPath("$.[*].ciudad").value(hasItem(DEFAULT_CIUDAD)))
            .andExpect(jsonPath("$.[*].direccion").value(hasItem(DEFAULT_DIRECCION)))
            .andExpect(jsonPath("$.[*].telefono").value(hasItem(DEFAULT_TELEFONO)))
            .andExpect(jsonPath("$.[*].fechaNacimiento").value(hasItem(DEFAULT_FECHA_NACIMIENTO.toString())))
            .andExpect(jsonPath("$.[*].parentesco").value(hasItem(DEFAULT_PARENTESCO.toString())));
    }

    @Test
    @Transactional
    void getFamiliar() throws Exception {
        // Initialize the database
        familiarRepository.saveAndFlush(familiar);

        // Get the familiar
        restFamiliarMockMvc
            .perform(get(ENTITY_API_URL_ID, familiar.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(familiar.getId().intValue()))
            .andExpect(jsonPath("$.cedula").value(DEFAULT_CEDULA))
            .andExpect(jsonPath("$.ciudad").value(DEFAULT_CIUDAD))
            .andExpect(jsonPath("$.direccion").value(DEFAULT_DIRECCION))
            .andExpect(jsonPath("$.telefono").value(DEFAULT_TELEFONO))
            .andExpect(jsonPath("$.fechaNacimiento").value(DEFAULT_FECHA_NACIMIENTO.toString()))
            .andExpect(jsonPath("$.parentesco").value(DEFAULT_PARENTESCO.toString()));
    }

    @Test
    @Transactional
    void getNonExistingFamiliar() throws Exception {
        // Get the familiar
        restFamiliarMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFamiliar() throws Exception {
        // Initialize the database
        familiarRepository.saveAndFlush(familiar);

        int databaseSizeBeforeUpdate = familiarRepository.findAll().size();

        // Update the familiar
        Familiar updatedFamiliar = familiarRepository.findById(familiar.getId()).get();
        // Disconnect from session so that the updates on updatedFamiliar are not directly saved in db
        em.detach(updatedFamiliar);
        updatedFamiliar
            .cedula(UPDATED_CEDULA)
            .ciudad(UPDATED_CIUDAD)
            .direccion(UPDATED_DIRECCION)
            .telefono(UPDATED_TELEFONO)
            .fechaNacimiento(UPDATED_FECHA_NACIMIENTO)
            .parentesco(UPDATED_PARENTESCO);

        restFamiliarMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFamiliar.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFamiliar))
            )
            .andExpect(status().isOk());

        // Validate the Familiar in the database
        List<Familiar> familiarList = familiarRepository.findAll();
        assertThat(familiarList).hasSize(databaseSizeBeforeUpdate);
        Familiar testFamiliar = familiarList.get(familiarList.size() - 1);
        assertThat(testFamiliar.getCedula()).isEqualTo(UPDATED_CEDULA);
        assertThat(testFamiliar.getCiudad()).isEqualTo(UPDATED_CIUDAD);
        assertThat(testFamiliar.getDireccion()).isEqualTo(UPDATED_DIRECCION);
        assertThat(testFamiliar.getTelefono()).isEqualTo(UPDATED_TELEFONO);
        assertThat(testFamiliar.getFechaNacimiento()).isEqualTo(UPDATED_FECHA_NACIMIENTO);
        assertThat(testFamiliar.getParentesco()).isEqualTo(UPDATED_PARENTESCO);
    }

    @Test
    @Transactional
    void putNonExistingFamiliar() throws Exception {
        int databaseSizeBeforeUpdate = familiarRepository.findAll().size();
        familiar.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFamiliarMockMvc
            .perform(
                put(ENTITY_API_URL_ID, familiar.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(familiar))
            )
            .andExpect(status().isBadRequest());

        // Validate the Familiar in the database
        List<Familiar> familiarList = familiarRepository.findAll();
        assertThat(familiarList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFamiliar() throws Exception {
        int databaseSizeBeforeUpdate = familiarRepository.findAll().size();
        familiar.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFamiliarMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(familiar))
            )
            .andExpect(status().isBadRequest());

        // Validate the Familiar in the database
        List<Familiar> familiarList = familiarRepository.findAll();
        assertThat(familiarList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFamiliar() throws Exception {
        int databaseSizeBeforeUpdate = familiarRepository.findAll().size();
        familiar.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFamiliarMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(familiar)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Familiar in the database
        List<Familiar> familiarList = familiarRepository.findAll();
        assertThat(familiarList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFamiliarWithPatch() throws Exception {
        // Initialize the database
        familiarRepository.saveAndFlush(familiar);

        int databaseSizeBeforeUpdate = familiarRepository.findAll().size();

        // Update the familiar using partial update
        Familiar partialUpdatedFamiliar = new Familiar();
        partialUpdatedFamiliar.setId(familiar.getId());

        partialUpdatedFamiliar.cedula(UPDATED_CEDULA).fechaNacimiento(UPDATED_FECHA_NACIMIENTO).parentesco(UPDATED_PARENTESCO);

        restFamiliarMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFamiliar.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFamiliar))
            )
            .andExpect(status().isOk());

        // Validate the Familiar in the database
        List<Familiar> familiarList = familiarRepository.findAll();
        assertThat(familiarList).hasSize(databaseSizeBeforeUpdate);
        Familiar testFamiliar = familiarList.get(familiarList.size() - 1);
        assertThat(testFamiliar.getCedula()).isEqualTo(UPDATED_CEDULA);
        assertThat(testFamiliar.getCiudad()).isEqualTo(DEFAULT_CIUDAD);
        assertThat(testFamiliar.getDireccion()).isEqualTo(DEFAULT_DIRECCION);
        assertThat(testFamiliar.getTelefono()).isEqualTo(DEFAULT_TELEFONO);
        assertThat(testFamiliar.getFechaNacimiento()).isEqualTo(UPDATED_FECHA_NACIMIENTO);
        assertThat(testFamiliar.getParentesco()).isEqualTo(UPDATED_PARENTESCO);
    }

    @Test
    @Transactional
    void fullUpdateFamiliarWithPatch() throws Exception {
        // Initialize the database
        familiarRepository.saveAndFlush(familiar);

        int databaseSizeBeforeUpdate = familiarRepository.findAll().size();

        // Update the familiar using partial update
        Familiar partialUpdatedFamiliar = new Familiar();
        partialUpdatedFamiliar.setId(familiar.getId());

        partialUpdatedFamiliar
            .cedula(UPDATED_CEDULA)
            .ciudad(UPDATED_CIUDAD)
            .direccion(UPDATED_DIRECCION)
            .telefono(UPDATED_TELEFONO)
            .fechaNacimiento(UPDATED_FECHA_NACIMIENTO)
            .parentesco(UPDATED_PARENTESCO);

        restFamiliarMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFamiliar.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFamiliar))
            )
            .andExpect(status().isOk());

        // Validate the Familiar in the database
        List<Familiar> familiarList = familiarRepository.findAll();
        assertThat(familiarList).hasSize(databaseSizeBeforeUpdate);
        Familiar testFamiliar = familiarList.get(familiarList.size() - 1);
        assertThat(testFamiliar.getCedula()).isEqualTo(UPDATED_CEDULA);
        assertThat(testFamiliar.getCiudad()).isEqualTo(UPDATED_CIUDAD);
        assertThat(testFamiliar.getDireccion()).isEqualTo(UPDATED_DIRECCION);
        assertThat(testFamiliar.getTelefono()).isEqualTo(UPDATED_TELEFONO);
        assertThat(testFamiliar.getFechaNacimiento()).isEqualTo(UPDATED_FECHA_NACIMIENTO);
        assertThat(testFamiliar.getParentesco()).isEqualTo(UPDATED_PARENTESCO);
    }

    @Test
    @Transactional
    void patchNonExistingFamiliar() throws Exception {
        int databaseSizeBeforeUpdate = familiarRepository.findAll().size();
        familiar.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFamiliarMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, familiar.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(familiar))
            )
            .andExpect(status().isBadRequest());

        // Validate the Familiar in the database
        List<Familiar> familiarList = familiarRepository.findAll();
        assertThat(familiarList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFamiliar() throws Exception {
        int databaseSizeBeforeUpdate = familiarRepository.findAll().size();
        familiar.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFamiliarMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(familiar))
            )
            .andExpect(status().isBadRequest());

        // Validate the Familiar in the database
        List<Familiar> familiarList = familiarRepository.findAll();
        assertThat(familiarList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFamiliar() throws Exception {
        int databaseSizeBeforeUpdate = familiarRepository.findAll().size();
        familiar.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFamiliarMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(familiar)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Familiar in the database
        List<Familiar> familiarList = familiarRepository.findAll();
        assertThat(familiarList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFamiliar() throws Exception {
        // Initialize the database
        familiarRepository.saveAndFlush(familiar);

        int databaseSizeBeforeDelete = familiarRepository.findAll().size();

        // Delete the familiar
        restFamiliarMockMvc
            .perform(delete(ENTITY_API_URL_ID, familiar.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Familiar> familiarList = familiarRepository.findAll();
        assertThat(familiarList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
