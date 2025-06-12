package com.telconova.tracking.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EdicionTiempoRequest {

    @NotNull(message = "El nuevo tiempo es obligatorio")
    @Min(value = 1, message = "El tiempo debe ser un valor positivo")
    private Integer tiempoNuevo;

    @NotBlank(message = "La justificación es obligatoria")
    @Size(min = 10, max = 200, message = "La justificación debe tener entre 10 y 200 caracteres")
    private String justificacion;
}
