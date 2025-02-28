package com.app.Kmail.service.impl;

import com.app.Kmail.service.S3Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class S3ServiceImpl implements S3Service {
    @Value("${aws.bucket}")
    private String bucketName;
    @Value("${aws.region}")
    private String region;

    private final S3Client s3Client;

    public S3ServiceImpl(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        if (file == null) {
            return null;
        }

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String key = "kmail/" + System.currentTimeMillis() + "-" + fileName;

        // Upload the file to S3
        try (InputStream inputStream = file.getInputStream()) {
            s3Client.putObject(PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(key)
                            .build(),
                    RequestBody.fromInputStream(inputStream, file.getSize()));
        }

        // Construct the file URL
        String s3Url = String.format("https://%s.s3.%s.amazonaws.com/%s",
                bucketName,
                region,  // You can either hardcode or retrieve from configuration
                key);

        return s3Url;
    }

    @Override
    public String extractKeyFromUrl(String attachmentUrl) {
        return "";
    }

    @Override
    public File downloadFile(String key, String prefix, String suffix) throws IOException {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        ResponseBytes<GetObjectResponse> s3Object = s3Client.getObjectAsBytes(getObjectRequest);

        File tempFile = File.createTempFile(prefix, suffix);
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(s3Object.asByteArray());
        }
        return tempFile;
    }
}