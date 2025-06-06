# Estructura del Proyecto

```
supportsuite-tracking-service/
├── src/
│   └── main/
│       ├── java/com/telconova/tracking/
│       │   ├── config/                    # Configuración de Swagger, RabbitMQ, WebSocket, etc.
│       │   │   ├── SwaggerConfig.java
│       │   │   ├── RabbitConfig.java
│       │   │   └── WebSocketConfig.java
│       │   │
│       │   ├── controller/                # Controladores REST para avances y evidencias
│       │   │   │── v1/
│       │   │   │   └── AvanceController.java              
│       │   │   └── v2/
│       │   │       └── AvanceController.java              
│       │   ├── dto/                       # DTOs (entrada/salida) para REST y WebSocket
│       │   │   ├── AvanceRequestDTO.java
│       │   │   ├── AvanceResponseDTO.java
│       │   │   ├── TiempoEditDTO.java
│       │   │   └── EvidenciaDTO.java
│       │   │
│       │   ├── entity/                    # Entidades JPA
│       │   │   ├── Avance.java
│       │   │   ├── Evidencia.java
│       │   │   └── AvanceEdicion.java
│       │   │
│       │   ├── repository/                # Repositorios Spring Data JPA
│       │   │   ├── AvanceRepository.java
│       │   │   ├── EvidenciaRepository.java
│       │   │   └── AvanceEdicionRepository.java
│       │   │
│       │   ├── service/                   # Lógica de negocio
│       │   │   ├── AvanceService.java
│       │   │   ├── EvidenciaService.java
│       │   │   ├── AvanceEdicionService.java
│       │   │   └── storage/
│       │   │       └── StorageService.java              # Interfaz común (Strategy pattern)
│       │   │           ├── model/
│       │   │           │   └── UploadedFile.java            # Metadata del archivo
│       │   │           ├── s3/
│       │   │           │   └── S3StorageService.java
│       │   │           ├── azure/
│       │   │           │   └── AzureBlobStorageService.java
│       │   │           └── minio/
│       │   │               └── MinioStorageService.java
│       │   ├── event/                     # RabbitMQ: eventos emitidos y escuchados
│       │   │   ├── publisher/
│       │   │   │   └── AvanceEventPublisher.java
│       │   │   ├── listener/
│       │   │   │   └── OrderStatusListener.java
│       │   │   └── payload/
│       │   │       └── AvanceRegistradoEvent.java
│       │   │
│       │   ├── websocket/                 # WebSocket con STOMP
│       │   │   ├── controller/
│       │   │   │   └── AvanceSocketController.java
│       │   │   ├── payload/
│       │   │   │   ├── AvanceSocketRequest.java
│       │   │   │   └── AvanceSocketNotification.java
│       │   │   └── publisher/
│       │   │       └── AvanceSocketPublisher.java
│       │   │
│       │   ├── mapper/                    # MapStruct o manuales
│       │   │   └── AvanceMapper.java
│       │   │
│       │   └── TrackingServiceApplication.java  # Clase principal Spring Boot
│       │
│       └── resources/
│           ├── application.yml           # Configuración (perfil por entorno)
│           ├── application-dev.yml
│           ├── application-prod.yml
│           ├── logback-spring.xml        # Logs
│           └── db/changelog/            # Liquidbase
│
├── src/test/java/com/telconova/tracking/
│   ├── controller/                       # Tests de endpoints
│   ├── service/                          # Tests unitarios
│   └── integration/                      # Tests de integración (WebSocket, RabbitMQ)
│
├── Dockerfile                            # Imagen base de Spring Boot
├── docker-compose.yml                    # Para entorno local con PostgreSQL, RabbitMQ, MinIO
├── README.md                             # Documentación del servicio
├── .env                                  # Variables locales
├── .gitignore
└── pom.xml                               # Dependencias Maven
```

## Patrones de diseño aplicados

- **Strategy Pattern**: Para el almacenamiento de archivos (S3, Azure, MinIO)
- **DTO Pattern**: Para transferencia de datos entre capas
- **Repository Pattern**: Acceso a datos
- **Publisher/Subscriber**: Para eventos y WebSockets