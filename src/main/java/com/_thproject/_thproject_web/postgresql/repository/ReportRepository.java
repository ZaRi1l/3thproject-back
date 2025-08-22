package com._thproject._thproject_web.postgresql.repository;

import com._thproject._thproject_web.postgresql.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("postgresqlReportRepository") // Bean 이름 충돌 방지를 위해 명시적 이름 지정
public interface ReportRepository extends JpaRepository<Report, Long> {

    // StudyKey로 소견서를 찾는 메소드 (가장 많이 사용됨)
    Optional<Report> findByStudyKey(Long studyKey);
}