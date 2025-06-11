package com.telconova.tracking.service.impl;

import com.telconova.tracking.dto.EvidenciaDto;
// import com.telconova.tracking.entity.Avance;
import com.telconova.tracking.entity.Evidencia;
import com.telconova.tracking.exception.InvalidFileException;
import com.telconova.tracking.exception.ResourceNotFoundException;
import com.telconova.tracking.mapper.EvidenciaMapper;
import com.telconova.tracking.repository.AvanceRepository;
import com.telconova.tracking.repository.EvidenciaRepository;
import com.telconova.tracking.service.EvidenciaService;
import com.telconova.tracking.service.storage.StorageService;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;


@Service
@Transactional
public class EvidenciaServiceImpl implements EvidenciaService {

    private static final Logger logger = LoggerFactory.getLogger(EvidenciaServiceImpl.class);
    private static final List<String> ALLOWED_CONTENT_TYPES =
            Arrays.asList("image/jpeg", "image/png", "image/gif", "application/pdf", "text/plain");
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
    private static final int MAX_FILES_PER_AVANCE = 5;

    private final EvidenciaRepository evidenciaRepository;
    private final AvanceRepository avanceRepository;
    private final StorageService storageService;
    private final EvidenciaMapper evidenciaMapper;
    private final MeterRegistry meterRegistry;

    public EvidenciaServiceImpl(EvidenciaRepository evidenciaRepository,
            AvanceRepository avanceRepository, StorageService storageService,
            EvidenciaMapper evidenciaMapper, MeterRegistry meterRegistry) {
        this.evidenciaRepository = evidenciaRepository;
        this.avanceRepository = avanceRepository;
        this.storageService = storageService;
        this.evidenciaMapper = evidenciaMapper;
        this.meterRegistry = meterRegistry;
    }

    @Override
    public EvidenciaDto uploadEvidencia(UUID avanceId, MultipartFile file)
            throws IOException, ResourceNotFoundException {
        Instant start = Instant.now();

        try {
            logger.debug("Iniciando carga de evidencia para avance: {}", avanceId);

            // Verificar si el avance existe
            avanceRepository.findById(avanceId)
                    .orElseThrow(() -> new ResourceNotFoundException("Avance", "id", avanceId));

            // Verificar si la orden está cerrada
            // Esto es un placeholder - en una implementación real, habría que verificar el estado
            // de la orden
            // a través de una consulta a otro servicio o mediante un campo en el avance

            // Validar el archivo
            validateFile(file);

            // Verificar límite de archivos
            long evidenciasCount = evidenciaRepository.findByAvanceId(avanceId).size();
            if (evidenciasCount >= MAX_FILES_PER_AVANCE) {
                throw new InvalidFileException(
                        String.format("Se ha excedido el límite de %d archivos por avance",
                                MAX_FILES_PER_AVANCE));
            }

            // Almacenar el archivo
            String storedPath = storageService.store(file, avanceId);

            // Crear entidad Evidencia
            Evidencia evidencia = new Evidencia();
            evidencia.setAvanceId(avanceId);
            String originalFilename = file.getOriginalFilename();
            String safeFilename =
                    originalFilename != null ? originalFilename : "archivo_sin_nombre";
            evidencia.setNombreArchivo(StringUtils.cleanPath(safeFilename));
            evidencia.setTipoContenido(file.getContentType());
            evidencia.setRutaArchivo(storedPath);
            evidencia.setTamanoBytes(file.getSize());

            // Guardar en base de datos
            evidencia = evidenciaRepository.save(evidencia);

            // Incrementar contador de métricas
            meterRegistry.counter("tracking.evidencia.uploaded").increment();

            // Convertir a DTO y devolver
            logger.info("Evidencia subida correctamente para avance: {}", avanceId);
            return evidenciaMapper.toDto(evidencia);
        } finally {
            // Registrar tiempo de procesamiento
            Duration duration = Duration.between(start, Instant.now());
            Timer.builder("tracking.evidencia.upload.time")
                    .description("Tiempo de carga de evidencia").register(meterRegistry)
                    .record(duration);
        }
    }

    @Override
    public List<EvidenciaDto> uploadMultipleEvidencias(UUID avanceId, List<MultipartFile> files)
            throws IOException, ResourceNotFoundException {
        logger.debug("Iniciando carga múltiple de evidencias para avance: {}", avanceId);

        // Verificar si el avance existe
        if (!avanceRepository.existsById(avanceId)) {
            throw new ResourceNotFoundException("Avance", "id", avanceId);
        }

        // Verificar límite de archivos
        long evidenciasCount = evidenciaRepository.findByAvanceId(avanceId).size();
        if (evidenciasCount + files.size() > MAX_FILES_PER_AVANCE) {
            throw new InvalidFileException(String.format(
                    "Se excedería el límite de %d archivos por avance", MAX_FILES_PER_AVANCE));
        }

        List<EvidenciaDto> uploadedEvidencias = new ArrayList<>();
        for (MultipartFile file : files) {
            uploadedEvidencias.add(uploadEvidencia(avanceId, file));
        }

        logger.info("Carga múltiple completada: {} evidencias para avance: {}",
                uploadedEvidencias.size(), avanceId);
        return uploadedEvidencias;
    }

    @Override
    public List<EvidenciaDto> findByAvanceId(UUID avanceId) {
        logger.debug("Buscando evidencias para avance: {}", avanceId);
        List<Evidencia> evidencias = evidenciaRepository.findByAvanceId(avanceId);
        return evidenciaMapper.toDto(evidencias);
    }

    @Override
    public EvidenciaDto findById(UUID id) throws ResourceNotFoundException {
        logger.debug("Buscando evidencia por ID: {}", id);
        Evidencia evidencia = evidenciaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evidencia", "id", id));
        EvidenciaDto dto = evidenciaMapper.toDto(evidencia);

        // Enriquecer con URL
        dto.setUrl(storageService.getUrl(evidencia.getRutaArchivo()));
        return dto;
    }

    @Override
    @Transactional
    public void deleteById(UUID id) throws ResourceNotFoundException {
        logger.debug("Eliminando evidencia por ID: {}", id);

        Evidencia evidencia = evidenciaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evidencia", "id", id));

        // Eliminar archivo del almacenamiento
        storageService.delete(evidencia.getRutaArchivo());

        // Eliminar registro de base de datos
        evidenciaRepository.delete(evidencia);

        logger.info("Evidencia eliminada correctamente: {}", id);
    }

    /**
     * Valida que el archivo cumpla con las restricciones
     * 
     * @param file Archivo a validar
     * @throws InvalidFileException Si el archivo no cumple con los requisitos
     */
    private void validateFile(MultipartFile file) throws InvalidFileException {
        // Verificar si el archivo está vacío
        if (file.isEmpty()) {
            throw new InvalidFileException("No se puede subir un archivo vacío");
        }

        // Verificar tipo de archivo
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType)) {
            throw new InvalidFileException("Tipo de archivo no permitido: " + contentType);
        }

        // Verificar tamaño
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new InvalidFileException(
                    String.format("El tamaño del archivo excede el máximo permitido de %dMB",
                            MAX_FILE_SIZE / (1024 * 1024)));
        }

        logger.debug("Archivo validado correctamente: {}", file.getOriginalFilename());
    }
}
