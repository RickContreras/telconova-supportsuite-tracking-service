package com.telconova.tracking.service;

import com.telconova.tracking.entity.Avance;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AvanceService {
    Avance save(Avance avance);

    List<Avance> findByOrdenId(UUID ordenId);

    Optional<Avance> findById(UUID id);

    void deleteById(UUID id);
}
