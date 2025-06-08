package com.telconova.tracking.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "avance_ediciones")
@Data
public class AvanceEdicion {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @Column(name = "avance_id", nullable = false)
    private UUID avanceId;

    @Column(name = "tiempo_anterior", nullable = false)
    private Integer tiempoAnterior;

    @Column(name = "tiempo_nuevo", nullable = false)
    private Integer tiempoNuevo;

    @Column(name = "justificacion", nullable = false)
    private String justificacion;

    @Column(name = "modificado_en", nullable = false)
    private LocalDateTime modificadoEn;

    @PrePersist
    protected void onCreate() {
        modificadoEn = LocalDateTime.now();
    }
}
