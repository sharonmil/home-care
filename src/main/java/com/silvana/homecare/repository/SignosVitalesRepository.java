package com.silvana.homecare.repository;

import com.silvana.homecare.domain.SignosVitales;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SignosVitales entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SignosVitalesRepository extends JpaRepository<SignosVitales, Long> {
    @Query("select signosVitales from SignosVitales signosVitales where signosVitales.familiar.login = ?#{principal.username}")
    List<SignosVitales> findByFamiliarIsCurrentUser();

    @Query("select signosVitales from SignosVitales signosVitales where signosVitales.medico.login = ?#{principal.username}")
    List<SignosVitales> findByMedicoIsCurrentUser();

    List<SignosVitales> findSignosVitalesByCreatedDateBetween(LocalDate from, LocalDate to);
}
