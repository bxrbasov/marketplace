package com.spring.marketplace.handler.service;

import com.spring.marketplace.utils.exception.ApplicationException;
import com.spring.marketplace.utils.exception.ErrorType;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.InputStream;
import java.nio.file.StandardCopyOption;

@Slf4j
@Service
public class FileStorageService {

    @Value("${app.download-upload.path}")
    private String basePath;

    @SneakyThrows
    public void uploadFile(MultipartFile file){

        try(InputStream inputStream = file.getInputStream()){
            Path path = Path.of(basePath);
            if(!Files.exists(path)){
                Files.createDirectories(path);
            }

            Path fullPathToFile = Path.of(basePath + file.getOriginalFilename());
            Files.copy(inputStream,fullPathToFile, StandardCopyOption.REPLACE_EXISTING);
            log.info("File saved to {}", fullPathToFile);
        }
        catch (Exception ex){
            log.error(ex.getMessage());
            throw new ApplicationException(ErrorType.FAILED_TO_UPLOAD_FILE);
        }
    }

    @SneakyThrows
    public Resource downloadFile(String fileName) {
        try{
            InputStreamResource resource = new InputStreamResource(Files.newInputStream(Path.of(basePath + fileName)));
            log.info("File downloaded to {}", resource.getFilename());
            return resource;
        }
        catch (Exception ex){
            log.error(ex.getMessage());
            throw new ApplicationException(ErrorType.FAILED_TO_DOWNLOAD_FILE);
        }
    }

}
