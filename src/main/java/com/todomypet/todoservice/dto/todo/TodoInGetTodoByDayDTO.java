package com.todomypet.todoservice.dto.todo;

import com.todomypet.todoservice.domain.node.AlertType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class TodoInGetTodoByDayDTO {
    private String todoId;
    private String content;
    private boolean clearYN;
    private boolean receiveAlert;
    private String alertAt;
    private AlertType alertType;
}
