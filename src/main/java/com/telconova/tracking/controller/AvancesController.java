package com.telconova.tracking.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/avances")
@Tag(name = "Avances", description = "API para gestión de avances técnicos")
public class AvancesController {

        @Operation(summary = "Obtener todos los avances",
                        description = "Retorna un mensaje de confirmación que el endpoint está funcionando")
        @ApiResponses({@ApiResponse(responseCode = "200", description = "Operación exitosa"),
                        @ApiResponse(responseCode = "500",
                                        description = "Error interno del servidor")})
        @GetMapping
        public Map<String, String> getAvances() {
                return Collections.singletonMap("message",
                                "Endpoint de avances funcionando correctamente");
        }

        @Operation(summary = "Obtener avances por orden",
                        description = "Retorna los avances asociados a una orden específica")
        @ApiResponses({@ApiResponse(responseCode = "200", description = "Operación exitosa"),
                        @ApiResponse(responseCode = "404", description = "Orden no encontrada"),
                        @ApiResponse(responseCode = "500",
                                        description = "Error interno del servidor")})
        @GetMapping("/orden/{orderId}")
        public ResponseEntity<Map<String, Object>> getAvancesByOrden(@PathVariable UUID orderId) {
                return ResponseEntity.ok(Collections.singletonMap("orderId", orderId.toString()));
        }
}
