package com.todomypet.todoservice.service;

import com.todomypet.todoservice.dto.todo.*;

import java.util.List;

public interface TodoService {
    AddTodoResDTO addTodo(String userId, AddTodoReqDTO addTodoReqDTO);

    void clearTodo(String userId, ClearTodoReqDTO clearTodoReqDTO);

    List<GetTodoByMonthResDTO> getTodoByMonth(String userId, String month);

    List<GetTodoByDayResDTO> getTodoByDay(String userId, String day);
}
