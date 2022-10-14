package com.silvana.homecare.repository;

import com.silvana.homecare.domain.Familiar;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data SQL repository for the Familiar entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FamiliarRepository extends JpaRepository<Familiar, Long> {

    Optional<Familiar> findByPaciente_Username_Email(String email);
}
