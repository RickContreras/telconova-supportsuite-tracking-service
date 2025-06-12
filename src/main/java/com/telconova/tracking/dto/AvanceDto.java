package com.telconova.tracking.dto;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class AvanceDto {
    private UUID id;

    @NotNull(message = "El ID de la orden es obligatorio")
    private Long ordenId;

    @NotNull(message = "El ID del técnico es obligatorio")
    private Long tecnicoId;

    @NotNull(message = "El comentario es obligatorio")
    @Size(min = 20, max = 500, message = "El comentario debe tener entre 20 y 500 caracteres")
    private String comentario;

    @NotNull(message = "El tiempo invertido es obligatorio")
    @Min(value = 1, message = "El tiempo invertido debe ser mayor a 0")
    private Integer tiempoInvertido;

    private LocalDateTime creadoEn;
    private LocalDateTime modificadoEn;

    @PrePersist
    protected void onCreate() {
        creadoEn = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        modificadoEn = LocalDateTime.now();
    }
}
