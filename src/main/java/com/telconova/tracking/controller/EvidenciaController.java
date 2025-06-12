package com.telconova.tracking.controller;

import com.telconova.tracking.dto.EvidenciaDto;
import com.telconova.tracking.dto.ErrorResponse;
import com.telconova.tracking.exception.AccessDeniedException;
import com.telconova.tracking.exception.InvalidFileException;
import com.telconova.tracking.exception.ResourceNotFoundException;
import com.telconova.tracking.service.EvidenciaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.core.Authentication;


import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/avances")
@Tag(name = "Evidencias", description = "API para gestión de evidencias de avances técnicos")
public class EvidenciaController {

        private static final Logger logger = LoggerFactory.getLogger(EvidenciaController.class);
        private final EvidenciaService evidenciaService;

        public EvidenciaController(EvidenciaService evidenciaService) {
                this.evidenciaService = evidenciaService;
        }

        @Operation(summary = "Subir una evidencia para un avance",
                        description = "Permite subir una imagen, documento u otro archivo como evidencia de un avance técnico")
        @ApiResponses({@ApiResponse(responseCode = "201",
                        description = "Evidencia guardada correctamente"),
                        @ApiResponse(responseCode = "400", description = "Archivo inválido",
                                        content = @Content(schema = @Schema(
                                                        implementation = ErrorResponse.class))),
                        @ApiResponse(responseCode = "404", description = "Avance no encontrado",
                                        content = @Content(schema = @Schema(
                                                        implementation = ErrorResponse.class))),
                        @ApiResponse(responseCode = "413", description = "Archivo demasiado grande",
                                        content = @Content(schema = @Schema(
                                                        implementation = ErrorResponse.class))),
                        @ApiResponse(responseCode = "500",
                                        description = "Error interno del servidor",
                                        content = @Content(schema = @Schema(
                                                        implementation = ErrorResponse.class)))})
        @PostMapping(value = "/{avanceId}/evidencias",
                        consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        @PreAuthorize("hasAnyRole('TECNICO', 'ADMIN')")
        public ResponseEntity<EvidenciaDto> uploadEvidencia(@PathVariable UUID avanceId,
                        @RequestParam("file") MultipartFile file) {
                try {
                        logger.info("Solicitud para subir evidencia al avance: {}", avanceId);
                        EvidenciaDto savedEvidencia =
                                        evidenciaService.uploadEvidencia(avanceId, file);
                        return ResponseEntity.status(HttpStatus.CREATED).body(savedEvidencia);
                } catch (ResourceNotFoundException e) {
                        logger.error("Avance no encontrado: {}", avanceId, e);
                        throw e;
                } catch (InvalidFileException e) {
                        logger.error("Error de validación de archivo: {}", e.getMessage(), e);
                        throw e;
                } catch (IOException e) {
                        logger.error("Error de E/S al subir archivo: {}", e.getMessage(), e);
                        throw new RuntimeException("Error al procesar el archivo", e);
                }
        }

        @Operation(summary = "Subir múltiples evidencias para un avance",
                        description = "Permite subir varias imágenes, documentos u otros archivos como evidencia de un avance técnico")
        @ApiResponses({@ApiResponse(responseCode = "201",
                        description = "Evidencias guardadas correctamente"),
                        @ApiResponse(responseCode = "400", description = "Archivo inválido",
                                        content = @Content(schema = @Schema(
                                                        implementation = ErrorResponse.class))),
                        @ApiResponse(responseCode = "404", description = "Avance no encontrado",
                                        content = @Content(schema = @Schema(
                                                        implementation = ErrorResponse.class))),
                        @ApiResponse(responseCode = "413", description = "Archivo demasiado grande",
                                        content = @Content(schema = @Schema(
                                                        implementation = ErrorResponse.class))),
                        @ApiResponse(responseCode = "500",
                                        description = "Error interno del servidor",
                                        content = @Content(schema = @Schema(
                                                        implementation = ErrorResponse.class)))})
        @PostMapping(value = "/{avanceId}/evidencias/multiple",
                        consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        @PreAuthorize("hasAnyRole('TECNICO', 'ADMIN')")
        public ResponseEntity<List<EvidenciaDto>> uploadMultipleEvidencias(
                        @PathVariable UUID avanceId,
                        @RequestParam("files") List<MultipartFile> files) {
                try {
                        logger.info("Solicitud para subir {} evidencias al avance: {}",
                                        files.size(), avanceId);
                        List<EvidenciaDto> savedEvidencias =
                                        evidenciaService.uploadMultipleEvidencias(avanceId, files);
                        return ResponseEntity.status(HttpStatus.CREATED).body(savedEvidencias);
                } catch (ResourceNotFoundException e) {
                        logger.error("Avance no encontrado: {}", avanceId, e);
                        throw e;
                } catch (InvalidFileException e) {
                        logger.error("Error de validación de archivos: {}", e.getMessage(), e);
                        throw e;
                } catch (IOException e) {
                        logger.error("Error de E/S al subir archivos: {}", e.getMessage(), e);
                        throw new RuntimeException("Error al procesar los archivos", e);
                }
        }

        @Operation(summary = "Obtener evidencias por avance",
                        description = "Recupera todas las evidencias asociadas a un avance técnico")
        @ApiResponses({@ApiResponse(responseCode = "200", description = "Evidencias encontradas"),
                        @ApiResponse(responseCode = "404", description = "Avance no encontrado",
                                        content = @Content(schema = @Schema(
                                                        implementation = ErrorResponse.class)))})
        @GetMapping("/{avanceId}/evidencias")
        @PreAuthorize("hasAnyRole('TECNICO', 'ADMIN', 'SUPERVISOR')")
        public ResponseEntity<List<EvidenciaDto>> getEvidenciasByAvance(
                        @PathVariable UUID avanceId) {
                logger.info("Obteniendo evidencias para el avance: {}", avanceId);
                List<EvidenciaDto> evidencias = evidenciaService.findByAvanceId(avanceId);
                return ResponseEntity.ok(evidencias);
        }

        @Operation(summary = "Obtener evidencia por ID",
                        description = "Recupera una evidencia específica por su ID")
        @ApiResponses({@ApiResponse(responseCode = "200", description = "Evidencia encontrada"),
                        @ApiResponse(responseCode = "404", description = "Evidencia no encontrada",
                                        content = @Content(schema = @Schema(
                                                        implementation = ErrorResponse.class)))})
        @GetMapping("/evidencias/{id}")
        @PreAuthorize("hasAnyRole('TECNICO', 'ADMIN', 'SUPERVISOR')")
        public ResponseEntity<EvidenciaDto> getEvidenciaById(@PathVariable UUID id) {
                logger.info("Obteniendo evidencia con ID: {}", id);
                EvidenciaDto evidencia = evidenciaService.findById(id);
                return ResponseEntity.ok(evidencia);
        }

        @Operation(summary = "Eliminar evidencia",
                        description = "Elimina una evidencia específica por su ID")
        @ApiResponses({@ApiResponse(responseCode = "204",
                        description = "Evidencia eliminada correctamente"),
                        @ApiResponse(responseCode = "404", description = "Evidencia no encontrada",
                                        content = @Content(schema = @Schema(
                                                        implementation = ErrorResponse.class))),
                        @ApiResponse(responseCode = "403", description = "No autorizado",
                                        content = @Content(schema = @Schema(
                                                        implementation = ErrorResponse.class)))})
        @DeleteMapping("/evidencias/{id}")
        @PreAuthorize("hasAnyRole('TECNICO', 'ADMIN')")
        public ResponseEntity<Object> deleteEvidencia(@PathVariable UUID id,
                        Authentication authentication) {

                try {
                        logger.info("Solicitud para eliminar evidencia con ID: {}", id);
                        // Eliminar evidencia

                        // Verificar permisos del usuario autenticado
                        if (!authentication.getAuthorities().stream()
                                        .anyMatch(grantedAuthority -> grantedAuthority
                                                        .getAuthority().equals("ROLE_ADMIN")
                                                        || grantedAuthority.getAuthority()
                                                                        .equals("ROLE_TECNICO"))) {
                                throw new AccessDeniedException(
                                                "No tiene permisos para eliminar esta evidencia");
                        }

                        evidenciaService.deleteById(id);
                        return ResponseEntity.noContent().build();

                } catch (ResourceNotFoundException e) {
                        logger.error("Evidencia no encontrada: {}", id, e);
                        ErrorResponse error = new ErrorResponse("NOT_FOUND", e.getMessage());
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);

                } catch (AccessDeniedException e) {
                        logger.error("Acceso denegado al eliminar evidencia: {}", id, e);
                        ErrorResponse error = new ErrorResponse("FORBIDDEN", e.getMessage());
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);

                } catch (Exception e) {
                        logger.error("Error al eliminar evidencia: {}", id, e);
                        ErrorResponse error = new ErrorResponse("INTERNAL_ERROR",
                                        "Error al eliminar la evidencia");
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
                }
        }
}
