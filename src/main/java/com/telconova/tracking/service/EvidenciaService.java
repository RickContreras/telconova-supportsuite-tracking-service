package com.telconova.tracking.service;

import com.telconova.tracking.dto.EvidenciaDto;
import com.telconova.tracking.exception.ResourceNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface EvidenciaService {

    /**
     * Guarda una evidencia asociada a un avance
     * 
     * @param avanceId ID del avance al que se asocia la evidencia
     * @param file Archivo subido
     * @return DTO con la información de la evidencia guardada
     * @throws IOException Si ocurre un error durante la carga del archivo
     * @throws ResourceNotFoundException Si el avance no existe
     */
    EvidenciaDto uploadEvidencia(UUID avanceId, MultipartFile file)
            throws IOException, ResourceNotFoundException;

    /**
     * Guarda múltiples evidencias asociadas a un avance
     * 
     * @param avanceId ID del avance al que se asocian las evidencias
     * @param files Lista de archivos subidos
     * @return Lista de DTOs con la información de las evidencias guardadas
     * @throws IOException Si ocurre un error durante la carga de archivos
     * @throws ResourceNotFoundException Si el avance no existe
     */
    List<EvidenciaDto> uploadMultipleEvidencias(UUID avanceId, List<MultipartFile> files)
            throws IOException, ResourceNotFoundException;

    /**
     * Obtiene todas las evidencias asociadas a un avance
     * 
     * @param avanceId ID del avance
     * @return Lista de DTOs con la información de las evidencias
     */
    List<EvidenciaDto> findByAvanceId(UUID avanceId);

    /**
     * Obtiene una evidencia por su ID
     * 
     * @param id ID de la evidencia
     * @return DTO con la información de la evidencia
     * @throws ResourceNotFoundException Si la evidencia no existe
     */
    EvidenciaDto findById(UUID id) throws ResourceNotFoundException;

    /**
     * Elimina una evidencia por su ID
     * 
     * @param id ID de la evidencia
     * @throws ResourceNotFoundException Si la evidencia no existe
     */
    void deleteById(UUID id) throws ResourceNotFoundException;
}
