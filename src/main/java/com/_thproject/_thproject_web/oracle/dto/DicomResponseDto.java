package com._thproject._thproject_web.oracle.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DicomResponseDto {
    private int imageCnt;
    private String[] data;
}