# Métricas y Monitoreo

## Prometheus

Exposición en `/actuator/prometheus` de métricas:
  * Número de avances registrados
  * Tamaño total de evidencias subidas

## Actuator

Endpoints disponibles:
* `/actuator/health` - Estado del servicio
* `/actuator/metrics` - Métricas detalladas  

## Logging

* Configurado con Logback
* Niveles diferentes según entorno (INFO en producción, DEBUG en desarrollo)
* Formato estructurado JSON para integración con ELK/Grafana

## Alertas

* Configuración de reglas en Prometheus/Grafana para notificar:
  * Latencia alta en operaciones de guardado
  * Errores recurrentes en carga de evidencias
  * Quedarse sin espacio de almacenamiento