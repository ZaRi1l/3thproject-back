package com._thproject._thproject_web.postgresql.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.OffsetDateTime;

@Entity
@Table(name = "reporttab")
@Getter
@Setter
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // BIGSERIAL에 해당
    @Column(name = "reportid")
    private Long reportId;

    @Column(name = "studykey", nullable = false, unique = true)
    private Long studyKey;

    // 기존 User 엔티티와의 다대일(N:1) 관계 설정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid", referencedColumnName = "userid", nullable = false)
    private User author; // 작성자 정보

    @Column(name = "report_status", length = 20)
    private String reportStatus;

    @Column(name = "report_content", columnDefinition = "TEXT")
    private String reportContent;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @Column(name = "delflag")
    private Short delFlag = 0;
}