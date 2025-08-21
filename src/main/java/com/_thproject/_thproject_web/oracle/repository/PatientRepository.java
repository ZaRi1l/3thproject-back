package com._thproject._thproject_web.oracle.repository;
import com._thproject._thproject_web.oracle.dto.DummyEntity;
import com._thproject._thproject_web.oracle.dto.PatientDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<DummyEntity, Long> {
    @Query(value = "SELECT PID, PNAME, PSEX, PBIRTHDATE FROM PATIENTTAB WHERE PID = :pid", nativeQuery = true)
    Optional<PatientDto> findByPid(@Param("pid") String pid);
}