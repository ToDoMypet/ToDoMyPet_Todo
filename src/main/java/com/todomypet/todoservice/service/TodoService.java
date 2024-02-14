package com.todomypet.todoservice.service;

import com.todomypet.todoservice.dto.todo.*;

import java.util.List;

public interface TodoService {
    List<AddTodoResDTO> addTodo(String userId, List<AddTodoReqDTO> addTodoReqDTO);

    void clearTodo(String userId, ClearTodoReqDTO clearTodoReqDTO);

    List<GetTodoByMonthResDTO> getTodoByMonth(String userId, String month);

    List<GetTodoByDayResDTO> getTodoByDay(String userId, String day);
}
