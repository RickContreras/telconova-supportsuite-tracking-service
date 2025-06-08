package com.telconova.tracking.repository;

import com.telconova.tracking.entity.Evidencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EvidenciaRepository extends JpaRepository<Evidencia, UUID> {
    List<Evidencia> findByAvanceId(UUID avanceId);
}
