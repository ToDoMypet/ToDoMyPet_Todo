package com.todomypet.todoservice.dto.openFeign;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UpdateExperiencePointReqDTO {
    private String petSeqId;
    private int experiencePoint;
}
