package com.todomypet.todoservice.dto.openFeign;

import com.todomypet.todoservice.domain.node.AchievementType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CheckAchieveOrNotReqDTO {
    private AchievementType type;
    private int condition;
}
