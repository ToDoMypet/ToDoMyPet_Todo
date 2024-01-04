package com.todomypet.todoservice.dto.todo;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AddTodoResDTO {
    private String todoId;
}
