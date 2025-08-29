package com._thproject._thproject_web.oracle.repository;
import com._thproject._thproject_web.oracle.dto.DummyEntity;
import com._thproject._thproject_web.oracle.dto.StudyDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudyRepository extends JpaRepository<DummyEntity, Long> {
    @Query(value = "SELECT STUDYKEY, STUDYINSUID, STUDYDATE, STUDYTIME, ACCESSNUM, STUDYID, STUDYDESC, MODALITY, BODYPART, PID, PNAME, PSEX, PBIRTHDATETIME, PATAGE, SERIESCNT, IMAGECNT FROM STUDYTAB WHERE STUDYKEY = :studyKey AND DELFLAG = 0", nativeQuery = true)
    Optional<StudyDto> findByStudyKey(@Param("studyKey") Long studyKey);

    // [수정] 날짜 범위 검색 기능 추가
    @Query(value = "SELECT STUDYKEY, STUDYINSUID, STUDYDATE, STUDYTIME, ACCESSNUM, STUDYID, STUDYDESC, MODALITY, " +
            "BODYPART, PID, PNAME, PSEX, PBIRTHDATETIME, PATAGE, SERIESCNT, IMAGECNT FROM STUDYTAB " +
            "WHERE PID = :pid AND DELFLAG = 0 " +
            "  AND (:startDate IS NULL OR STUDYDATE >= :startDate) " +
            "  AND (:endDate IS NULL OR STUDYDATE <= :endDate) " +
            "  AND (:modality = 'ALL' OR MODALITY = :modality) " +
            "ORDER BY STUDYDATE DESC, STUDYTIME DESC", nativeQuery = true)
    List<StudyDto> findStudiesByPid(@Param("pid") String pid,
                                    @Param("startDate") String startDate,
                                    @Param("endDate") String endDate,
                                    @Param("modality") String modality);


}