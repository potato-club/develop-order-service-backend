package com.server.dos.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.UUID;

@Transactional
@Service
@RequiredArgsConstructor
@Slf4j
public class S3Service {
    private final AmazonS3Client amazonS3Client;

    @Value("${s3.bucket}")
    private String bucket;

    public String upload(MultipartFile multipartFile, String dir) {
        if(multipartFile.isEmpty()) {
            throw new IllegalArgumentException("File is Empty");
        }
        String fileName = multipartFile.getOriginalFilename();
        long size = multipartFile.getSize();

        String uuid = UUID.randomUUID().toString();
        String fileNameWithUUID = uuid + fileName;
        String filePath = dir + "/" + fileNameWithUUID;

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());
        objectMetadata.setContentLength(size);

        try {
            amazonS3Client.putObject(new PutObjectRequest(bucket, filePath, multipartFile.getInputStream(), objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            throw new RuntimeException("File Stream Error");
        }

        return amazonS3Client.getUrl(bucket, filePath).toString();
    }

//    public ResponseEntity<byte[]> download(String filePath) throws IOException {
//        S3Object s3Object = amazonS3Client.getObject(new GetObjectRequest(bucket, filePath));
//        S3ObjectInputStream objectInputStream = s3Object.getObjectContent();
//        byte[] bytes = IOUtils.toByteArray(objectInputStream);
//
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.setContentType(contentType(filePath));
//        httpHeaders.setContentLength(bytes.length);
//
//        String fileName = getFileNameFromPath(filePath);
//        String name = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
//        httpHeaders.setContentDispositionFormData("attachment", name);
//
//        return new ResponseEntity<>(bytes, httpHeaders, HttpStatus.OK);
//    }
//
//    private MediaType contentType(String key) {
//        String[] arr = key.split("\\.");
//        String type = arr[arr.length - 1];
//        switch (type) {
//            case "txt":
//                return MediaType.TEXT_PLAIN;
//            case "png":
//                return MediaType.IMAGE_PNG;
//            case "jpg":
//                return MediaType.IMAGE_JPEG;
//            default:
//                return MediaType.APPLICATION_OCTET_STREAM;
//        }
//    }

    public void delete(String filePath) {
        try {
            amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, filePath));
        } catch (AmazonServiceException e) {
            throw new RuntimeException("delete failed");
        }
    }

//    private String getFileNameFromPath(String filePath) {
//        int lastIndex = filePath.lastIndexOf('/');
//        if (lastIndex != -1 && lastIndex < filePath.length() - 1) {
//            return filePath.substring(lastIndex + 1);
//        }
//        return filePath;
//    }
}
