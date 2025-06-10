package com.telconova.tracking.config;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Component
public class SecurityAuditLogger {

    private static final Logger securityLogger = LoggerFactory.getLogger("security-audit");

    public void logAuthenticationSuccess(String username) {
        securityLogger.info("Autenticación exitosa: usuario={}", username);
    }

    public void logAuthenticationFailure(String username, String reason) {
        securityLogger.warn("Autenticación fallida: usuario={}, razón={}", username, reason);
    }

    public void logAccessDenied(HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth != null ? auth.getName() : "anonymous";
        securityLogger.warn("Acceso denegado: usuario={}, ruta={}, método={}", username,
                request.getRequestURI(), request.getMethod());
    }

    public void logSensitiveAction(String action, String resourceId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth != null ? auth.getName() : "unknown";
        securityLogger.info("Acción sensible: usuario={}, acción={}, recurso={}", username, action,
                resourceId);
    }
}
