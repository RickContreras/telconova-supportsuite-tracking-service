package com.telconova.tracking.repository;

import com.telconova.tracking.entity.Avance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AvanceRepository extends JpaRepository<Avance, UUID> {
    List<Avance> findByOrdenIdOrderByCreadoEnDesc(UUID ordenId);
}
