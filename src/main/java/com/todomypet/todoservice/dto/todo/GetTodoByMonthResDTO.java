package com.todomypet.todoservice.dto.todo;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class GetTodoByMonthResDTO {
    private String id;
    private String todoContent;
    private String todoStartedAt;
    private String todoEndedAt;
    private String categoryTextColorCode;
    private String categoryBgColorCode;
}
