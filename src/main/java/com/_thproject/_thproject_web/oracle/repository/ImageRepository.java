package com._thproject._thproject_web.oracle.repository;
import com._thproject._thproject_web.oracle.dto.DummyEntity;
import com._thproject._thproject_web.oracle.dto.ImageDto;
import com._thproject._thproject_web.oracle.dto.ImageDto.ImagePathDto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface ImageRepository extends JpaRepository<DummyEntity, Long> {
    @Query(value = "SELECT STUDYKEY, SERIESKEY, IMAGEKEY, STUDYINSUID, SERIESINSUID, SOPINSTANCEUID, SOPCLASSUID, STSTORAGEID, PATH, FNAME FROM IMAGETAB WHERE SERIESKEY = :seriesKey AND DELFLAG = 0", nativeQuery = true)
    List<ImageDto> findBySeriesKey(@Param("seriesKey") Long seriesKey);

    @Query(value = "SELECT PATH, FNAME " +
    "FROM IMAGETAB " +
    "WHERE STUDYINSUID = :studyinsuid " +
    "  AND SERIESINSUID = :seriesinsuid " +
    "  AND DELFLAG = 0",
    nativeQuery = true)
    List<ImageDto.ImagePathDto> findImagePaths(@Param("studyinsuid") Long studyinsuid,
                        @Param("seriesinsuid") Long seriesinsuid);


        // [신규] 숫자 KEY로 조회하는 쿼리
    // WHERE 절의 컬럼이 STUDYKEY와 SERIESKEY로 변경되었습니다.
    @Query(value = "SELECT PATH, FNAME " +
                   "FROM IMAGETAB " +
                   "WHERE STUDYKEY = :studyKey " +
                   "  AND SERIESKEY = :seriesKey " +
                   "  AND DELFLAG = 0",
           nativeQuery = true)
    List<ImagePathDto> findImagePathsByKeys(@Param("studyKey") Long studyKey,
                                            @Param("seriesKey") Long seriesKey);
}