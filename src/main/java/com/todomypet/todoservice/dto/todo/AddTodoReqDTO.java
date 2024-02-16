package com.todomypet.todoservice.dto.todo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddTodoReqDTO {
    List<TodoInfoReqDTO> todoInfos;
    RepeatInfoReqDTO repeatInfo;
}
