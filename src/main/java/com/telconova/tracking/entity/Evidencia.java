package com.telconova.tracking.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "evidencias")
@Data
public class Evidencia {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @Column(name = "avance_id", nullable = false)
    private UUID avanceId;

    @Column(nullable = false)
    private String nombreArchivo;

    @Column(nullable = false)
    private String tipoContenido;

    @Column(nullable = false)
    private String rutaArchivo;

    @Column(nullable = false)
    private Long tamanoBytes;

    @Column(name = "creado_en", nullable = false)
    private LocalDateTime creadoEn;

    @PrePersist
    protected void onCreate() {
        creadoEn = LocalDateTime.now();
    }
}
