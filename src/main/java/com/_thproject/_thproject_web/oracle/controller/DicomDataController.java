package com._thproject._thproject_web.oracle.controller;

import com._thproject._thproject_web.oracle.dto.*;
import com._thproject._thproject_web.oracle.repository.*;
import com._thproject._thproject_web.oracle.service.PatientService;
import com._thproject._thproject_web.oracle.service.SeriesService;
import com._thproject._thproject_web.oracle.service.StudyService;
import com._thproject._thproject_web.oracle.service.ImageService;
// [추가] PostgreSQL의 Report 엔티티와 ReportService를 import 합니다.
import com._thproject._thproject_web.postgresql.entity.Report;
import com._thproject._thproject_web.postgresql.service.ReportService;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import com._thproject._thproject_web.postgresql.dto.ReportResponseDto;

import java.util.List;

@Controller
//@ConditionalOnProperty(name = "spring.datasource.oracle.enabled", havingValue = "true")
public class DicomDataController {

    // --- [추가] Oracle Service ---
    private final PatientService patientService;
    private final StudyService studyService;
    private final SeriesService seriesService;
    private final ImageService imageService;
    // --- [추가] PostgreSQL Service ---
    private final ReportService reportService;

    // [수정] 생성자에 ReportService 주입을 추가합니다.
    public DicomDataController(PatientService patientService, StudyService studyService,
                               SeriesService seriesService, ImageService imageService,
                               ReportService reportService) {
        this.patientService = patientService;
        this.studyService = studyService;
        this.seriesService = seriesService;
        this.imageService = imageService;
        this.reportService = reportService;
    }

    // --- 최상위 Query Resolver (기존 코드) ---

    @QueryMapping
    public PatientDto patient(@Argument String pid) {
        return patientService.getPatientByPid(pid);
    }

    @QueryMapping
    public StudyDto study(@Argument Long studyKey) {
        return studyService.getStudyByStudyKey(studyKey);
    }

    @QueryMapping
    public SeriesDto series(@Argument Long studyKey, @Argument Long seriesKey) {
        return seriesService.getSeriesByStudyKeyAndSeriesKey(studyKey, seriesKey);
    }

    // --- 관계 필드 Resolver (SchemaMapping) (기존 코드) ---
    // 이 부분의 코드를 /*...*/ 대신 실제 코드로 모두 채워넣었습니다.

    @SchemaMapping(typeName = "Patient", field = "studies")
    public List<StudyDto> getStudiesForPatient(PatientDto patient) {
        return studyService.findStudiesByPid(patient.getPid());
    }

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

    // *** [핵심 수정 부분] 서로 다른 DB의 데이터를 연결하는 부분 ***
    // Study 타입의 'report' 필드가 요청될 때
    @SchemaMapping(typeName = "Study", field = "report")
    // --- [수정] 반환 타입을 엔티티(Report)에서 DTO(ReportResponseDto)로 변경 ---
    public ReportResponseDto getReportForStudy(StudyDto study) {
        if (study == null || study.getStudyKey() == null) {
            return null;
        }

        // 1. StudyKey를 사용하여 PostgreSQL DB의 ReportService를 호출하고 '엔티티'를 받습니다.
        var reportEntity = reportService.getReportByStudyKey(study.getStudyKey());

        // 2. 클라이언트에게 반환하기 전에 'DTO'로 변환합니다.
        return ReportResponseDto.fromEntity(reportEntity);
    }
}