package com.richal.learnonline.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterParamsDto {

    private String mobile;

    private String password;

    private Integer regChannel;

    private String smsCode;
}
