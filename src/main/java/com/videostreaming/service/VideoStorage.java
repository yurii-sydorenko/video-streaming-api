package com.videostreaming.service;

import org.springframework.core.io.FileSystemResource;
import org.springframework.web.multipart.MultipartFile;

public interface VideoStorage {

    void save(MultipartFile file);

    FileSystemResource get(String fileName);

}
