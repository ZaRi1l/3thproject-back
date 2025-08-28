// src/main/java/com/_thproject/_thproject_web/oracle/controller/DicomDataController.java

package com._thproject._thproject_web.oracle.controller;

import com._thproject._thproject_web.oracle.dto.*;
import com._thproject._thproject_web.oracle.service.PatientService;
import com._thproject._thproject_web.oracle.service.SeriesService;
import com._thproject._thproject_web.oracle.service.StudyService;
import com._thproject._thproject_web.oracle.service.ImageService;
import com._thproject._thproject_web.postgresql.service.ReportService;
import com._thproject._thproject_web.postgresql.dto.ReportResponseDto;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import java.util.List;

@Controller
@ConditionalOnProperty(name = "spring.datasource.oracle.enabled", havingValue = "true")
public class DicomDataController {

    private final PatientService patientService;
    private final StudyService studyService;
    private final SeriesService seriesService;
    private final ImageService imageService;
    private final ReportService reportService;

    // 생성자는 변경 없이 그대로 유지됩니다.
    public DicomDataController(PatientService patientService, StudyService studyService,
                               SeriesService seriesService, ImageService imageService,
                               ReportService reportService) {
        this.patientService = patientService;
        this.studyService = studyService;
        this.seriesService = seriesService;
        this.imageService = imageService;
        this.reportService = reportService;
    }

    // --- 최상위 Query Resolver 수정 ---

    /**
     * [신규] 여러 조건(pid, pname)으로 환자 목록을 검색하는 API입니다.
     * GraphQL 스키마에 새로 추가한 'searchPatients' 쿼리를 처리합니다.
     * @param pid 환자 ID (부분 검색)
     * @param pname 환자 이름 (부분 검색)
     * @return 검색 조건에 맞는 환자 DTO 목록
     */
    @QueryMapping
    public List<PatientDto> searchPatients(@Argument String pid, @Argument String pname) {
        return patientService.searchPatients(pid, pname);
    }

    /**
     * [기존] 정확한 ID로 단일 환자를 조회하는 기능입니다.
     * 이 기능은 그대로 유지되지만, 이제 주로 상세 정보 확인 등 다른 용도로 사용될 수 있습니다.
     */
    @QueryMapping
    public PatientDto patient(@Argument String pid) {
        return patientService.getPatientByPid(pid);
    }

    /**
     * [기존] StudyKey로 단일 검사를 조회하는 기능입니다. (변경 없음)
     */
    @QueryMapping
    public StudyDto study(@Argument Long studyKey) {
        return studyService.getStudyByStudyKey(studyKey);
    }

    /**
     * [기존] StudyKey와 SeriesKey로 단일 시리즈를 조회하는 기능입니다. (변경 없음)
     */
    @QueryMapping
    public SeriesDto series(@Argument Long studyKey, @Argument Long seriesKey) {
        return seriesService.getSeriesByStudyKeyAndSeriesKey(studyKey, seriesKey);
    }


    // --- 관계 필드 Resolver (SchemaMapping) 수정 ---

    /**
     * [수정] Patient 타입의 'studies' 필드를 처리할 때, 날짜 범위 인자를 받도록 수정합니다.
     * GraphQL 쿼리에서 'studies(studyDateStart: "...", studyDateEnd: "...")'와 같이 호출하면,
     * 이 인자들을 StudyService로 전달하여 검사 목록을 필터링합니다.
     * @param patient 부모 객체인 환자 정보
     * @param studyDateStart 검색 시작일 (YYYY-MM-DD 형식)
     * @param studyDateEnd 검색 종료일 (YYYY-MM-DD 형식)
     * @return 필터링된 검사 DTO 목록
     */
    @SchemaMapping(typeName = "Patient", field = "studies")
    public List<StudyDto> getStudiesForPatient(PatientDto patient,
                                               @Argument String studyDateStart,
                                               @Argument String studyDateEnd) {
        // 수정된 StudyService의 findStudiesByPid 메소드를 호출합니다.
        return studyService.findStudiesByPid(patient.getPid(), studyDateStart, studyDateEnd);
    }

    // --- 아래의 SchemaMapping 메소드들은 변경 사항이 없습니다. ---

    @SchemaMapping(typeName = "Study", field = "series")
    public List<SeriesDto> getSeriesForStudy(StudyDto study) {
        return seriesService.findByStudyKey(study.getStudyKey());
    }

    @SchemaMapping(typeName = "Study", field = "patient")
    public PatientDto getPatientForStudy(StudyDto study) {
        return patientService.getPatientByPid(study.getPid());
    }

    @SchemaMapping(typeName = "Series", field = "images")
    public List<ImageDto> getImagesForSeries(SeriesDto series) {
        return imageService.findBySeriesKey(series.getSeriesKey());
    }

    @SchemaMapping(typeName = "Series", field = "study")
    public StudyDto getStudyForSeries(SeriesDto series) {
        return studyService.getStudyByStudyKey(series.getStudyKey());
    }

    @SchemaMapping(typeName = "Image", field = "series")
    public SeriesDto getSeriesForImage(ImageDto image) {
        return seriesService.findBySeriesKey(image.getSeriesKey());
    }

    @SchemaMapping(typeName = "Study", field = "report")
    public ReportResponseDto getReportForStudy(StudyDto study) {
        if (study == null || study.getStudyKey() == null) {
            return null;
        }
        var reportEntity = reportService.getReportByStudyKey(study.getStudyKey());
        return ReportResponseDto.fromEntity(reportEntity);
    }
}