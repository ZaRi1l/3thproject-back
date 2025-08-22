package com._thproject._thproject_web.oracle.controller;

import com._thproject._thproject_web.oracle.dto.*;
import com._thproject._thproject_web.oracle.repository.*;
// [추가] PostgreSQL의 Report 엔티티와 ReportService를 import 합니다.
import com._thproject._thproject_web.postgresql.entity.Report;
import com._thproject._thproject_web.postgresql.service.ReportService;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import java.util.List;

@Controller
@ConditionalOnProperty(name = "spring.datasource.oracle.enabled", havingValue = "true")
public class DicomDataController {

    // --- 기존 Oracle Repository ---
    private final PatientRepository patientRepository;
    private final StudyRepository studyRepository;
    private final SeriesRepository seriesRepository;
    private final ImageRepository imageRepository;

    // --- [추가] PostgreSQL Service ---
    private final ReportService reportService;

    // [수정] 생성자에 ReportService 주입을 추가합니다.
    public DicomDataController(PatientRepository patientRepository, StudyRepository studyRepository,
                               SeriesRepository seriesRepository, ImageRepository imageRepository,
                               ReportService reportService) {
        this.patientRepository = patientRepository;
        this.studyRepository = studyRepository;
        this.seriesRepository = seriesRepository;
        this.imageRepository = imageRepository;
        this.reportService = reportService;
    }

    // --- 최상위 Query Resolver (기존 코드) ---

    @QueryMapping
    public PatientDto patient(@Argument String pid) {
        return patientRepository.findByPid(pid).orElse(null);
    }

    @QueryMapping
    public StudyDto study(@Argument Long studyKey) {
        return studyRepository.findByStudyKey(studyKey).orElse(null);
    }

    @QueryMapping
    public SeriesDto series(@Argument Long studyKey, @Argument Long seriesKey) {
        return seriesRepository.findByStudyKeyAndSeriesKey(studyKey, seriesKey).orElse(null);
    }

    // --- 관계 필드 Resolver (SchemaMapping) (기존 코드) ---
    // 이 부분의 코드를 /*...*/ 대신 실제 코드로 모두 채워넣었습니다.

    @SchemaMapping(typeName = "Patient", field = "studies")
    public List<StudyDto> getStudiesForPatient(PatientDto patient) {
        return studyRepository.findStudiesByPid(patient.getPid());
    }

    @SchemaMapping(typeName = "Study", field = "series")
    public List<SeriesDto> getSeriesForStudy(StudyDto study) {
        return seriesRepository.findByStudyKey(study.getStudyKey());
    }
}