package com._thproject._thproject_web.oracle.repository;
import com._thproject._thproject_web.oracle.dto.DummyEntity;
import com._thproject._thproject_web.oracle.dto.PatientDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<DummyEntity, Long> {
    @Query(value = "SELECT PID, PNAME, PSEX, PBIRTHDATE FROM PATIENTTAB WHERE PID = :pid", nativeQuery = true)
    Optional<PatientDto> findByPid(@Param("pid") String pid);
    // [추가] PID와 PNAME으로 부분 일치 검색 (대소문자 무시)
    @Query(value = "SELECT PID, PNAME, PSEX, PBIRTHDATE FROM PATIENTTAB " +
            "WHERE (:pid IS NULL OR UPPER(PID) LIKE '%' || UPPER(:pid) || '%') " +
            "  AND (:pname IS NULL OR UPPER(PNAME) LIKE '%' || UPPER(:pname) || '%')",
            nativeQuery = true)
    List<PatientDto> searchByPidAndPname(@Param("pid") String pid, @Param("pname") String pname);
}