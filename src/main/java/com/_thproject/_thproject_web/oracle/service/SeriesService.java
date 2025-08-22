package com._thproject._thproject_web.oracle.service;

import com._thproject._thproject_web.oracle.dto.SeriesDto;
import com._thproject._thproject_web.oracle.repository.SeriesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SeriesService {

    private final SeriesRepository seriesRepository;

    /**
     * 검사 키(studyKey)를 사용하여 해당 검사에 속한 모든 시리즈 목록을 조회합니다.
     * GraphQL의 'Study { series }' 필드를 처리합니다.
     */
    @Transactional(readOnly = true)
    public List<SeriesDto> findByStudyKey(Long studyKey) {
        return seriesRepository.findByStudyKey(studyKey);
    }

    /**
     * 시리즈 키(seriesKey)를 사용하여 단일 시리즈 정보를 조회합니다.
     * 리졸버의 역참조('Image { series }')에서 사용됩니다.
     */
    @Transactional(readOnly = true)
    public SeriesDto findBySeriesKey(Long seriesKey) {
        return seriesRepository.findBySeriesKey(seriesKey).orElse(null);
    }

    /**
     * 검사 키(studyKey)와 시리즈 키(seriesKey)를 모두 사용하여 단일 시리즈를 조회합니다.
     * GraphQL의 'series(studyKey: ..., seriesKey: ...)' 쿼리를 처리합니다.
     */
    @Transactional(readOnly = true)
    public SeriesDto findByStudyKeyAndSeriesKey(Long studyKey, Long seriesKey) {
        return seriesRepository.findByStudyKeyAndSeriesKey(studyKey, seriesKey).orElse(null);
    }
}