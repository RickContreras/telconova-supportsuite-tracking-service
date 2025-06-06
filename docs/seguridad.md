# Políticas de Seguridad

## Roles y permisos

- **TECNICO**: Puede registrar avances y subir evidencias para sus órdenes asignadas
- **SUPERVISOR**: Puede ver todos los avances y evidencias
- **ADMIN**: Control total del sistema, incluida la edición de tiempos invertidos

## Autenticación

- **JWT**: Validación de firma y expiración.
- Token transmitido en header `Authorization: Bearer <token>` 

## Control de acceso

- Se utiliza `@PreAuthorize` por rol para proteger endpoints:
  ```java
  @PreAuthorize("hasRole('TECNICO') or hasRole('SUPERVISOR')")
  ```

## Validación

- Validación de entrada con Bean Validation (@Valid)
- Sanitización de datos para prevenir XSS
- Límites en tamaño de archivos y tipos MIME permitidos

## Auditoría

- Registro de cambios en tiempo invertido con justificación obligatoria
- Logs de acceso y operaciones sensibles