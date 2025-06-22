Como activar el monitereo en local.

Se inicia el devcontainer

Se garantiza que el archivo prometheus.yml coincida con los nombres de los microservicios.

Lanzar las bases de datos adicionales, cada microservicio con una sola.

# Base de datos para el servicio de usuarios
docker run -d \
  --name db-usuarios \
  --network telconova-supportsuite-tracking-service_devcontainer_default \
  -e POSTGRES_USER=usuarios_user \
  -e POSTGRES_PASSWORD=root1 \
  -e POSTGRES_DB=usuarios_db \
  -p 5433:5432 \
  postgres:16-alpine

# Base de datos para el servicio de órdenes de trabajo
docker run -d \
  --name db-workorder \
  --network telconova-supportsuite-tracking-service_devcontainer_default \
  -e POSTGRES_USER=workorder_user \
  -e POSTGRES_PASSWORD=root1 \
  -e POSTGRES_DB=workorder_db \
  -p 5434:5432 \
  postgres:16-alpine

docker run -d --name ms-tracking   --network=telconova-supportsuite-tracking-service_devcontainer_default   -p 8080:8080   --env-file .env   telconova/ms-tracking:latest

Asi:
```yml
 - job_name: 'Microservicio de Tracking'
    metrics_path: '/actuator/prometheus'
    static_configs:
      - targets: ['ms-tracking:8080']
```

docker run -d --name ms-usuarios   --network=telconova-supportsuite-tracking-service_devcontainer_default   -p 8081:8081   --env-file .env.auth   telconova/ms-usuarios:latest

```yml
 - job_name: 'Microservicio de Usuarios y Autenticación'
    metrics_path: '/actuator/prometheus'
    static_configs:
      - targets: ['ms-usuarios:8081']
```

docker run -d --name ms-workorder   --network=telconova-supportsuite-tracking-service_devcontainer_default   -p 8082:8082   --env-file .env.workorder   telconova/ms-workorder:latest

```yml
 - job_name: 'Microservicio de Ordenes de Trabajo'
    metrics_path: '/actuator/prometheus'
    static_configs:
      - targets: ['ms-workorder:8082']
```