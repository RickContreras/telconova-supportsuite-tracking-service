# 🌐 WebSocket

## Canal de comunicación

- Canal: `/ws/tracking/{orderId}`
- Publica eventos en tiempo real cuando se registra un nuevo avance o se edita un avance existente
- Usado por dashboards técnicos y operativos

## Protocolo

- Se utiliza STOMP como protocolo de mensajería sobre WebSockets
- La configuración está en `com.telconova.tracking.config.WebSocketConfig`

## Payloads

### Notificación de nuevo avance

```json
{
  "tipo": "AVANCE_CREADO",
  "avanceId": "uuid-del-avance",
  "ordenId": "id-de-la-orden",
  "comentario": "Texto del avance",
  "tecnicoId": "id-del-tecnico",
  "tecnicoNombre": "Nombre del técnico",
  "tiempoInvertido": 45,
  "timestamp": "2025-06-01T14:30:00Z"
}
```

### Notificación de tiempo modificado

```json
{
  "tipo": "TIEMPO_MODIFICADO",
  "avanceId": "uuid-del-avance",
  "ordenId": "id-de-la-orden",
  "tiempoAnterior": 30,
  "tiempoNuevo": 45,
  "justificacion": "Texto de justificación",
  "timestamp": "2025-06-01T14:35:00Z" 
}
```

## Componentes

El controlador `AvanceSocketController` maneja la lógica de mensajería WebSocket