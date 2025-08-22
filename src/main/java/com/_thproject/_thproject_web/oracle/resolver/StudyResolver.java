package com._thproject._thproject_web.oracle.resolver;
// ... package, imports ...
import com._thproject._thproject_web.oracle.service.StudyService;
import com._thproject._thproject_web.postgresql.entity.Report;
import com._thproject._thproject_web.postgresql.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

@Controller
@ConditionalOnProperty(name = "spring.datasource.oracle.enabled", havingValue = "true")
@RequiredArgsConstructor
public class StudyResolver {

    private final StudyService studyService; // 기존 Oracle Study 서비스
    private final ReportService reportService; // 새로 만든 PostgreSQL Report 서비스

    // ... 기존 Study, Patient, Series 등 리졸버 메소드들 ...

    /**
     * [추가된 부분]
     * Study 타입의 'report' 필드가 요청될 때 호출됩니다.
     * Oracle DB에서 조회된 Study 정보(StudyDto)를 기반으로,
     * PostgreSQL DB에서 해당 studyKey를 가진 Report를 조회하여 연결합니다.
     */
    @SchemaMapping(typeName = "Study", field = "report")
    public Report getReportForStudy(com._thproject._thproject_web.oracle.dto.StudyDto study) {
        if (study == null || study.getStudyKey() == null) {
            return null;
        }
        // StudyKey를 사용하여 PostgreSQL DB의 ReportService를 호출합니다.
        return reportService.getReportByStudyKey(study.getStudyKey());
    }
}