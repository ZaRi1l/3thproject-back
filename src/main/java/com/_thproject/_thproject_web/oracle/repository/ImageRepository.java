package com._thproject._thproject_web.oracle.repository;
import com._thproject._thproject_web.oracle.dto.DummyEntity;
import com._thproject._thproject_web.oracle.dto.ImageDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface ImageRepository extends JpaRepository<DummyEntity, Long> {
    @Query(value = "SELECT STUDYKEY, SERIESKEY, IMAGEKEY, STUDYINSUID, SERIESINSUID, SOPINSTANCEUID, SOPCLASSUID, STSTORAGEID, PATH, FNAME FROM IMAGETAB WHERE SERIESKEY = :seriesKey AND DELFLAG = 0", nativeQuery = true)
    List<ImageDto> findBySeriesKey(@Param("seriesKey") Long seriesKey);
}