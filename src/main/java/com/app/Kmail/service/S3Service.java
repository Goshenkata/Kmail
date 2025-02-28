package com.app.Kmail.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public interface S3Service {
    String uploadFile(MultipartFile file) throws IOException;

    String extractKeyFromUrl(String attachmentUrl);

    File downloadFile(String key, String prefix, String suffix) throws IOException;
}