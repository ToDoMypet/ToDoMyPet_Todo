package com.todomypet.todoservice.dto.todo;

import com.todomypet.todoservice.domain.node.AlertType;
import com.todomypet.todoservice.domain.node.RepeatType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Builder
@Getter
public class TodoDetailResDTO {
    private String todoId;
    private String content;
    private String categoryName;
    private String categoryColorCode;
    private LocalDate startedAtDate;
    private LocalTime startedAtTime;
    private LocalDate endedAtDate;
    private LocalTime endedAtTime;
    private RepeatType repeatType;
    private List<Integer> repeatData;
    private LocalDate repeatEndDate;
    private boolean markOnTheCalenderOrNot;
    private boolean receiveAlert;
    private AlertType alertType;
    private LocalDateTime alertAt;
}
