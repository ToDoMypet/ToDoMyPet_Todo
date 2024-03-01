package com.todomypet.todoservice.dto.openFeign;

import com.todomypet.todoservice.domain.node.AchievementType;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AchieveReqDTO {
    private AchievementType type;
    private int condition;
}
