package com.todomypet.todoservice.service;

import com.todomypet.todoservice.dto.todo.*;

import java.util.List;

public interface TodoService {
    List<AddTodoResDTO> addTodo(String userId, AddTodoReqDTO todoInfoReqDTO);

    void clearTodo(String userId, ClearTodoReqDTO clearTodoReqDTO);

    List<GetTodoByMonthResDTO> getTodoByMonth(String userId, String month);

    List<GetTodoByDayResDTO> getTodoByDay(String userId, String day);

    void unclearTodo(String userId, UnclearTodoReqDTO unclearTodoReqDTO);

    String deleteTodo(String userId, String todoId);

    TodoDetailResDTO getTodoDetail(String userId, String todoId);

    String updateTodo(String userId, String todoId, UpdateTodoReqDTO updateInfos);

    String endTheRepeatTodo(String userId, EndTheRepeatTodoReqDTO req);

    List<GetTodoByAlertTimeResDTO> getTodoByAlertAt(String alertAt);
}
