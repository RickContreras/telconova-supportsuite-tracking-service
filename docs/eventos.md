# 📤 Eventos del Servicio

## Eventos publicados

| Evento                         | Cuándo se emite                       |
|--------------------------------|---------------------------------------|
| `avance.registrado`            | Al crear un nuevo avance             |
| `avance.evidencia.subida`      | Al subir evidencia asociada          |
| `avance.tiempo.modificado`     | Al editar tiempo invertido           |

### Estructura del evento `avance.registrado`

```json
{
  "avanceId": "uuid-del-avance",
  "ordenId": "id-de-la-orden",
  "tecnicoId": "id-del-tecnico",
  "comentario": "Texto del avance",
  "tiempoInvertido": 45,
  "timestamp": "2025-06-01T14:30:00Z"
}
```

## Eventos Consumidos

| Evento                    | Emisor               | Uso en Tracking-Service                                        |
| ------------------------- | -------------------- | -------------------------------------------------------------- |
| `order.status.changed`    | `workorder-service`  | Verificar si la orden aún permite registrar avances.           |
| `assignment.created`      | `assignment-service` | Habilitar el registro de avances solo si hay técnico asignado. |
| `order.closed` (opcional) | `workorder-service`  | Marcar los avances como bloqueados (solo lectura).             |