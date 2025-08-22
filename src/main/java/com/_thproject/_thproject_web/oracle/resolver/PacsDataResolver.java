package com._thproject._thproject_web.oracle.resolver;

// Oracle 패키지의 DTO 및 서비스 import
import com._thproject._thproject_web.oracle.dto.*;
import com._thproject._thproject_web.oracle.service.*;

// PostgreSQL 패키지의 Report 관련 클래스 import
import com._thproject._thproject_web.postgresql.entity.Report;
import com._thproject._thproject_web.postgresql.service.ReportService;

import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class PacsDataResolver {

    // === Oracle DB 관련 서비스 주입 ===
    private final PatientService patientService;
    private final StudyService studyService; // 방금 만든 StudyService
    private final SeriesService seriesService;
    private final ImageService imageService;

    // === PostgreSQL DB 관련 서비스 주입 ===
    private final ReportService reportService;

    // ===================================================================
    // 최상위 Query 처리 (API의 시작점)
    // ===================================================================

    @QueryMapping
    public PatientDto patient(@Argument String pid) {
        return patientService.getPatientByPid(pid);
    }

    @QueryMapping
    public StudyDto study(@Argument Long studyKey) {
        return studyService.getStudyByStudyKey(studyKey);
    }

    // SeriesRepository에 findByStudyKeyAndSeriesKey가 있으므로, SeriesService에 해당 메소드를 추가해야 합니다.
    // public SeriesDto findByStudyKeyAndSeriesKey(Long studyKey, Long seriesKey) {
    //     return seriesRepository.findByStudyKeyAndSeriesKey(studyKey, seriesKey).orElse(null);
    // }
    @QueryMapping
    public SeriesDto series(@Argument Long studyKey, @Argument Long seriesKey) {
        return seriesService.findByStudyKeyAndSeriesKey(studyKey, seriesKey);
    }

    // ===================================================================
    // 중첩 필드(Nested Field) 처리 (GraphQL의 핵심)
    // ===================================================================

    // Patient 타입의 'studies' 필드가 요청될 때
    @SchemaMapping(typeName = "Patient", field = "studies")
    public List<StudyDto> getStudiesForPatient(PatientDto patient) {
        return studyService.getStudiesByPid(patient.getPid());
    }

    // Study 타입의 'series' 필드가 요청될 때
    @SchemaMapping(typeName = "Study", field = "series")
    public List<SeriesDto> getSeriesForStudy(StudyDto study) {
        return seriesService.findByStudyKey(study.getStudyKey());
    }

    // Study 타입의 'patient' 필드가 요청될 때 (역참조)
    @SchemaMapping(typeName = "Study", field = "patient")
    public PatientDto getPatientForStudy(StudyDto study) {
        return patientService.getPatientByPid(study.getPid());
    }

    // Series 타입의 'images' 필드가 요청될 때
    @SchemaMapping(typeName = "Series", field = "images")
    public List<ImageDto> getImagesForSeries(SeriesDto series) {
        return imageService.findBySeriesKey(series.getSeriesKey());
    }

    // Series 타입의 'study' 필드가 요청될 때 (역참조)
    @SchemaMapping(typeName = "Series", field = "study")
    public StudyDto getStudyForSeries(SeriesDto series) {
        return studyService.getStudyByStudyKey(series.getStudyKey());
    }

    // Image 타입의 'series' 필드가 요청될 때 (역참조)
    @SchemaMapping(typeName = "Image", field = "series")
    public SeriesDto getSeriesForImage(ImageDto image) {
        return seriesService.findBySeriesKey(image.getSeriesKey());
    }

    // *** [핵심] 서로 다른 DB의 데이터를 연결하는 부분 ***
    // Study 타입의 'report' 필드가 요청될 때
    @SchemaMapping(typeName = "Study", field = "report")
    public Report getReportForStudy(StudyDto study) {
        // Oracle DB에서 조회한 StudyDto의 studyKey를 사용하여
        // PostgreSQL DB의 ReportService를 호출합니다.
        return reportService.getReportByStudyKey(study.getStudyKey());
    }
}