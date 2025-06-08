package com.telconova.tracking.repository;

import com.telconova.tracking.entity.AvanceEdicion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AvanceEdicionRepository extends JpaRepository<AvanceEdicion, UUID> {
    List<AvanceEdicion> findByAvanceIdOrderByModificadoEnDesc(UUID avanceId);
}
