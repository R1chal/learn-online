package com.richal.learnonline.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KillCourseDTO {

    @NotNull(message = "非空字段")
    private Long activityId;
    @NotNull(message = "非空字段")
    private Long courseId;
}
