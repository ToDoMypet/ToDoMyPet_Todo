package com.todomypet.todoservice.service;

import com.todomypet.todoservice.domain.node.Todo;
import com.todomypet.todoservice.dto.todo.AddTodoReqDTO;
import com.todomypet.todoservice.dto.todo.AddTodoResDTO;
import com.todomypet.todoservice.exception.CustomException;
import com.todomypet.todoservice.exception.ErrorCode;
import com.todomypet.todoservice.repository.HaveRepository;
import com.todomypet.todoservice.repository.IncludeRepository;
import com.todomypet.todoservice.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService {

    private final HaveRepository haveRepository;
    private final TodoRepository todoRepository;
    private final IncludeRepository includeRepository;


    @Override
    public AddTodoResDTO addTodo(String userId, AddTodoReqDTO addTodoReqDTO) {
        if (!haveRepository.existsHaveRelationshipBetweenUserAndCategory(userId, addTodoReqDTO.getCategoryId())) {
            throw new CustomException(ErrorCode.WRONG_CATEGORY_ID);
        };

        Todo todo = Todo.builder().content(addTodoReqDTO.getContent()).startedAt(addTodoReqDTO.getStartedAt())
                .endedAt(addTodoReqDTO.getEndedAt()).receiveAlert(addTodoReqDTO.isReceiveAlert()).clearYN(false)
                .getExperiencePointOrNot(false).markOnTheCalenderOrNot(addTodoReqDTO.isMarkOnTheCalenderOrNot())
                .alertAt(addTodoReqDTO.getAlertAt()).build();

        String todoId = todoRepository.save(todo).getId();

        includeRepository.createIncludeRelationshipBetweenCategoryAndTodo(todoId, addTodoReqDTO.getCategoryId());

        return AddTodoResDTO.builder().todoId(todoId).build();
    }
}
