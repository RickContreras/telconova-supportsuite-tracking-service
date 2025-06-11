package com.telconova.tracking.service.storage.impl;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;
import com.azure.storage.blob.models.BlobHttpHeaders;
import com.telconova.tracking.config.StorageConfig.StorageProperties;
import com.telconova.tracking.service.storage.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@ConditionalOnProperty(name = "app.storage.type", havingValue = "azure")
public class AzureBlobStorageService implements StorageService {

    private static final Logger logger = LoggerFactory.getLogger(AzureBlobStorageService.class);
    private final BlobContainerClient containerClient;
    // private final StorageProperties storageProperties;

    public AzureBlobStorageService(StorageProperties storageProperties) {
        // this.storageProperties = storageProperties;

        logger.info("Inicializando Azure Blob Storage con contenedor: {}",
                storageProperties.getAzure().getContainerName());

        // Crear el cliente del contenedor de Azure Blob Storage
        this.containerClient = new BlobContainerClientBuilder()
                .connectionString(storageProperties.getAzure().getConnectionString())
                .containerName(storageProperties.getAzure().getContainerName()).buildClient();
    }

    @Override
    public String store(MultipartFile file, UUID avanceId) throws IOException {
        // Validar el archivo
        if (file.isEmpty()) {
            throw new IllegalArgumentException("No se puede almacenar un archivo vacío");
        }

        // Generar un nombre único para el archivo usando timestamp y UUID
        String timestamp =
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename != null
                ? originalFilename.substring(originalFilename.lastIndexOf("."))
                : "";
        String blobName =
                String.format("%s/%s_%s%s", avanceId, timestamp, UUID.randomUUID(), extension);

        logger.debug("Almacenando archivo en Azure Blob Storage: {}", blobName);

        // Obtener referencia al blob
        BlobClient blobClient = containerClient.getBlobClient(blobName);

        // Establecer los headers HTTP del blob
        BlobHttpHeaders headers = new BlobHttpHeaders().setContentType(file.getContentType());

        // Subir el archivo
        blobClient.upload(file.getInputStream(), file.getSize(), true);
        blobClient.setHttpHeaders(headers);

        logger.info("Archivo almacenado correctamente en Azure: {}", blobName);

        // Devolver la ruta donde se almacenó
        return blobName;
    }

    @Override
    public Path load(String filename) {
        // No es realmente utilizado, pero podemos simular una ruta local
        return Paths.get(filename);
    }

    @Override
    public void delete(String filename) {
        // Obtener referencia al blob
        BlobClient blobClient = containerClient.getBlobClient(filename);

        // Eliminar el blob
        blobClient.deleteIfExists();

        logger.info("Archivo eliminado de Azure: {}", filename);
    }

    @Override
    public String getUrl(String filename) {
        // Obtener referencia al blob
        BlobClient blobClient = containerClient.getBlobClient(filename);

        // Devolver la URL
        return blobClient.getBlobUrl();
    }
}
