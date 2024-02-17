package com.todomypet.todoservice.domain.node;

import lombok.Getter;

@Getter
public enum AlertType {
    EXACT("정각"),
    FIVE_MINUTE("5분 전"),
    TEN_MINUTE("10분 전"),
    ONE_HOUR("1시간 전"),
    ONE_DAY("하루 전")
    ;

    private final String alertType;

    AlertType(String achievementType) {
        this.alertType = achievementType;
    }
}
