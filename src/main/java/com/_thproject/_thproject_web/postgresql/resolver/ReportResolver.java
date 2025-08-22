package com._thproject._thproject_web.postgresql.resolver;

import com._thproject._thproject_web.postgresql.entity.Report;
import com._thproject._thproject_web.postgresql.entity.User;
import com._thproject._thproject_web.postgresql.service.ReportService;
// ReportInput은 스키마 정의에 따른 DTO 클래스입니다.
// spring-graphql이 자동으로 생성해주거나 직접 만들어야 할 수 있습니다.
// 여기서는 Map 또는 직접 만든 DTO를 사용한다고 가정합니다.
// 아래 예시는 직접 만든 DTO(record)를 사용하는 경우입니다.
// record ReportInput(Long studyKey, String reportContent, String reportStatus) {}

import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

// ReportInput DTO 정의 (Resolver 파일 안에 간단히 정의하거나 별도 파일로 분리 가능)
record ReportInput(Long studyKey, String reportContent, ReportStatus reportStatus) {}
enum ReportStatus { DRAFT, FINAL, CORRECTED }


@Controller
@RequiredArgsConstructor
public class ReportResolver {

    private final ReportService reportService;

    // === Query Mappings ===

    @QueryMapping
    @PreAuthorize("isAuthenticated()") // 로그인한 사용자만 조회 가능
    public Report reportByStudyKey(@Argument Long studyKey) {
        return reportService.getReportByStudyKey(studyKey);
    }

    // === Mutation Mappings ===

    @MutationMapping
    @PreAuthorize("isAuthenticated()") // 로그인한 사용자만 작성/수정 가능
    public Report createOrUpdateReport(@Argument ReportInput input) {
        // Spring Security 컨텍스트에서 현재 인증된 사용자 정보를 가져옵니다.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // User 엔티티의 getUsername()이 userid를 반환하도록 했으므로, .getName()은 userid입니다.
        String currentUserId = authentication.getName();

        return reportService.createOrUpdateReport(
                input.studyKey(),
                input.reportContent(),
                input.reportStatus().name(), // Enum을 String으로 변환
                currentUserId // 서비스에 현재 로그인된 사용자의 ID를 안전하게 전달
        );
    }

    // === Nested Field Resolvers ===

    // GraphQL 스키마의 Report 타입 안에 있는 'author' 필드를 해석하는 메소드
    @SchemaMapping(typeName = "Report", field = "author")
    public User getAuthor(Report report) {
        // Report 엔티티는 이미 User 객체를 'author' 필드에 가지고 있습니다.
        // LAZY 로딩이므로, 이 필드에 접근하는 시점에 실제 쿼리가 발생합니다.
        return report.getAuthor();
    }
}