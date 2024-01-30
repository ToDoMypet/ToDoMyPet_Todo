package com.todomypet.todoservice.domain.node;

import lombok.Getter;

@Getter
public enum AlertType {
    ;

    private final String alertType;

    AlertType(String achievementType) {
        this.alertType = achievementType;
    }
}
