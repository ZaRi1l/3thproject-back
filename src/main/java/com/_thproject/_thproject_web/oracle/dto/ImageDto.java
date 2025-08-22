package com._thproject._thproject_web.oracle.dto;
import lombok.Getter;
@Getter
public class ImageDto {
    private final Long studyKey;
    private final Long seriesKey;
    private final Long imageKey;
    private final String studyinsuid;
    private final String seriesinsuid;
    private final String sopinstanceuid;
    private final String sopclassuid;
    private final Integer ststorageid;
    private final String path;
    private final String fname;
    public ImageDto(Number studyKey, Number seriesKey, Number imageKey, String studyinsuid, String seriesinsuid, String sopinstanceuid, String sopclassuid, Number ststorageid, String path, String fname) {
        this.studyKey = (studyKey == null) ? null : studyKey.longValue();
        this.seriesKey = (seriesKey == null) ? null : seriesKey.longValue();
        this.imageKey = (imageKey == null) ? null : imageKey.longValue();
        this.studyinsuid = studyinsuid;
        this.seriesinsuid = seriesinsuid;
        this.sopinstanceuid = sopinstanceuid;
        this.sopclassuid = sopclassuid;
        this.ststorageid = (ststorageid == null) ? null : ststorageid.intValue();
        this.path = path;
        this.fname = fname;
    }

    @Getter
    public static class ImagePathDto {
        private final String path;
        private final String fname;
        public ImagePathDto(String path, String fname) {
            this.path = path;
            this.fname = fname;
        }
    }

}