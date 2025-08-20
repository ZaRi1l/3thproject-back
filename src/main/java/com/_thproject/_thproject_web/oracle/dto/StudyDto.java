package com._thproject._thproject_web.oracle.dto;

import lombok.Getter;

// 이 DTO는 Oracle DB의 StudyTab 테이블과 1:1로 매핑되는 순수 데이터 객체입니다.
@Getter
public class StudyDto {

    private final Long studyKey;
    private final String studyinsuid;
    private final String studydate;
    private final String studytime;
    private final String accessnum;
    private final String studyid;
    private final String studydesc;
    private final String modality;
    private final String bodypart;
    private final String pid;
    private final String pname;
    private final String psex;
    private final String pbirthdatetime;
    private final String patage;
    private final Integer seriescnt;
    private final Integer imagecnt;

    /**
     * Spring Data JPA가 Native Query 결과를 이 DTO 객체로 바로 매핑하기 위해 사용하는 생성자입니다.
     * SELECT 절의 컬럼 순서와 이 생성자의 파라미터 순서, 타입이 정확히 일치해야 합니다.
     * Oracle의 NUMBER 타입은 Java의 Number 객체로 매핑되므로, 안전하게 변환해줍니다.
     */
    public StudyDto(Number studyKey, String studyinsuid, String studydate, String studytime,
                    String accessnum, String studyid, String studydesc, String modality,
                    String bodypart, String pid, String pname, String psex,
                    String pbirthdatetime, String patage, Number seriescnt, Number imagecnt) {
        this.studyKey = (studyKey == null) ? null : studyKey.longValue();
        this.studyinsuid = studyinsuid;
        this.studydate = studydate;
        this.studytime = studytime;
        this.accessnum = accessnum;
        this.studyid = studyid;
        this.studydesc = studydesc;
        this.modality = modality;
        this.bodypart = bodypart;
        this.pid = pid;
        this.pname = pname;
        this.psex = psex;
        this.pbirthdatetime = pbirthdatetime;
        this.patage = patage;
        this.seriescnt = (seriescnt == null) ? null : seriescnt.intValue();
        this.imagecnt = (imagecnt == null) ? null : imagecnt.intValue();
    }
}