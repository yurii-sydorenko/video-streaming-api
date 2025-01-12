package com.videostreaming.service.impl;

import com.videostreaming.model.exception.StorageException;
import com.videostreaming.service.VideoStorage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Service responsible for working with a local video storage.
 * @author Yurii Sydorenko
 */
@Service
@ConditionalOnProperty(value = "video-streaming.storage.type", havingValue = "local")
public class LocalVideoStorage implements VideoStorage {

    private final String storagePath;

    public LocalVideoStorage(@Value("${video-streaming.storage.local.path}") String storagePath) throws IOException {
        Files.createDirectories(Paths.get(storagePath));
        this.storagePath = storagePath;
    }

    @Override
    public void save(MultipartFile file) {
        String destination = storagePath + file.getOriginalFilename();

        try (FileOutputStream fileOutputStream = new FileOutputStream(destination)) {
            fileOutputStream.write(file.getBytes());
        } catch (Exception e) {
            throw new StorageException(e.getMessage());
        }
    }

    @Override
    public FileSystemResource get(String fileName) {
        return new FileSystemResource(storagePath + fileName);
    }

}
