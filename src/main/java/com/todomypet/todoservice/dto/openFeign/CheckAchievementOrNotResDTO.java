package com.todomypet.todoservice.dto.openFeign;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CheckAchievementOrNotResDTO {
    private boolean achieveOrNot;
    private String achievementId;
}
