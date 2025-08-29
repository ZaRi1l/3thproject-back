package com._thproject._thproject_web.oracle.service;

import com._thproject._thproject_web.oracle.dto.StudyDto;
import com._thproject._thproject_web.oracle.repository.StudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@ConditionalOnProperty(name = "spring.datasource.oracle.enabled", havingValue = "true")
@RequiredArgsConstructor
public class StudyService {

    private final StudyRepository studyRepository;

    /**
     * StudyKey를 사용하여 단일 검사(Study) 정보를 조회하는 기능은 그대로 유지됩니다.
     * GraphQL의 'study(studyKey: ...)' 쿼리를 처리합니다.
     */
    @Transactional(readOnly = true) // Oracle DB 트랜잭션
    public StudyDto getStudyByStudyKey(Long studyKey) {
        return studyRepository.findByStudyKey(studyKey).orElse(null);
    }

    /**
     * [수정] 환자 ID(pid)와 날짜 범위(startDate, endDate)를 사용하여 검사 목록을 조회합니다.
     * - 기존의 getStudiesByPid, findStudiesByPid 메소드를 하나로 통합하고 날짜 검색 기능을 추가했습니다.
     * - 이 메소드는 GraphQL의 'Patient { studies }' 필드를 처리할 때 호출됩니다.
     */
    @Transactional(readOnly = true) // Oracle DB 트랜잭션
    public List<StudyDto> findStudiesByPid(String pid, String startDate, String endDate, String modality) {
        // [수정] Repository를 호출할 때 날짜 파라미터를 함께 전달합니다.
        return studyRepository.findStudiesByPid(pid, startDate, endDate, modality);
    }
}