package com._thproject._thproject_web.oracle.controller;

import com._thproject._thproject_web.oracle.service.ImageStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import jakarta.servlet.http.HttpServletRequest;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

@Tag(name = "DICOM Image Download API", description = "DICOM 이미지 파일 다운로드 및 보기 API")
@RestController
@ConditionalOnProperty(name = "spring.datasource.oracle.enabled", havingValue = "true")
@RequestMapping("/api/images")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8080"}, 
    allowedHeaders = {"Authorization", "Content-Type", "Accept", "X-Requested-With", "Cache-Control"},
    allowCredentials = "true",
    methods = {RequestMethod.GET, RequestMethod.OPTIONS})
public class ImageDownloadController {

    private final ImageStorageService imageStorageService;

    public ImageDownloadController(ImageStorageService imageStorageService) {
        this.imageStorageService = imageStorageService;
    }

    // downloadImageByEncodedPath 메서드도 동일하게 CORS 관련 코드가 없어야 합니다.
    @GetMapping("/encoded-download")
    public ResponseEntity<Resource> downloadImageByEncodedPath(
            @RequestParam String encodedPath) {
        
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(encodedPath);
            String fullPath = new String(decodedBytes, StandardCharsets.UTF_8);

            Path pathObject = Paths.get(fullPath);
            String directoryPath = pathObject.getParent().toString();
            String fileName = pathObject.getFileName().toString();

            Resource resource = imageStorageService.loadImageAsResource(directoryPath, fileName);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"");
            headers.add(HttpHeaders.CONTENT_TYPE, "application/dicom");

            return ResponseEntity.ok().headers(headers).body(resource);
        } catch (Exception e) {
            System.err.println("파일 다운로드 실패: encodedPath=" + encodedPath);
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/encoded-view")
    public ResponseEntity<Resource> viewImageByEncodedPath(
            // HttpServletRequest는 이제 필요 없으므로 제거해도 됩니다.
            @RequestParam String encodedPath) {
        
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(encodedPath);
            String fullPath = new String(decodedBytes, StandardCharsets.UTF_8);

            Path pathObject = Paths.get(fullPath);
            String directoryPath = pathObject.getParent().toString();
            String fileName = pathObject.getFileName().toString();

            Resource resource = imageStorageService.loadImageAsResource(directoryPath, fileName);
            
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"");
            headers.add(HttpHeaders.CONTENT_TYPE, "application/dicom");

            // ★★★ CORS 관련 로직이 완전히 제거되었습니다 ★★★

            return ResponseEntity.ok().headers(headers).body(resource);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }
}