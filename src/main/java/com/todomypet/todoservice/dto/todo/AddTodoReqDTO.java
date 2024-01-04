package com.todomypet.todoservice.dto.todo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddTodoReqDTO {
    private String categoryId;
    private String content;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private boolean receiveAlert;
    private boolean markOnTheCalenderOrNot;
    private LocalDateTime alertAt;
}
