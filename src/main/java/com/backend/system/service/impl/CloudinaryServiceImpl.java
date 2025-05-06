package com.backend.system.service.impl;

import com.backend.system.service.CloudinaryService;
import com.cloudinary.Cloudinary;
import com.cloudinary.api.ApiResponse;
import com.cloudinary.utils.ObjectUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;


@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CloudinaryServiceImpl implements CloudinaryService {

    Cloudinary cloudinary;

    @Override
    public String uploadMultipartFile(MultipartFile file) throws IOException {
        UUID uuid = UUID.randomUUID();
        Map params = ObjectUtils.asMap(
                "public_id", uuid.toString(),
                "use_filename", true,
                "unique_filename", false,
                "overwrite", true
        );

        Map result = cloudinary.uploader().upload(file.getBytes(), params);
        return (String) result.get("url");
    }

    @Override
    public String uploadFile(File file) throws IOException {
        return "";
    }

    @Override
    public String uploadBytes(byte[] file) throws IOException {
        UUID uuid = UUID.randomUUID();
        Map params = ObjectUtils.asMap(
                "public_id", uuid.toString(),
                "use_filename", true,
                "unique_filename", false,
                "overwrite", true
        );
        Map result = cloudinary.uploader().upload(file, params);
        return (String) result.get("url");
    }

    @Override
    public String get(String publicId) throws Exception {
        ApiResponse apiResponse = cloudinary.api().resource(publicId, ObjectUtils.emptyMap());
        return (String) apiResponse.get("url");
    }
}
