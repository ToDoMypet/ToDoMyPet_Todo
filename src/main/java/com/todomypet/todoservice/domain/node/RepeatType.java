package com.todomypet.todoservice.domain.node;

import lombok.Getter;

@Getter
public enum RepeatType {
    NONE_REPEAT("반복 없음"),
    REPEAT_DAILY("매일"),
    REPEAT_WEEKLY("매주"),
    REPEAT_MONTHLY("매월")
    ;

    private final String repeatType;

    RepeatType(String repeatType) {
        this.repeatType = repeatType;
    }
}
