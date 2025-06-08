package com.telconova.tracking.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "avances")
@Data
public class Avance {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @Column(name = "orden_id", nullable = false)
    private UUID ordenId;

    @Column(name = "tecnico_id", nullable = false)
    private UUID tecnicoId;

    @Column(nullable = false)
    private String comentario;

    @Column(name = "tiempo_invertido", nullable = false)
    private Integer tiempoInvertido;

    @Column(name = "creado_en", nullable = false)
    private LocalDateTime creadoEn;

    @Column(name = "modificado_en")
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
