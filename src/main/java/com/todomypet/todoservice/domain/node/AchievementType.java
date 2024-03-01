package com.todomypet.todoservice.domain.node;

import lombok.Getter;

@Getter
public enum AchievementType {
    ATTENDANCE("출석"),
    ACHIEVE("달성"),
    EVOLUTION("진화"),
    GRADUATION("최종"),
    CONTINUE_ATTENDANCE("연속 출석")
    ;

    private final String achievementType;

    AchievementType(String achievementType) {
        this.achievementType = achievementType;
    }
}
