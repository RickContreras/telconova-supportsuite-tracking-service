package com.telconova.tracking.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class EvidenciaDto {
    private UUID id;
    private UUID avanceId;
    private String nombreArchivo;
    private String tipoContenido;
    private Long tamanoBytes;
    private LocalDateTime creadoEn;
    private String url;
}
