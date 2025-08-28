package com._thproject._thproject_web.oracle.service;

import com._thproject._thproject_web.oracle.dto.PatientDto;
import com._thproject._thproject_web.oracle.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@ConditionalOnProperty(name = "spring.datasource.oracle.enabled", havingValue = "true")
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    // ... (getPatientByPid 메소드는 그대로 둡니다)
    @Transactional(readOnly = true)
    public PatientDto getPatientByPid(String pid) {
        return patientRepository.findByPid(pid).orElse(null);
    }

    /**
     * [수정] 부분 검색을 위한 서비스 메소드
     * - 입력값이 빈 문자열("")일 경우, DB 쿼리의 'IS NULL' 조건이 잘 동작하도록 null로 변환해줍니다.
     * - 이것이 한 필드만으로도 검색이 가능하게 하는 핵심 로직입니다.
     */
    @Transactional(readOnly = true)
    public List<PatientDto> searchPatients(String pid, String pname) {
        // pid가 비어있으면 null로, 아니면 그대로 사용
        String searchPid = (pid != null && !pid.trim().isEmpty()) ? pid : null;
        // pname이 비어있으면 null로, 아니면 그대로 사용
        String searchPname = (pname != null && !pname.trim().isEmpty()) ? pname : null;

        // 두 파라미터가 모두 null이면 (즉, 입력이 없으면) 검색하지 않고 빈 리스트 반환
        if (searchPid == null && searchPname == null) {
            return List.of();
        }

        return patientRepository.searchByPidAndPname(searchPid, searchPname);
    }
}