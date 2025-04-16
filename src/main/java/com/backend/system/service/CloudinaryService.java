package com.backend.system.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CloudinaryService {
    String upload(MultipartFile file) throws IOException;
    String get(String publicId) throws Exception;
}
