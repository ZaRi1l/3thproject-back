package com._thproject._thproject_web.oracle.service;

import com._thproject._thproject_web.oracle.dto.PatientDto;
import com._thproject._thproject_web.oracle.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@ConditionalOnProperty(name = "spring.datasource.oracle.enabled", havingValue = "true")
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;

    /**
     * 환자 ID(pid)를 사용하여 단일 환자 정보를 조회합니다.
     * GraphQL의 'patient(pid: ...)' 쿼리를 처리합니다.
     */
    @Transactional(readOnly = true)
    public PatientDto getPatientByPid(String pid) {
        return patientRepository.findByPid(pid).orElse(null);
    }
}