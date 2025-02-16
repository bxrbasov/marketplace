package com.spring.marketplace.http.rest;

import com.spring.marketplace.handler.service.FileStorageService;
import com.spring.marketplace.handler.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/documents")
public class DocumentController {

    private final ReportService reportService;
    private final FileStorageService fileStorageService;

    @GetMapping
    public ResponseEntity<List<String>> getReportFilesName() {
        return ResponseEntity.ok().body(reportService.getReportFilesName());
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestBody MultipartFile file) {
        fileStorageService.uploadFile(file);
        return ResponseEntity.ok("File was upload successfully");
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(@RequestParam(name = "fileName") String fileName) {
        Resource fileToDownload = fileStorageService.downloadFile(fileName);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(fileToDownload);
    }
}
