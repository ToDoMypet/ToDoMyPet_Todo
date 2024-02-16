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
public class TodoInfoReqDTO {
    private String categoryId;
    private String content;
    private String startedAtDate;
    private String startedAtTime;
    private String endedAtDate;
    private String endedAtTime;
    private boolean receiveAlert;
    private boolean markOnTheCalenderOrNot;
    private LocalDateTime alertAt;
}
