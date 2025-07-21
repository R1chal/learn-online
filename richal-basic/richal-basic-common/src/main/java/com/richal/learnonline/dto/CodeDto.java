package com.richal.learnonline.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 封装验证码和时间戳
 *
 * @author Richal
 * @since 2025/07/21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CodeDto {

    private String code;

    private Long timer;
}
