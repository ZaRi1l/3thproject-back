package com._thproject._thproject_web.postgresql.resolver;

import com._thproject._thproject_web.postgresql.dto.ReportInput;
import com._thproject._thproject_web.postgresql.dto.ReportResponseDto;
import com._thproject._thproject_web.postgresql.dto.UserResponseDto;
import com._thproject._thproject_web.postgresql.service.ReportService;

import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ReportResolver {

    private final ReportService reportService;

    // === Query Mappings ===

    @QueryMapping
    @PreAuthorize("isAuthenticated()")
    public ReportResponseDto reportByStudyKey(@Argument Long studyKey) {
        var reportEntity = reportService.getReportByStudyKey(studyKey);
        return ReportResponseDto.fromEntity(reportEntity);
    } // <-- [해결 1] 이 메소드가 '}' 로 올바르게 닫혔는지 확인합니다.

    // === Mutation Mappings ===

    @MutationMapping
    @PreAuthorize("isAuthenticated()")
    public ReportResponseDto createOrUpdateReport(@Argument ReportInput input) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserId = authentication.getName();

        // null 체크 로직
        String reportStatusString = (input.reportStatus() != null)
                // --- [핵심 수정 부분] .name()을 .toString()으로 변경 ---
                ? input.reportStatus().toString()
                : "DRAFT";

        var savedReportEntity = reportService.createOrUpdateReport(
                input.studyKey(),
                input.reportContent(),
                reportStatusString,
                currentUserId
        );

        return ReportResponseDto.fromEntity(savedReportEntity);
    }

    // === Nested Field Resolvers ===

    @SchemaMapping(typeName = "Report", field = "author")
    public UserResponseDto getAuthor(ReportResponseDto report) {
        return report.getAuthor();
    }
}