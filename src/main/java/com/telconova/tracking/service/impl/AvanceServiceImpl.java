package com.telconova.tracking.service.impl;

import com.telconova.tracking.entity.Avance;
import com.telconova.tracking.repository.AvanceRepository;
import com.telconova.tracking.service.AvanceService;
// import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@lombok.RequiredArgsConstructor
public class AvanceServiceImpl implements AvanceService {

    private final AvanceRepository avanceRepository;

    @Override
    public Avance save(Avance avance) {
        return avanceRepository.save(avance);
    }

    @Override
    // @Cacheable(value = "avances", key = "'ordenId_' + #ordenId")
    public List<Avance> findByOrdenId(Long ordenId) {
        return avanceRepository.findByOrdenIdOrderByCreadoEnDesc(ordenId);
    }

    @Override
    // @Cacheable(value = "avances", key = "'all'")
    public List<Avance> findAll() {
        return avanceRepository.findAll();
    }

    @Override
    // @Cacheable(value = "avance", key = "#id")
    public Optional<Avance> findById(UUID id) {
        return avanceRepository.findById(id);
    }

    @Override
    // @CacheEvict(value = {"avance", "avances"}, allEntries = true)
    public void deleteById(UUID id) {
        avanceRepository.deleteById(id);
    }
}
