package com.richal.learnonline.dto;

import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
public class MessageStationDto {
    private Long id;
    private String title;
    private String content;
    private String type;
    private Date sendTime;
    private Integer isread;
    private List<Long> userIds;
}
