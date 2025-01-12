package com.videostreaming.service.impl;

import com.videostreaming.service.VideoStorage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service responsible for working with a cloud storage.
 * @author Yurii Sydorenko
 */
@Service
@ConditionalOnProperty(value = "video-streaming.storage.type", havingValue = "cloud")
public class CloudVideoStorage implements VideoStorage {

    @Override
    public void save(MultipartFile file) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public FileSystemResource get(String fileName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
