package com.telconova.tracking.exception;

import com.telconova.tracking.dto.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

        private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

        @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
                        ResourceNotFoundException ex, WebRequest request) {
                logger.warn("Recurso no encontrado: {}", ex.getMessage());

                ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value(),
                                ex.getMessage(), LocalDateTime.now());
                error.setPath(request.getDescription(false).replace("uri=", ""));

                return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }

        @ExceptionHandler(AuthorizationDeniedException.class)
        public ResponseEntity<ErrorResponse> handleAuthorizationDeniedException(
                        AuthorizationDeniedException ex, WebRequest request) {
                logger.warn("Autorización denegada: {}", ex.getMessage());

                ErrorResponse error = new ErrorResponse(HttpStatus.FORBIDDEN.value(),
                                "No tienes los permisos necesarios para acceder a este recurso",
                                LocalDateTime.now());
                error.setPath(request.getDescription(false).replace("uri=", ""));
                error.setError("Acceso Denegado");

                return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
        }

        @ExceptionHandler(AccessDeniedException.class)
        public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex,
                        WebRequest request) {
                logger.warn("Acceso denegado: {}", ex.getMessage());

                ErrorResponse error = new ErrorResponse(HttpStatus.FORBIDDEN.value(),
                                "No tienes los permisos necesarios para realizar esta operación",
                                LocalDateTime.now());
                error.setPath(request.getDescription(false).replace("uri=", ""));
                error.setError("Acceso Denegado");

                return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
        }

        @ExceptionHandler(InvalidFileException.class)
        public ResponseEntity<ErrorResponse> handleInvalidFileException(InvalidFileException ex,
                        WebRequest request) {
                logger.warn("Archivo inválido: {}", ex.getMessage());

                ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(),
                                ex.getMessage(), LocalDateTime.now());
                error.setPath(request.getDescription(false).replace("uri=", ""));

                return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }


        protected ResponseEntity<Object> handleMaxUploadSizeExceededException(
                        MaxUploadSizeExceededException ex, HttpHeaders headers, HttpStatus status,
                        WebRequest request) {

                logger.warn("Tamaño de archivo excedido: {}", ex.getMessage());

                ErrorResponse error = new ErrorResponse(HttpStatus.PAYLOAD_TOO_LARGE.value(),
                                "El tamaño del archivo excede el límite permitido",
                                LocalDateTime.now());
                error.setPath(request.getDescription(false).replace("uri=", ""));

                return new ResponseEntity<>(error, HttpStatus.PAYLOAD_TOO_LARGE);
        }

        @ExceptionHandler(ConstraintViolationException.class)
        public ResponseEntity<ErrorResponse> handleConstraintViolationException(
                        ConstraintViolationException ex, WebRequest request) {
                logger.warn("Validación fallida: {}", ex.getMessage());

                ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(),
                                ex.getMessage(), LocalDateTime.now());
                error.setPath(request.getDescription(false).replace("uri=", ""));

                return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorResponse> handleGenericException(Exception ex,
                        WebRequest request) {
                logger.error("Error no controlado: ", ex);

                ErrorResponse error = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                "Error interno del servidor: " + ex.getMessage(),
                                LocalDateTime.now());
                error.setPath(request.getDescription(false).replace("uri=", ""));

                return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
}
