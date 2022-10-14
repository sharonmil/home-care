package com.silvana.homecare.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.silvana.homecare.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FamiliarTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Familiar.class);
        Familiar familiar1 = new Familiar();
        familiar1.setId(1L);
        Familiar familiar2 = new Familiar();
        familiar2.setId(familiar1.getId());
        assertThat(familiar1).isEqualTo(familiar2);
        familiar2.setId(2L);
        assertThat(familiar1).isNotEqualTo(familiar2);
        familiar1.setId(null);
        assertThat(familiar1).isNotEqualTo(familiar2);
    }
}
