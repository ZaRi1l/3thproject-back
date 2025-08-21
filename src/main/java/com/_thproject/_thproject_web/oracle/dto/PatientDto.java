package com._thproject._thproject_web.oracle.dto;
import lombok.Getter;
@Getter
public class PatientDto {
    private final String pid;
    private final String pname;
    private final String psex;
    private final String pbirthdate;
    public PatientDto(String pid, String pname, String psex, String pbirthdate) {
        this.pid = pid;
        this.pname = pname;
        this.psex = psex;
        this.pbirthdate = pbirthdate;
    }
}