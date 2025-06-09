package com.telconova.tracking.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class AvanceDto {
    private UUID id;
    private UUID ordenId;
    private UUID tecnicoId;
    private String comentario;
    private Integer tiempoInvertido;
    private LocalDateTime creadoEn;
    private LocalDateTime modificadoEn;
}
