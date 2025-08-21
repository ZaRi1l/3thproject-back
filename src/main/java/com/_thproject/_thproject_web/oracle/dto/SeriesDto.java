package com._thproject._thproject_web.oracle.dto;
import lombok.Getter;
@Getter
public class SeriesDto {
    private final Long studyKey;
    private final Long seriesKey;
    private final String studyinsuid;
    private final String seriesinsuid;
    private final Integer seriesnum;
    private final String modality;
    private final String seriesdate;
    private final String seriestime;
    private final String bodypart;
    private final String seriesdesc;
    private final Integer imagecnt;
    public SeriesDto(Number studyKey, Number seriesKey, String studyinsuid, String seriesinsuid, Number seriesnum, String modality, String seriesdate, String seriestime, String bodypart, String seriesdesc, Number imagecnt) {
        this.studyKey = (studyKey == null) ? null : studyKey.longValue();
        this.seriesKey = (seriesKey == null) ? null : seriesKey.longValue();
        this.studyinsuid = studyinsuid;
        this.seriesinsuid = seriesinsuid;
        this.seriesnum = (seriesnum == null) ? null : seriesnum.intValue();
        this.modality = modality;
        this.seriesdate = seriesdate;
        this.seriestime = seriestime;
        this.bodypart = bodypart;
        this.seriesdesc = seriesdesc;
        this.imagecnt = (imagecnt == null) ? null : imagecnt.intValue();
    }
}