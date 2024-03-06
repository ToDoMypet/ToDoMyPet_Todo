package com.todomypet.todoservice.dto.todo;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class GetTodoByAlertTimeResListDTO {
    List<GetTodoByAlertTimeResDTO> response;
}
