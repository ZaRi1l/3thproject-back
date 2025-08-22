package com._thproject._thproject_web.oracle.service;

import com._thproject._thproject_web.oracle.dto.ImageDto;
import com._thproject._thproject_web.oracle.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}