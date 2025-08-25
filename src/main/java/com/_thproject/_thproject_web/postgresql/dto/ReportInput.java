package com._thproject._thproject_web.postgresql.dto;

/**
 * Report 생성 또는 수정을 위한 입력 DTO.
 * GraphQL 스키마의 input 타입과 일치해야 합니다.
 */
public record ReportInput(
        Long studyKey,
        String reportContent,
        ReportStatus reportStatus // 이제 public으로 선언된 ReportStatus를 참조합니다.
) {}