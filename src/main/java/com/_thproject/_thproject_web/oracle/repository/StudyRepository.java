package com._thproject._thproject_web.oracle.repository;

import com._thproject._thproject_web.oracle.dto.DummyEntity;
import com._thproject._thproject_web.oracle.dto.StudyDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudyRepository extends JpaRepository<DummyEntity, Long> {

    /**
     * studyKey를 이용해 StudyTab에서 하나의 검사 정보를 조회합니다.
     * SELECT 절에 나열된 컬럼의 순서가 StudyDto 생성자의 파라미터 순서와 정확히 일치해야 합니다.
     * @param studyKey 조회할 검사의 Primary Key
     * @return 조회된 검사 정보를 담은 StudyDto 객체 (없으면 Optional.empty())
     */
    @Query(value = "SELECT " +
                   "    STUDYKEY, STUDYINSUID, STUDYDATE, STUDYTIME, " +
                   "    ACCESSNUM, STUDYID, STUDYDESC, MODALITY, " +
                   "    BODYPART, PID, PNAME, PSEX, " +
                   "    PBIRTHDATETIME, PATAGE, SERIESCNT, IMAGECNT " +
                   "FROM STUDYTAB " +
                   "WHERE STUDYKEY = :studyKey AND DELFLAG = 0", // 삭제되지 않은 데이터만 조회
           nativeQuery = true)
    Optional<StudyDto> findByStudyKey(@Param("studyKey") Long studyKey);
}