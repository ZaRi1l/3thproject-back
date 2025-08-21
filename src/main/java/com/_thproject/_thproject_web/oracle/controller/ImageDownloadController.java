package com._thproject._thproject_web.oracle.controller;

import com._thproject._thproject_web.oracle.service.ImageStorageService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/images")
public class ImageDownloadController {

    private final ImageStorageService imageStorageService;

    public ImageDownloadController(ImageStorageService imageStorageService) {
        this.imageStorageService = imageStorageService;
    }

    /**
     * 이미지 다운로드 요청을 처리하는 API
     * 예: /api/images/download?path=...&fname=...
     */
    @GetMapping("/download")
    public ResponseEntity<Resource> downloadImage(
            @RequestParam String path,
            @RequestParam String fname) {

        try {
            // 1. 서비스 계층을 호출하여 네트워크 드라이브에서 파일을 Resource 형태로 가져옵니다.
            Resource resource = imageStorageService.loadImageAsResource(path, fname);

            // 2. 클라이언트에게 파일 다운로드임을 알려주기 위한 HTTP 헤더를 설정합니다.
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fname + "\"");
            // DICOM 표준 MIME 타입을 명시합니다. (없어도 동작은 하지만, 명시하는 것이 좋습니다.)
            headers.add(HttpHeaders.CONTENT_TYPE, "application/dicom");

            // 3. 파일 데이터(Resource)와 헤더를 담아 성공(200 OK) 응답을 보냅니다.
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(resource);

        } catch (IOException e) {
            // 파일을 찾지 못했거나 네트워크 연결에 실패한 경우
            // 에러 로그를 남기고, 클라이언트에게는 파일을 찾을 수 없다는 응답(404 Not Found)을 보냅니다.
            System.err.println("파일 다운로드 실패: path=" + path + ", fname=" + fname);
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    // ===================================================================
    // 2. [신규] DICOM 뷰어에서 "파일 보기"를 위한 API
    // ===================================================================
    @GetMapping("/view")
    public ResponseEntity<Resource> viewImage(@RequestParam String path, @RequestParam String fname) {
        try {
            Resource resource = imageStorageService.loadImageAsResource(path, fname);
            HttpHeaders headers = new HttpHeaders();
            // Content-Disposition 헤더를 아예 보내지 않거나, "inline"으로 보냅니다.
            // 이렇게 하면 브라우저는 파일을 다운로드하지 않고, 응답 본문을 그대로 뷰어에게 전달합니다.

            headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fname + "\"");
            
            headers.add(HttpHeaders.CONTENT_TYPE, "application/dicom");

            return ResponseEntity.ok().headers(headers).body(resource);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }
}