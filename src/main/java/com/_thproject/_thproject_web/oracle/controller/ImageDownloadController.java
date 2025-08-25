package com._thproject._thproject_web.oracle.controller;

import com._thproject._thproject_web.oracle.service.ImageStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;

// @Tag: 이 컨트롤러의 API들을 "DICOM Image Download API" 라는 이름으로 그룹화합니다.
@Tag(name = "DICOM Image Download API", description = "DICOM 이미지 파일 다운로드 및 보기 API")
@RestController
//@ConditionalOnProperty(name = "spring.datasource.oracle.enabled", havingValue = "true")
@RequestMapping("/api/images")
public class ImageDownloadController {

    private final ImageStorageService imageStorageService;

    public ImageDownloadController(ImageStorageService imageStorageService) {
        this.imageStorageService = imageStorageService;
    }

    // ===================================================================
    // 1. Base64 인코딩된 경로로 파일 다운로드 API
    // ===================================================================
    @Operation(summary = "인코딩된 경로로 이미지 다운로드", description = "Base64로 인코딩된 전체 파일 경로를 받아 디코딩 후, 해당 파일을 다운로드합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공적으로 파일 다운로드", content = @Content(mediaType = "application/dicom")),
        @ApiResponse(responseCode = "404", description = "파일을 찾을 수 없거나 경로가 잘못됨", content = @Content)
    })
    @GetMapping("/encoded-download")
    public ResponseEntity<Resource> downloadImageByEncodedPath(
            HttpServletRequest request,
            @Parameter(name = "encodedPath", description = "Base64로 인코딩된 전체 파일 경로", required = true, example = "RDovZGljb21faW1hZ2VzLzIwMjMvMDEvMTUvaW1nMDAxLmRjbQ==")
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

            // --- [핵심 수정] 여러 출처를 동적으로 처리하는 로직 ---
            
            // 1. 허용할 출처 목록을 정의합니다.
            List<String> allowedOrigins = List.of("http://localhost:3000", "http://localhost:8080");

            // 2. 요청 헤더에서 'Origin' 값을 가져옵니다.
            String origin = request.getHeader("Origin");

            // 3. 요청의 Origin이 허용 목록에 있는지 확인하고, 있다면 응답 헤더에 추가합니다.
            if (origin != null && allowedOrigins.contains(origin)) {
                headers.add("Access-Control-Allow-Origin", origin);
                headers.add("Access-Control-Allow-Credentials", "true");
            }

            return ResponseEntity.ok().headers(headers).body(resource);

        } catch (Exception e) {
            System.err.println("파일 다운로드 실패: encodedPath=" + encodedPath);
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    // ===================================================================
    // 2. Base64 인코딩된 경로로 파일 보기 API
    // ===================================================================
    @Operation(summary = "인코딩된 경로로 이미지 보기", description = "Base64로 인코딩된 전체 파일 경로를 받아 디코딩 후, 해당 파일을 뷰어에서 볼 수 있도록 인라인으로 제공합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공적으로 파일 제공", content = @Content(mediaType = "application/dicom")),
        @ApiResponse(responseCode = "404", description = "파일을 찾을 수 없거나 경로가 잘못됨", content = @Content)
    })
    @GetMapping("/encoded-view")
    public ResponseEntity<Resource> viewImageByEncodedPath(
            HttpServletRequest request,
            @Parameter(name = "encodedPath", description = "Base64로 인코딩된 전체 파일 경로", required = true, example = "RDovZGljb21faW1hZ2VzLzIwMjMvMDEvMTUvaW1nMDAxLmRjbQ==")
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

            // --- [핵심 수정] 여러 출처를 동적으로 처리하는 로직 ---
            
            // 1. 허용할 출처 목록을 정의합니다.
            List<String> allowedOrigins = List.of("http://localhost:3000", "http://localhost:8080");

            // 2. 요청 헤더에서 'Origin' 값을 가져옵니다.
            String origin = request.getHeader("Origin");

            // 3. 요청의 Origin이 허용 목록에 있는지 확인하고, 있다면 응답 헤더에 추가합니다.
            if (origin != null && allowedOrigins.contains(origin)) {
                headers.add("Access-Control-Allow-Origin", origin);
                headers.add("Access-Control-Allow-Credentials", "true");
            }

            return ResponseEntity.ok().headers(headers).body(resource);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }
}