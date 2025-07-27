package com.richal.learnonline.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SMSDto {

    private String title;

    private String content;

    private Date sendTime;

    private List<UserPhoneDto> userPhoneDtos;
}
