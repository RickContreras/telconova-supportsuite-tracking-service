package com.telconova.tracking.service;

import com.telconova.tracking.entity.Avance;
import com.telconova.tracking.exception.ResourceNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AvanceService {
    Avance save(Avance avance);

    List<Avance> findByOrdenId(Long ordenId);

    List<Avance> findAll();

    Optional<Avance> findById(UUID id);

    void deleteById(UUID id);

    /**
     * Edita el tiempo invertido de un avance y registra la justificación
     * 
     * @param avanceId ID del avance a editar
     * @param tiempoNuevo Nuevo valor de tiempo invertido
     * @param justificacion Razón de la modificación (obligatorio)
     * @return El avance actualizado
     * @throws ResourceNotFoundException Si el avance no existe
     * @throws IllegalArgumentException Si los datos de entrada son inválidos
     */
    Avance editarTiempoInvertido(UUID avanceId, Integer tiempoNuevo, String justificacion)
            throws ResourceNotFoundException, IllegalArgumentException;
}
