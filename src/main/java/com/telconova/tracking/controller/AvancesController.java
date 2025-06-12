package com.telconova.tracking.controller;

import com.telconova.tracking.dto.AvanceDto;
import com.telconova.tracking.entity.Avance;
import com.telconova.tracking.mapper.AvanceMapper;
import com.telconova.tracking.service.AvanceService;
import com.telconova.tracking.config.SecurityAuditLogger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import org.springframework.security.core.Authentication;
import java.util.*;

@RestController
@RequestMapping("/api/v1/avances")
@Tag(name = "Avances", description = "API para gestión de avances técnicos")
@RequiredArgsConstructor
public class AvancesController {

        private static final Logger logger = LoggerFactory.getLogger(AvancesController.class);
        private final AvanceService avanceService;
        private final AvanceMapper avanceMapper;
        private final SecurityAuditLogger securityAuditLogger;

        // TODO: Implementar bus de eventos
        // private final AvanceEventPublisher avanceEventPublisher;

        @Operation(summary = "Obtener todos los avances",
                        description = "Retorna un mensaje de confirmación que el endpoint está funcionando")
        @ApiResponses({@ApiResponse(responseCode = "200", description = "Operación exitosa"),
                        @ApiResponse(responseCode = "500",
                                        description = "Error interno del servidor")})
        // Accesible por todos los roles (TECNICO, SUPERVISOR, ADMIN)
        @GetMapping
        public ResponseEntity<List<AvanceDto>> getAvances() {
                logger.debug("Solicitando todos los avances");
                List<Avance> avances = avanceService.findAll();
                return ResponseEntity.ok(avanceMapper.toDto(avances));
        }

        @Operation(summary = "Obtener avances por orden",
                        description = "Retorna los avances asociados a una orden específica")
        @ApiResponses({@ApiResponse(responseCode = "200", description = "Operación exitosa"),
                        @ApiResponse(responseCode = "404", description = "Orden no encontrada"),
                        @ApiResponse(responseCode = "500",
                                        description = "Error interno del servidor")})
        @GetMapping("/orden/{orderId}")
        public ResponseEntity<List<AvanceDto>> getAvancesByOrden(@PathVariable Long orderId) {
                logger.debug("Solicitando avances para la orden: {}", orderId);
                List<Avance> avances = avanceService.findByOrdenId(orderId);
                return ResponseEntity.ok(avanceMapper.toDto(avances));
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
        public ResponseEntity<AvanceDto> createAvance(@Valid @RequestBody AvanceDto avanceDto) {
                logger.debug("Recibida solicitud para crear avance: {}", avanceDto);

                // Validar datos del avance
                validateAvanceData(avanceDto);

                // Convertir DTO a entidad y guardar
                Avance avance = avanceMapper.toEntity(avanceDto);
                Avance savedAvance = avanceService.save(avance);

                // Registrar acción
                securityAuditLogger.logSensitiveAction("CREAR_AVANCE",
                                savedAvance.getId().toString());

                // TODO: Implementar bus de eventos
                // Aquí se publicaría el evento de avance registrado
                // Aquí se publicaría el evento de avance registrado
                // avanceEventPublisher.publishAvanceRegistrado(savedAvance);

                // Devolver respuesta
                AvanceDto responseDto = avanceMapper.toDto(savedAvance);
                return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
        }

        /**
         * Valida los datos del avance según reglas de negocio
         */
        private void validateAvanceData(AvanceDto avanceDto) {
                // Validar comentario (entre 20 y 500 caracteres)
                if (avanceDto.getComentario() == null || avanceDto.getComentario().length() < 20
                                || avanceDto.getComentario().length() > 500) {
                        throw new IllegalArgumentException(
                                        "El comentario debe tener entre 20 y 500 caracteres");
                }

                // Validar tiempo invertido (debe ser positivo)
                if (avanceDto.getTiempoInvertido() == null || avanceDto.getTiempoInvertido() <= 0) {
                        throw new IllegalArgumentException(
                                        "El tiempo invertido debe ser un valor positivo");
                }

                // Validar que exista un ID de orden
                if (avanceDto.getOrdenId() == null) {
                        throw new IllegalArgumentException("Se debe especificar el ID de la orden");
                }
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

        @Operation(summary = "Editar comentario de avance",
                        description = "Permite a un técnico editar el comentario de un avance que haya creado")
        @ApiResponses({@ApiResponse(responseCode = "200",
                        description = "Comentario actualizado correctamente"),
                        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
                        @ApiResponse(responseCode = "403",
                                        description = "No tiene permisos para editar este comentario"),
                        @ApiResponse(responseCode = "404", description = "Avance no encontrado")})
        @PutMapping("/{avanceId}/comentario")
        @PreAuthorize("hasRole('TECNICO')")
        public ResponseEntity<Map<String, Object>> editarComentario(@PathVariable UUID avanceId,
                        @RequestParam @Size(min = 20, max = 500,
                                        message = "El comentario debe tener entre 20 y 500 caracteres") String comentario,
                        Authentication authentication) {



                // Buscar el avance
                Avance avance = avanceService.findById(avanceId)
                                .orElseThrow(() -> new RuntimeException(
                                                "Avance no encontrado con ID: " + avanceId));



                // Validar el comentario
                if (comentario == null || comentario.trim().length() < 20
                                || comentario.length() > 500) {
                        throw new IllegalArgumentException(
                                        "El comentario debe tener entre 20 y 500 caracteres");
                }

                // Actualizar el comentario
                avance.setComentario(comentario);
                avance.setModificadoEn(LocalDateTime.now());
                Avance avanceActualizado = avanceService.save(avance);

                // Preparar respuesta
                Map<String, Object> response = new HashMap<>();
                response.put("avanceId", avanceId.toString());
                response.put("mensaje", "Comentario actualizado correctamente");
                response.put("comentario", avanceActualizado.getComentario());
                response.put("fechaModificacion", avanceActualizado.getModificadoEn());

                return ResponseEntity.ok(response);
        }
}
