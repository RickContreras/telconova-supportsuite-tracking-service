# 📡 Endpoints REST

| Método | Endpoint                                     | Descripción |
|--------|----------------------------------------------|-------------|
| `POST` | `/api/tracking/{orderId}/avances`            | Registrar un avance técnico |
| `POST` | `/api/tracking/{avanceId}/evidencias`        | Subir evidencias adjuntas |
| `PATCH`| `/api/tracking/{avanceId}/tiempo`            | Editar tiempo invertido con justificación |
| `GET`  | `/api/tracking/orden/{orderId}`              | Consultar todos los avances de una orden |

## Detalles de los endpoints

### POST /api/tracking/{orderId}/avances

Registra un nuevo avance técnico asociado a una orden.

**Request Body:**
```json
{
  "comentario": "Descripción del avance realizado (20-500 caracteres)",
  "tiempoInvertido": 45
}
```

**Response:**
```json
{
  "id": "uuid-del-avance",
  "comentario": "Descripción del avance realizado",
  "tiempoInvertido": 45,
  "tecnicoId": "id del tecnico",
  "creadoEn": "2025-06-01T14:30:00Z"
}
```