# 🗃 Modelo de datos

## Tabla: `avances`

| Campo             | Tipo       | Descripción                          |
|------------------|------------|--------------------------------------|
| `id`              | UUID       | ID del avance                        |
| `orden_id`        | BIGINT     | FK a la orden                        |
| `tecnico_id`      | BIGINT     | Usuario que registró el avance       |
| `comentario`      | TEXT       | Texto del avance (20–500 caracteres) |
| `tiempo_invertido`| INTEGER    | Minutos invertidos                   |
| `creado_en`       | TIMESTAMP  | Fecha de creación                    |
| `modificado_en`   | TIMESTAMP  | Fecha de última modificación         |

## Tabla: `evidencias`

| Campo           | Tipo     | Descripción                            |
|------------------|----------|----------------------------------------|
| `id`             | UUID     | ID del archivo                         |
| `avance_id`      | UUID     | FK al avance                           |
| `url`            | TEXT     | Ubicación del archivo (S3/Blob)        |
| `tipo_mime`      | TEXT     | Tipo de archivo (`image/jpeg`, etc.)   |
| `tamaño`         | INTEGER  | Tamaño en bytes                        |

## Tabla: `avance_ediciones`

| Campo            | Tipo      | Descripción                            |
|------------------|-----------|----------------------------------------|
| `id`             | UUID      | ID de la edición                       |
| `avance_id`      | UUID      | FK al avance editado                   |
| `tiempo_anterior`| INTEGER   | Valor anterior                         |
| `tiempo_nuevo`   | INTEGER   | Valor actualizado                      |
| `justificacion`  | TEXT      | Justificación obligatoria              |
| `modificado_en`  | TIMESTAMP | Fecha y hora del cambio                |

## Diagrama de relaciones

```
Avance 1:N Evidencia
Avance 1:N AvanceEdicion
```