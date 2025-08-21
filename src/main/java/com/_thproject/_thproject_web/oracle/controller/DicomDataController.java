package com._thproject._thproject_web.oracle.controller;

import com._thproject._thproject_web.oracle.dto.ImageDto;
import com._thproject._thproject_web.oracle.dto.PatientDto;
import com._thproject._thproject_web.oracle.dto.SeriesDto;
import com._thproject._thproject_web.oracle.dto.StudyDto;
import com._thproject._thproject_web.oracle.repository.ImageRepository;
import com._thproject._thproject_web.oracle.repository.PatientRepository;
import com._thproject._thproject_web.oracle.repository.SeriesRepository;
import com._thproject._thproject_web.oracle.repository.StudyRepository;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import java.util.List;


@Controller
@ConditionalOnProperty(name = "spring.datasource.oracle.enabled", havingValue = "true") // <-- 이 줄 추가
public class DicomDataController {

    private final PatientRepository patientRepository;
    private final StudyRepository studyRepository;
    private final SeriesRepository seriesRepository;
    private final ImageRepository imageRepository;

    public DicomDataController(PatientRepository patientRepository, StudyRepository studyRepository,
                               SeriesRepository seriesRepository, ImageRepository imageRepository) {
        this.patientRepository = patientRepository;
        this.studyRepository = studyRepository;
        this.seriesRepository = seriesRepository;
        this.imageRepository = imageRepository;
    }

    // --- 최상위 Query Resolver ---

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

    // --- 관계 필드 Resolver (SchemaMapping) ---

    @SchemaMapping(typeName = "Patient", field = "studies")
    public List<StudyDto> getStudiesForPatient(PatientDto patient) {
        return studyRepository.findStudiesByPid(patient.getPid());
    }

    @SchemaMapping(typeName = "Study", field = "series")
    public List<SeriesDto> getSeriesForStudy(StudyDto study) {
        return seriesRepository.findByStudyKey(study.getStudyKey());
    }
    
    @SchemaMapping(typeName = "Study", field = "patient")
    public PatientDto getPatientForStudy(StudyDto study) {
        return patientRepository.findByPid(study.getPid()).orElse(null);
    }

    @SchemaMapping(typeName = "Series", field = "images")
    public List<ImageDto> getImagesForSeries(SeriesDto series) {
        return imageRepository.findBySeriesKey(series.getSeriesKey());
    }
    
    @SchemaMapping(typeName = "Series", field = "study")
    public StudyDto getStudyForSeries(SeriesDto series) {
        return studyRepository.findByStudyKey(series.getStudyKey()).orElse(null);
    }

    @SchemaMapping(typeName = "Image", field = "series")
    public SeriesDto getSeriesForImage(ImageDto image) {
        return seriesRepository.findBySeriesKey(image.getSeriesKey()).orElse(null);
    }
}