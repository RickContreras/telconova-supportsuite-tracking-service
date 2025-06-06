FROM openjdk:21-jdk-slim

# Instalar dependencias necesarias
RUN apt-get update && export DEBIAN_FRONTEND=noninteractive \
    && apt-get -y install --no-install-recommends \
        postgresql-client \
        git \
        maven \
        sudo \
        curl \
        gnupg \
    && apt-get clean -y && rm -rf /var/lib/apt/lists/*

# Configurar el repositorio de Docker
RUN mkdir -p /etc/apt/keyrings \
    && curl -fsSL https://download.docker.com/linux/ubuntu/gpg | gpg --dearmor -o /etc/apt/keyrings/docker.gpg \
    && echo "deb [arch=amd64 signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu jammy stable" > /etc/apt/sources.list.d/docker.list \
    && apt-get update

# Instalar Docker y sus componentes
RUN apt-get install -y docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin \
    && apt-get clean -y && rm -rf /var/lib/apt/lists/*

# Crear el usuario vscode con permisos adecuados
RUN useradd -ms /bin/bash vscode \
    && mkdir -p /workspace \
    && chown -R vscode:vscode /workspace \
    && usermod -aG sudo vscode \
    && usermod -aG docker vscode \
    && echo "vscode ALL=(ALL) NOPASSWD:ALL" >> /etc/sudoers

# Establecer el usuario vscode como predeterminado
USER vscode

# Establecer el directorio de trabajo
WORKDIR /workspace