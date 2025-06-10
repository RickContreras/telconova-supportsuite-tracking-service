# Documentación de Seguridad JWT

## Visión General

El microservicio de tracking implementa autenticación basada en JWT (JSON Web Token) para proteger sus endpoints. Este microservicio no genera tokens, sino que valida los tokens generados por el microservicio de autenticación y usuarios.

## Configuración

### Variables de Entorno

| Variable | Descripción | Valor por defecto |
|----------|-------------|-------------------|
| JWT_SECRET | Clave secreta compartida para validar tokens | - |
| JWT_EXPIRATION | Tiempo de expiración de tokens (ms) | 3600000 |

### Propiedades (application.yml)

```yaml
app:
  jwt:
    secret: ${JWT_SECRET}
    expiration: ${JWT_EXPIRATION:3600000}
```

## Formato del Token JWT

El token JWT debe contener la siguiente estructura en sus claims:

```json
{
  "roles": ["TECNICO"],
  "sub": "username",
  "iat": 1749513193,
  "exp": 1749599593,
  "jti": "id-único",
  "iss": "telconova-auth-service",
  "aud": "telconova-apis",
  "nbf": 1749513193
}
```

## Flujo de Autenticación

1. El cliente obtiene un token JWT del microservicio de autenticación
2. El cliente incluye este token en el header Authorization en peticiones al microservicio de tracking
3. El filtro JwtAuthenticationFilter valida el token y establece el SecurityContext
4. Los controladores usan anotaciones @PreAuthorize para verificar roles


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