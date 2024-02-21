package com.todomypet.todoservice.domain.node;

import lombok.Getter;

@Getter
public enum AlertType {
    EVENT_TIME("이벤트 시간"),
    FIVE_MINUTE("5분 전"),
    TEN_MINUTE("10분 전"),
    FIFTEEN_MINUTE("15분 전"),
    THIRTY_MINUTE("30분 전"),
    ONE_HOUR("1시간 전"),
    TWO_HOUR("2시간 전"),
    ONE_DAY("1일 전"),
    TWO_DAY("2일 전"),
    ONE_WEEK("1주 전")
    ;

    private final String alertType;

    AlertType(String achievementType) {
        this.alertType = achievementType;
    }
}
