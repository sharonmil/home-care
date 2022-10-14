package com.silvana.homecare.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.silvana.homecare.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HistoriaClinicaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HistoriaClinica.class);
        HistoriaClinica historiaClinica1 = new HistoriaClinica();
        historiaClinica1.setId(1L);
        HistoriaClinica historiaClinica2 = new HistoriaClinica();
        historiaClinica2.setId(historiaClinica1.getId());
        assertThat(historiaClinica1).isEqualTo(historiaClinica2);
        historiaClinica2.setId(2L);
        assertThat(historiaClinica1).isNotEqualTo(historiaClinica2);
        historiaClinica1.setId(null);
        assertThat(historiaClinica1).isNotEqualTo(historiaClinica2);
    }
}
