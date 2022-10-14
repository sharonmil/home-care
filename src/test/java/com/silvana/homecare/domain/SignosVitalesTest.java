package com.silvana.homecare.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.silvana.homecare.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SignosVitalesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SignosVitales.class);
        SignosVitales signosVitales1 = new SignosVitales();
        signosVitales1.setId(1L);
        SignosVitales signosVitales2 = new SignosVitales();
        signosVitales2.setId(signosVitales1.getId());
        assertThat(signosVitales1).isEqualTo(signosVitales2);
        signosVitales2.setId(2L);
        assertThat(signosVitales1).isNotEqualTo(signosVitales2);
        signosVitales1.setId(null);
        assertThat(signosVitales1).isNotEqualTo(signosVitales2);
    }
}
