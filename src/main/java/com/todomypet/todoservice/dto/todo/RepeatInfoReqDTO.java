package com.todomypet.todoservice.dto.todo;

import com.todomypet.todoservice.domain.node.RepeatType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RepeatInfoReqDTO {
    RepeatType repeatType;
    List<Integer> repeatData;
}
