package com.silvana.homecare.repository;

import com.silvana.homecare.domain.HistoriaClinica;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the HistoriaClinica entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HistoriaClinicaRepository extends JpaRepository<HistoriaClinica, Long> {
    @Query("select historiaClinica from HistoriaClinica historiaClinica where historiaClinica.familiar.login = ?#{principal.username}")
    List<HistoriaClinica> findByFamiliarIsCurrentUser();

    @Query("select historiaClinica from HistoriaClinica historiaClinica where historiaClinica.medico.login = ?#{principal.username}")
    List<HistoriaClinica> findByMedicoIsCurrentUser();
}
