package com._thproject._thproject_web.oracle.controller;

import com._thproject._thproject_web.oracle.dto.DicomResponseDto;
import com._thproject._thproject_web.oracle.service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// @Tag: 이 컨트롤러에 속한 API들을 그룹화합니다.
@Tag(name = "DICOM Data API", description = "Oracle DB의 DICOM 관련 데이터 조회 REST API")
@RestController
@RequestMapping("/api/v1")
public class DicomRestController {

    private final ImageService imageService;

    public DicomRestController(ImageService imageService) {
        this.imageService = imageService;
    }

    // [변경점] URL 경로, 메서드 이름, 파라미터 이름 및 타입을 모두 숫자 Key 기준으로 변경!
    @Operation(summary = "이미지 경로 목록 조회 (By Key)", description = "StudyKey와 SeriesKey(숫자 ID)를 사용하여 해당하는 모든 이미지의 전체 경로를 Base64로 인코딩하여 반환합니다.")
    @ApiResponses(value = { /* ... 기존과 동일 ... */ })
    @GetMapping("/studies/keys/{studyKey}/{seriesKey}")
    public ResponseEntity<DicomResponseDto> getSeriesImagesByKeys(
            @Parameter(name = "studyKey", description = "조회할 Study의 숫자 Key", required = true, example = "20230115085350020")
            @PathVariable("studyKey") Long studyKey,

            @Parameter(name = "seriesKey", description = "조회할 Series의 숫자 Key", required = true, example = "20230115085351461")
            @PathVariable("seriesKey") Long seriesKey) {
        
        // [변경점] 새로 만들 서비스 메서드를 호출합니다.
        DicomResponseDto responseDto = imageService.getEncodedImagePathsByKeys(studyKey, seriesKey);
        
        return ResponseEntity.ok(responseDto);
    }
}