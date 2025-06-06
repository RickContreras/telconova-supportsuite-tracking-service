# 🧱 Arquitectura del Módulo

## Componentes principales

- **Controllers**  
  - `AvanceController`: endpoints para registrar avances, subir evidencias, editar tiempo invertido.

- **Services**  
  - `AvanceService`: lógica central para validar y guardar avances.  
  - `EvidenciaService`: validación y carga de archivos adjuntos.

- **Repositories**  
  - `AvanceRepository`  
  - `EvidenciaRepository`  
  - `AvanceEdicionRepository` (auditoría de cambios en tiempo invertido)

- **Event Publisher**  
  - Publica eventos como:
    - `avance.registrado`  
    - `avance.evidencia.subida`  
    - `avance.tiempo.modificado`

## Reglas de validación

- **Comentario**: mínimo 20 caracteres, máximo 500.
- **Adjuntos**: máximo 3; tipos permitidos: `.pdf`, `.jpg`, `.jpeg`, `.png`, `.txt`.
- **Tiempo invertido**: campo entero (en minutos); editable con justificación.
- Avances solo permitidos si la orden **no está finalizada o cerrada** (control desde WorkOrder).