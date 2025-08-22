package com._thproject._thproject_web.postgresql.service;

import com._thproject._thproject_web.postgresql.entity.Report;
import com._thproject._thproject_web.postgresql.entity.User;
import com._thproject._thproject_web.postgresql.repository.ReportRepository;
import com._thproject._thproject_web.postgresql.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository; // 사용자 정보 조회를 위해 주입

    /**
     * StudyKey로 소견서를 조회합니다.
     * @param studyKey 조회할 검사의 StudyKey
     * @return Report 객체 또는 null
     */
    @Transactional(readOnly = true)
    public Report getReportByStudyKey(Long studyKey) {
        return reportRepository.findByStudyKey(studyKey).orElse(null);
    }

    /**
     * 소견서를 생성하거나 업데이트합니다.
     * @param studyKey 대상 검사 키
     * @param content 소견 내용
     * @param status 소견 상태
     * @param userId 작성자 ID (JWT 토큰에서 추출된 값)
     * @return 저장된 Report 객체
     */
    @Transactional
    public Report createOrUpdateReport(Long studyKey, String content, String status, String userId) {
        // 1. JWT 토큰의 userId로 작성자(User) 엔티티를 DB에서 조회합니다.
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("인증된 사용자를 DB에서 찾을 수 없습니다: " + userId));

        // 2. studyKey로 기존 소견서가 있는지 확인합니다.
        // 없으면 새로운 Report 객체를 생성하고, 있으면 기존 객체를 가져옵니다.
        Report report = reportRepository.findByStudyKey(studyKey)
                .orElse(new Report());

        // 3. 소견서 정보를 설정합니다. (생성/업데이트 공통 로직)
        report.setStudyKey(studyKey);
        report.setAuthor(author); // 조회한 User 엔티티를 설정
        report.setReportContent(content);
        report.setReportStatus(status);
        report.setDelFlag((short) 0);

        // 4. JpaRepository의 save 메소드를 통해 저장(INSERT 또는 UPDATE) 후 반환합니다.
        return reportRepository.save(report);
    }
}