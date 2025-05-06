package com.backend.system.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public interface CloudinaryService {
    String uploadMultipartFile(MultipartFile file) throws IOException;
    String uploadFile(File file) throws IOException;
    String uploadBytes(byte[] file) throws IOException;
    String get(String publicId) throws Exception;
}
