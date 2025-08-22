package com._thproject._thproject_web.oracle.service;

import com._thproject._thproject_web.oracle.dto.StudyDto;
import com._thproject._thproject_web.oracle.repository.StudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudyService {

    private final StudyRepository studyRepository;

    /**
     * StudyKey를 사용하여 단일 검사(Study) 정보를 조회합니다.
     * GraphQL의 'study(studyKey: ...)' 쿼리를 처리합니다.
     */
    @Transactional(readOnly = true) // Oracle DB 트랜잭션
    public StudyDto getStudyByStudyKey(Long studyKey) {
        return studyRepository.findByStudyKey(studyKey).orElse(null);
    }

    /**
     * 환자 ID(pid)를 사용하여 해당 환자의 모든 검사 목록을 조회합니다.
     * GraphQL의 'Patient { studies }' 필드를 처리합니다.
     */
    @Transactional(readOnly = true) // Oracle DB 트랜잭션
    public List<StudyDto> getStudiesByPid(String pid) {
        return studyRepository.findStudiesByPid(pid);
    }
}