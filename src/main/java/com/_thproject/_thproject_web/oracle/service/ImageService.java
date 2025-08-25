package com._thproject._thproject_web.oracle.service;

import com._thproject._thproject_web.oracle.dto.DicomResponseDto;
import com._thproject._thproject_web.oracle.dto.ImageDto;
import com._thproject._thproject_web.oracle.dto.ImageDto.ImagePathDto;
import com._thproject._thproject_web.oracle.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
@ConditionalOnProperty(name = "spring.datasource.oracle.enabled", havingValue = "true")
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;

    /**
     * 시리즈 키(seriesKey)를 사용하여 해당 시리즈에 속한 모든 이미지 목록을 조회합니다.
     * GraphQL의 'Series { images }' 필드를 처리합니다.
     */
    @Transactional(readOnly = true)
    public List<ImageDto> findBySeriesKey(Long seriesKey) {
        return imageRepository.findBySeriesKey(seriesKey);
    }


    public DicomResponseDto getEncodedImagePaths(Long studyinstanceuid, Long seriesinstanceuid) {
        
        // 1. DB에서 해당 시리즈의 모든 이미지 경로를 조회합니다.
        List<ImageDto.ImagePathDto> imagePaths = imageRepository.findImagePaths(studyinstanceuid, seriesinstanceuid);

        List<String> base64EncodedPaths = new ArrayList<>();

        // 2. 조회된 각 경로(문자열)를 Base64로 인코딩합니다.
        for (ImageDto.ImagePathDto imagePath : imagePaths) {
            // 예: "D:/dicom_images/2023/01/15/img001.dcm"
            String fullPath = imagePath.getPath() + "/" + imagePath.getFname();
            
            // 문자열을 UTF-8 바이트 배열로 변환합니다.
            byte[] pathBytes = fullPath.getBytes(StandardCharsets.UTF_8);
            
            // 바이트 배열을 Base64 문자열로 인코딩합니다.
            String encodedPath = Base64.getEncoder().encodeToString(pathBytes);
            
            base64EncodedPaths.add(encodedPath);
        }

        // 3. 최종 응답 DTO를 생성하고 값을 채웁니다.
        DicomResponseDto responseDto = new DicomResponseDto();
        responseDto.setImageCnt(base64EncodedPaths.size());
        responseDto.setData(base64EncodedPaths.toArray(new String[0]));

        return responseDto;
    }


    // [신규] 숫자 Key를 파라미터로 받는 메서드
    public DicomResponseDto getEncodedImagePathsByKeys(Long studyKey, Long seriesKey) {
        // [신규] 숫자 Key로 조회하는 리포지토리 메서드를 호출
        List<ImagePathDto> imagePaths = imageRepository.findImagePathsByKeys(studyKey, seriesKey);

        // 아래 인코딩 로직은 동일합니다.
        List<String> base64EncodedPaths = new ArrayList<>();
        for (ImagePathDto imagePath : imagePaths) {
            String fullPath = imagePath.getPath() + "/" + imagePath.getFname();
            byte[] pathBytes = fullPath.getBytes(StandardCharsets.UTF_8);
            String encodedPath = Base64.getEncoder().encodeToString(pathBytes);
            base64EncodedPaths.add(encodedPath);
        }

        DicomResponseDto responseDto = new DicomResponseDto();
        responseDto.setImageCnt(base64EncodedPaths.size());
        responseDto.setData(base64EncodedPaths.toArray(new String[0]));

        return responseDto;
    }
}