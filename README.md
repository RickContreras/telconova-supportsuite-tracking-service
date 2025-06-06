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