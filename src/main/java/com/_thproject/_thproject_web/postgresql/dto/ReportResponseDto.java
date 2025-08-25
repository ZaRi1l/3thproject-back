package com._thproject._thproject_web.postgresql.dto;

import com._thproject._thproject_web.postgresql.entity.Report;
import lombok.Builder;
import lombok.Getter;
import java.time.OffsetDateTime;

@Getter
@Builder
public class ReportResponseDto {
    private Long reportId;
    private Long studyKey;
    private String reportStatus;
    private String reportContent;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private UserResponseDto author; // 기존의 UserResponseDto를 포함

    public static ReportResponseDto fromEntity(Report report) {
        if (report == null) return null;
        return ReportResponseDto.builder()
                .reportId(report.getReportId())
                .studyKey(report.getStudyKey())
                .reportStatus(report.getReportStatus())
                .reportContent(report.getReportContent())
                .createdAt(report.getCreatedAt())
                .updatedAt(report.getUpdatedAt())
                .author(UserResponseDto.from(report.getAuthor())) // UserResponseDto 활용
                .build();
    }
}