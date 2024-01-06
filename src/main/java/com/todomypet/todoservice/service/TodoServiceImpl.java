package com.todomypet.todoservice.service;

import com.todomypet.todoservice.domain.node.Todo;
import com.todomypet.todoservice.dto.openFeign.UpdateExperiencePointReqDTO;
import com.todomypet.todoservice.dto.todo.AddTodoReqDTO;
import com.todomypet.todoservice.dto.todo.AddTodoResDTO;
import com.todomypet.todoservice.dto.todo.ClearTodoReqDTO;
import com.todomypet.todoservice.exception.CustomException;
import com.todomypet.todoservice.exception.ErrorCode;
import com.todomypet.todoservice.repository.HaveRepository;
import com.todomypet.todoservice.repository.IncludeRepository;
import com.todomypet.todoservice.repository.TodoRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TodoServiceImpl implements TodoService {

    private final HaveRepository haveRepository;
    private final TodoRepository todoRepository;
    private final IncludeRepository includeRepository;
    private final UserServiceClient userServiceClient;


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

    @Override
    @Transactional
    public void clearTodo(String userId, ClearTodoReqDTO clearTodoReqDTO) {
        String categoryId = clearTodoReqDTO.getCategoryId();
        String todoId = clearTodoReqDTO.getTodoId();

        if (!haveRepository.existsHaveRelationshipBetweenUserAndCategory(userId, categoryId)){
            throw new CustomException(ErrorCode.WRONG_CATEGORY_ID);
        }
        if (!includeRepository.existsIncludeRelationshipBetweenCategoryAndTodo(categoryId, todoId)) {
            throw new CustomException(ErrorCode.NOT_EXISTS_RELATIONSHIP_BETWEEN_CATEGORY_AND_TODO);
        }

        if (!todoRepository.isGetExperiencePoint(todoId)) {
            String petSeq = userServiceClient.getMainPet(userId).getData();
            userServiceClient.updateExperiencePoint(userId, UpdateExperiencePointReqDTO.builder()
                    .petSeqId(petSeq).experiencePoint(5).build());
        }

        todoRepository.updateClearYNAndGetExperienceByTodoId(clearTodoReqDTO.getTodoId());
    }
}
