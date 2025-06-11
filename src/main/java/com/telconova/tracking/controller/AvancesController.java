package com.telconova.tracking.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.HttpStatus;
import com.telconova.tracking.dto.AvanceDto;
import com.telconova.tracking.entity.Avance;
import com.telconova.tracking.service.AvanceService;
import com.telconova.tracking.mapper.AvanceMapper;
import com.telconova.tracking.event.publisher.AvanceEventPublisher;
import com.telconova.tracking.security.JwtService;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;

import java.util.Collections;
import java.util.HashMap;
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
        // Accesible por todos los roles (TECNICO, SUPERVISOR, ADMIN)
        @GetMapping
        public Map<String, String> getAvances() {
                return Collections.singletonMap("message", "Lista de avances");
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

        // ENDPOINT ESPECÍFICO PARA TÉCNICO
        @Operation(summary = "Crear nuevo avance técnico",
                        description = "Permite a un técnico registrar un nuevo avance")
        @ApiResponses({@ApiResponse(responseCode = "201",
                        description = "Avance creado exitosamente"),
                        @ApiResponse(responseCode = "400",
                                        description = "Datos de avance inválidos"),
                        @ApiResponse(responseCode = "403",
                                        description = "No tiene permisos para crear avances"),
                        @ApiResponse(responseCode = "500",
                                        description = "Error interno del servidor")})
        @PostMapping
        @PreAuthorize("hasRole('TECNICO')")
        public ResponseEntity<Map<String, Object>> createAvance(
                        @RequestBody Map<String, Object> avanceData) {
                Map<String, Object> response = new HashMap<>();
                response.put("id", UUID.randomUUID().toString());
                response.put("message", "Avance creado correctamente");
                response.put("data", avanceData);

                return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }

        // ENDPOINT PARA EDITAR TIEMPO (SOLO TÉCNICOS)
        @Operation(summary = "Editar tiempo de avance",
                        description = "Permite a un técnico actualizar el tiempo de un avance")
        @ApiResponses({@ApiResponse(responseCode = "200",
                        description = "Tiempo actualizado correctamente"),
                        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
                        @ApiResponse(responseCode = "403",
                                        description = "No tiene permisos para editar tiempos"),
                        @ApiResponse(responseCode = "404", description = "Avance no encontrado"),
                        @ApiResponse(responseCode = "500",
                                        description = "Error interno del servidor")})
        @PutMapping("/{avanceId}/editar-tiempo")
        @PreAuthorize("hasRole('TECNICO')")
        public ResponseEntity<Map<String, Object>> editarTiempo(@PathVariable UUID avanceId,
                        @RequestBody Map<String, Object> tiempoData) {

                Map<String, Object> response = new HashMap<>();
                response.put("avanceId", avanceId.toString());
                response.put("message", "Tiempo actualizado correctamente");
                response.put("tiempoActualizado", tiempoData);

                return ResponseEntity.ok(response);
        }

        // ENDPOINT ESPECÍFICO PARA ADMIN
        @Operation(summary = "Eliminar avance",
                        description = "Permite a un administrador eliminar un avance")
        @ApiResponses({@ApiResponse(responseCode = "200",
                        description = "Avance eliminado correctamente"),
                        @ApiResponse(responseCode = "403",
                                        description = "No tiene permisos para eliminar avances"),
                        @ApiResponse(responseCode = "404", description = "Avance no encontrado"),
                        @ApiResponse(responseCode = "500",
                                        description = "Error interno del servidor")})
        @DeleteMapping("/{avanceId}")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<Map<String, Object>> eliminarAvance(@PathVariable UUID avanceId) {
                Map<String, Object> response = new HashMap<>();
                response.put("avanceId", avanceId.toString());
                response.put("message", "Avance eliminado correctamente");

                return ResponseEntity.ok(response);
        }

        // ENDPOINT ESPECÍFICO PARA ADMIN (REPORTES)
        @Operation(summary = "Obtener reporte de avances",
                        description = "Permite a administradores generar reportes de avances")
        @ApiResponses({@ApiResponse(responseCode = "200",
                        description = "Reporte generado correctamente"),
                        @ApiResponse(responseCode = "403",
                                        description = "No tiene permisos para generar reportes"),
                        @ApiResponse(responseCode = "500",
                                        description = "Error interno del servidor")})
        @GetMapping("/reportes/{tipoReporte}")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<Map<String, Object>> generarReporte(
                        @PathVariable String tipoReporte) {
                Map<String, Object> response = new HashMap<>();
                response.put("tipoReporte", tipoReporte);
                response.put("fechaGeneracion", java.time.LocalDateTime.now().toString());
                response.put("message", "Reporte generado correctamente");

                // Datos de ejemplo para el reporte
                Map<String, Object> datos = new HashMap<>();
                datos.put("totalAvances", 150);
                datos.put("avancesCompletados", 120);
                datos.put("avancesPendientes", 30);
                response.put("datos", datos);

                return ResponseEntity.ok(response);
        }
}
