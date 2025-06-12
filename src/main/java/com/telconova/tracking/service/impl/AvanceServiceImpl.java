package com.telconova.tracking.service.impl;

import com.telconova.tracking.entity.Avance;
import com.telconova.tracking.entity.AvanceEdicion;
import com.telconova.tracking.repository.AvanceRepository;
import com.telconova.tracking.service.AvanceService;
import com.telconova.tracking.exception.ResourceNotFoundException;
import com.telconova.tracking.repository.AvanceEdicionRepository;
// import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
@lombok.RequiredArgsConstructor
public class AvanceServiceImpl implements AvanceService {

    private final AvanceRepository avanceRepository;
    private final AvanceEdicionRepository avanceEdicionRepository;
    private static final Logger logger = LoggerFactory.getLogger(AvanceServiceImpl.class);

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

    @Override
    @Transactional
    public Avance editarTiempoInvertido(UUID avanceId, Integer tiempoNuevo, String justificacion)
            throws ResourceNotFoundException, IllegalArgumentException {

        logger.debug("Editando tiempo invertido para avance: {}", avanceId);

        // Validaciones de entrada
        if (tiempoNuevo == null || tiempoNuevo <= 0) {
            throw new IllegalArgumentException("El tiempo invertido debe ser un valor positivo");
        }

        if (justificacion == null || justificacion.trim().isEmpty()
                || justificacion.length() < 10) {
            throw new IllegalArgumentException(
                    "La justificación es obligatoria y debe tener al menos 10 caracteres");
        }

        // Buscar el avance
        Avance avance = avanceRepository.findById(avanceId)
                .orElseThrow(() -> new ResourceNotFoundException("Avance", "id", avanceId));

        // Guardar el tiempo anterior para el registro de auditoría
        Integer tiempoAnterior = avance.getTiempoInvertido();

        // Solo proceder si hay un cambio real en el tiempo
        if (!tiempoAnterior.equals(tiempoNuevo)) {
            // Crear registro de edición
            AvanceEdicion edicion = new AvanceEdicion();
            edicion.setAvanceId(avanceId);
            edicion.setTiempoAnterior(tiempoAnterior);
            edicion.setTiempoNuevo(tiempoNuevo);
            edicion.setJustificacion(justificacion);

            // Guardar registro de edición
            avanceEdicionRepository.save(edicion);

            // Actualizar el avance
            avance.setTiempoInvertido(tiempoNuevo);
            avance.setModificadoEn(LocalDateTime.now());
            avance = avanceRepository.save(avance);


            logger.info("Tiempo invertido actualizado correctamente para avance: {}", avanceId);
        } else {
            logger.info("No se detectó cambio en el tiempo invertido para avance: {}", avanceId);
        }

        return avance;
    }
}
