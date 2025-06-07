FROM openjdk:21-slim-bullseye

# Variables de entorno esenciales
ENV DEBIAN_FRONTEND=noninteractive \
    LANG=C.UTF-8 \
    JAVA_HOME=/usr/local/openjdk-21

# Instalación de dependencias esenciales
RUN apt-get update && apt-get -y install --no-install-recommends \
    postgresql-client \
    git \
    maven \
    sudo \
    curl \
    ca-certificates \
    zip \
    unzip \
    htop \
    redis-tools \
    redis-server \
    # Limpieza
    && apt-get clean -y \
    && rm -rf /var/lib/apt/lists/* \
    # Configuración de usuario
    && useradd -ms /bin/bash vscode \
    && mkdir -p /workspace /home/vscode/.m2 \
    && chown -R vscode:vscode /workspace /home/vscode \
    && echo "vscode ALL=(ALL) NOPASSWD:ALL" >> /etc/sudoers

USER vscode
WORKDIR /workspace