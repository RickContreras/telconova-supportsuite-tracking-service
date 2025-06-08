# 📍 Tracking-Service (Gestión de avances técnicos)

Microservicio del sistema **TelcoNova SupportSuite** responsable de registrar y gestionar los **avances técnicos** realizados por los técnicos en campo. Cada avance puede incluir comentarios, evidencias y tiempo invertido, y se asocia directamente a una orden de trabajo.

## 📑 Documentación

- [Arquitectura del Módulo](docs/arquitectura.md)
- [API Endpoints](docs/api-endpoints.md)
- [Eventos](docs/eventos.md)
- [Modelo de Datos](docs/modelo-datos.md)
- [WebSockets](docs/websockets.md)
- [Tecnologías](docs/tecnologias.md)
- [Estructura del Proyecto](docs/estructura-proyecto.md)
- [Seguridad](docs/seguridad.md)
- [Métricas y Monitoreo](docs/metricas.md)

## 🧭 Responsabilidades principales

- Registrar **avances** (comentarios) para órdenes de trabajo
- Validar y almacenar **evidencias adjuntas**
- Permitir edición del **tiempo invertido** con justificación y auditoría
- Emitir **eventos de dominio** para otros microservicios
- Notificar actualizaciones **en tiempo real** mediante WebSocket

## Redis

Para comprobar la funcionalidad de redis ejecute esto:

```sh
redis-cli -h redis ping #Recibir una respuesta de redis
redis-cli -h redis #Entrar al cli de redis.
```

## Requisitos

- Java 21
- Maven 3.6.3
- Docker 28.1.1-1 y Docker Compose 2.36.2-1
- PostgreSQL 13.21
- Redis 6.0.16

## Configuración

Copia el archivo `.env.minimal.example` a `.env` y ajusta las variables según tu entorno.

## Ejecución

### Local con Maven

Una vez creado el archivo .env ejecutar el siguiente comando:

```bash
export $(cat .env | xargs)
```

Luego ejecute la instalacion de paquetes y corra el proyecto.

```bash
mvn clean install
mvn spring-boot:run
```

## 📚 Recursos Adicionales

* [Spring WebFlux Reference](https://docs.spring.io/spring-framework/docs/current/reference/html/web-reactive.html)
* [Azure Blob Storage SDK](https://docs.microsoft.com/azure/storage/blobs)
* [RFC 6455 WebSockets](https://tools.ietf.org/html/rfc6455)