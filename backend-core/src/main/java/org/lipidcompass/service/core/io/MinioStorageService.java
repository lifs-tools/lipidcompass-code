/*
 * Copyright 2022 LIFS.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.lipidcompass.service.core.io;

import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.GetObjectResponse;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.ObjectWriteResponse;
import io.minio.RemoveObjectArgs;
import io.minio.UploadObjectArgs;
import io.minio.errors.MinioException;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.lipidcompass.data.model.submission.FileResource;
import org.lipidcompass.data.model.submission.FileResource.FileType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Nils Hoffmann
 */
@Slf4j
public class MinioStorageService {

    private final MinioClient minioClient;
    private final MinioProperties minioProperties;
    private final Path tmpDir;

    @Autowired
    public MinioStorageService(MinioClient minioClient, MinioProperties minioProperties) {
        this.minioClient = minioClient;
        this.minioProperties = minioProperties;
        this.tmpDir = Paths.get(minioProperties.getTmpDir());
        try {
            Files.createDirectories(tmpDir);
        } catch (IOException ex) {
            log.error("Could not create directory hierarchy {}", minioProperties.getTmpDir());
            log.error("Caused by Exception: ", ex);
        }
    }

    protected Optional<String> getOrCreateBucket(String bucketName) {
        try {
            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
            return Optional.of(bucketName);
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException mex) {
            log.error("Caught exception while trying to access minio!", mex);
            return Optional.empty();
        }
    }

    protected Optional<String> getOrCreateDefaultBucket() {
        return getOrCreateBucket(minioProperties.getDefaultBucketName());
    }

    protected void delete(UUID bucketId, String fileName) {
        Optional<String> defaultBucket = getOrCreateDefaultBucket();
        if (defaultBucket.isPresent()) {
            String bucketIdString = bucketId.toString();
            Optional<String> submissionBucket = getOrCreateBucket(bucketIdString);
            if (submissionBucket.isPresent()) {
                try {
                    String objectName = fileName;
                    minioClient.removeObject(
                            RemoveObjectArgs.builder()
                                    .bucket(bucketId.toString())
                                    .object(objectName)
                                    .object(fileName)
                                    .build()
                    );
                } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException mex) {
                    log.error("Caught exception while trying to access minio!", mex);
                }
            }
        }
    }

    protected Optional<FileResource> store(UUID bucketId, Path filePath, String sha256Hash, Long fileSize, Optional<FileType> fileType) {
        Optional<String> defaultBucket = getOrCreateDefaultBucket();
        if (defaultBucket.isPresent()) {
            String bucketIdString = bucketId.toString();
            Optional<String> submissionBucket = getOrCreateBucket(bucketIdString);
            if (submissionBucket.isPresent()) {
                try {
                    String objectName = filePath.getFileName().toString();
                    String fileName = filePath.toAbsolutePath().toString();
                    ObjectWriteResponse response = minioClient.uploadObject(
                            UploadObjectArgs.builder()
                                    .bucket(bucketId.toString())
                                    .object(objectName)
                                    .filename(fileName)
                                    .build()
                    );
                    return Optional.of(new FileResource(objectName, fileName, sha256Hash, fileSize, fileType.orElse(FileType.BINARY), Collections.emptyList()));
                } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException mex) {
                    log.error("Caught exception while trying to access minio!", mex);
                }
            }
        }
        return Optional.empty();
    }

    public Optional<FileResource> store(MultipartFile file,
            UUID bucketId, FileType fileType) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if (file.isEmpty()) {
                throw new StorageException(
                        "Failed to store empty file " + filename);
            }
            Path sessionPath = buildSessionPath(bucketId);
            Path filePath = buildPathToFile(sessionPath,
                    filename);
            Files.createDirectories(filePath.getParent());
            Files.copy(file.getInputStream(), filePath,
                    StandardCopyOption.REPLACE_EXISTING);
            return store(bucketId, filePath, calculateSha256(filePath).orElse(""), file.getSize(), Optional.ofNullable(fileType));
        } catch (IOException e) {
            throw new StorageException("Failed to store file " + filename, e);
        }
    }

    public void delete(String fileName, UUID sessionId) {
        delete(sessionId, StringUtils.cleanPath(fileName));
    }

    protected Optional<String> calculateSha256(Path p) {
        byte[] buffer = new byte[8192];
        int count;
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(p.toFile()))) {
                while ((count = bis.read(buffer)) > 0) {
                    digest.update(buffer, 0, count);
                }
                byte[] hash = digest.digest();
                return Optional.of(Base64.getEncoder().encodeToString(hash));
            } catch (IOException ex) {
                log.error("Caught IOException while trying to calculate hash from file " + p.toString(), ex);
            }
        } catch (NoSuchAlgorithmException ex) {
            log.error("Could not find algorithm for SHA-256: ", ex);
        }
        return Optional.empty();
    }
    
    public Optional<Resource> loadFile(String fileName, UUID bucketId) {
        return load(fileName, bucketId.toString());
    }
    
    protected Optional<Resource> load(String fileName, String bucketId) {
        Optional<String> defaultBucket = getOrCreateDefaultBucket();
        if (defaultBucket.isPresent()) {
            Optional<String> submissionBucket = getOrCreateBucket(bucketId);
            if (submissionBucket.isPresent()) {
                try {
                    GetObjectResponse gor = minioClient.getObject(GetObjectArgs.builder().bucket(bucketId).object(fileName).build());
                    Path sessionPath = buildSessionPath(bucketId);
                    Path filePath = buildPathToFile(sessionPath, fileName);
                    Files.createDirectories(filePath.getParent());
                    Files.copy(gor, filePath, StandardCopyOption.REPLACE_EXISTING);
                    Resource resource = new UrlResource(filePath.toUri());
                    if (resource.exists() || resource.isReadable()) {
                        return Optional.of(resource);
                    } else {
                        throw new StorageFileNotFoundException(
                        "Could not read file: " + fileName);
                    }
                } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException mex) {
                    log.error("Caught exception while trying to access minio!", mex);
                }
            }
        }
        return Optional.empty();
    }

    public List<FileResource> loadAll(String submissionId) {
        Optional<String> defaultBucket = getOrCreateDefaultBucket();
        if (defaultBucket.isPresent()) {

        }
        return Collections.emptyList();
    }

    private Path buildSessionPath(String sessionId) {
        if (sessionId == null) {
            throw new StorageException(
                    "Cannot store file when sessionId is null!");
        }
        return this.tmpDir.resolve(sessionId);
    }
    
    private Path buildSessionPath(UUID sessionId) {
        return buildSessionPath(sessionId.toString());
    }

    private Path buildPathToFile(Path sessionPath, String filename) {
        checkForbiddenRelativePaths(filename);
        return sessionPath.resolve(filename);
    }

    private void checkForbiddenRelativePaths(String path) {
        if (path.contains("..")) {
            throw new StorageException(
                    "Cannot store file with relative path outside current directory "
                    + path);
        }
    }

    public void delete(FileResource fileResource, UUID storageBucket) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
