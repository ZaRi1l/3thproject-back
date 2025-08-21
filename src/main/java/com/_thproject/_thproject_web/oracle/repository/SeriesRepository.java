package com._thproject._thproject_web.oracle.repository;
import com._thproject._thproject_web.oracle.dto.DummyEntity;
import com._thproject._thproject_web.oracle.dto.SeriesDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SeriesRepository extends JpaRepository<DummyEntity, Long> {
    @Query(value = "SELECT STUDYKEY, SERIESKEY, STUDYINSUID, SERIESINSUID, SERIESNUM, MODALITY, SERIESDATE, SERIESTIME, BODYPART, SERIESDESC, IMAGECNT FROM SERIESTAB WHERE STUDYKEY = :studyKey AND DELFLAG = 0 ORDER BY SERIESNUM ASC", nativeQuery = true)
    List<SeriesDto> findByStudyKey(@Param("studyKey") Long studyKey);
    
    @Query(value = "SELECT STUDYKEY, SERIESKEY, STUDYINSUID, SERIESINSUID, SERIESNUM, MODALITY, SERIESDATE, SERIESTIME, BODYPART, SERIESDESC, IMAGECNT FROM SERIESTAB WHERE SERIESKEY = :seriesKey AND DELFLAG = 0", nativeQuery = true)
    Optional<SeriesDto> findBySeriesKey(@Param("seriesKey") Long seriesKey);
    
    @Query(value = "SELECT STUDYKEY, SERIESKEY, STUDYINSUID, SERIESINSUID, SERIESNUM, MODALITY, SERIESDATE, SERIESTIME, BODYPART, SERIESDESC, IMAGECNT FROM SERIESTAB WHERE STUDYKEY = :studyKey AND SERIESKEY = :seriesKey AND DELFLAG = 0", nativeQuery = true)
    Optional<SeriesDto> findByStudyKeyAndSeriesKey(@Param("studyKey") Long studyKey, @Param("seriesKey") Long seriesKey);
}