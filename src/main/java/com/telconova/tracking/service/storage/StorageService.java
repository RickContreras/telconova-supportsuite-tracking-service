package com.telconova.tracking.service.storage;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

public interface StorageService {
    String store(MultipartFile file, UUID avanceId) throws IOException;

    Path load(String filename);

    void delete(String filename);

    String getUrl(String filename);
}
