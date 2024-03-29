package com.todomypet.todoservice.service;

import com.github.f4b6a3.ulid.UlidCreator;
import com.todomypet.todoservice.domain.node.Category;
import com.todomypet.todoservice.domain.node.RepeatType;
import com.todomypet.todoservice.domain.node.Todo;
import com.todomypet.todoservice.domain.relationship.Have;
import com.todomypet.todoservice.dto.openFeign.UpdateExperiencePointReqDTO;
import com.todomypet.todoservice.dto.todo.*;
import com.todomypet.todoservice.exception.CustomException;
import com.todomypet.todoservice.exception.ErrorCode;
import com.todomypet.todoservice.repository.CategoryRepository;
import com.todomypet.todoservice.repository.HaveRepository;
import com.todomypet.todoservice.repository.IncludeRepository;
import com.todomypet.todoservice.repository.TodoRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@AllArgsConstructor
public class TodoServiceImpl implements TodoService {

    private final HaveRepository haveRepository;
    private final TodoRepository todoRepository;
    private final IncludeRepository includeRepository;
    private final PetServiceClient petServiceClient;
    private final CategoryRepository categoryRepository;
    private final UserServiceClient userServiceClient;


    @Override
    @Transactional
    public List<AddTodoResDTO> addTodo(String userId, AddTodoReqDTO todoInfoReqDTO) {

        List<AddTodoResDTO> response = new ArrayList<>();
        RepeatType repeatType = todoInfoReqDTO.getRepeatInfo().getRepeatType();
        List<Integer> repeatData = todoInfoReqDTO.getRepeatInfo().getRepeatData();
        repeatData.sort(Integer::compareTo);
        StringBuilder repeatCode = new StringBuilder();
        Random rnd = new Random();

        while (true) {
            for (int i = 0; i < 2; i++) {
                repeatCode.append((char) (rnd.nextInt(26) + 65));
            }
            for (int i = 0; i < 9; i++) {
                repeatCode.append(rnd.nextInt(10));
            }

            if (todoRepository.getTodoByRepeatCode(repeatCode.toString()).isEmpty()) {
                break;
            }
        }

        for (TodoInfoReqDTO req : todoInfoReqDTO.getTodoInfos()) {
            if (haveRepository.existsHaveRelationshipBetweenUserAndCategory(userId, req.getCategoryId()) == null) {
                throw new CustomException(ErrorCode.WRONG_CATEGORY_ID);
            };

            Todo.TodoBuilder todoBuilder = Todo.builder().id(UlidCreator.getUlid().toString())
                .content(req.getContent())
                .startedAtDate(LocalDate.parse(req.getStartedAtDate()))
                .receiveAlert(req.isReceiveAlert()).clearYN(false)
                .getExperiencePointOrNot(false).markOnTheCalenderOrNot(req.isMarkOnTheCalenderOrNot())
                .alertAt(req.getAlertAt())
                .alertType(req.getAlertType())
                .repeatType(repeatType)
                .repeatData(repeatData)
                .repeatCode(repeatCode.toString());

            if (req.getStartedAtTime() != null) {
                todoBuilder.startedAtTime(LocalTime.parse(req.getStartedAtTime()));
            }
            if (req.getEndedAtDate() != null) {
                todoBuilder.endedAtDate(LocalDate.parse(req.getEndedAtDate()));
            }
            if (req.getEndedAtTime() != null) {
                todoBuilder.endedAtTime(LocalTime.parse(req.getEndedAtTime()));
            }
            if (todoInfoReqDTO.getRepeatInfo().getRepeatEndDate() != null) {
                todoBuilder.repeatEndDate(todoInfoReqDTO.getRepeatInfo().getRepeatEndDate());
            }
            if (todoInfoReqDTO.getRepeatInfo().getRepeatStartDate() != null) {
                todoBuilder.repeatStartDate(todoInfoReqDTO.getRepeatInfo().getRepeatStartDate());
            }

            Todo todo = todoBuilder.build();

            String todoId = todoRepository.save(todo).getId();

            includeRepository.createIncludeRelationshipBetweenCategoryAndTodo(todoId, req.getCategoryId());
            response.add(AddTodoResDTO.builder().todoId(todoId).build());
        }

        return response;
    }

    @Override
    @Transactional
    public void clearTodo(String userId, ClearTodoReqDTO clearTodoReqDTO) {
        String categoryId = clearTodoReqDTO.getCategoryId();
        String todoId = clearTodoReqDTO.getTodoId();
        Todo todo = todoRepository.getOneTodoByTodoId(todoId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXISTS_TODO_ID));

        if (todo.isClearYN()) {
            throw new CustomException(ErrorCode.ALREADY_CLEAR_TODO);
        }

        if (haveRepository.existsHaveRelationshipBetweenUserAndCategory(userId, categoryId) == null){
            throw new CustomException(ErrorCode.WRONG_CATEGORY_ID);
        }
        if (!includeRepository.existsIncludeRelationshipBetweenCategoryAndTodo(categoryId, todoId)) {
            throw new CustomException(ErrorCode.NOT_EXISTS_RELATIONSHIP_BETWEEN_CATEGORY_AND_TODO);
        }

        if (!todoRepository.isGetExperiencePoint(todoId)) {
            try {
                String petSeq = petServiceClient.getMainPet(userId).getData();
                petServiceClient.updateExperiencePoint(userId, UpdateExperiencePointReqDTO.builder()
                        .petSeqId(petSeq).experiencePoint(5).build());
            } catch (Exception e) {
                e.printStackTrace();
                throw new CustomException(ErrorCode.FEIGN_CLIENT_ERROR);
            }

        }

        todoRepository.updateClearYNAndGetExperienceByTodoId(clearTodoReqDTO.getTodoId());
    }

    @Override
    @Transactional
    public List<GetTodoByMonthResDTO> getTodoByMonth(String userId, String month) {
        List<GetTodoByMonthResDTO> response = new ArrayList<>();
        LocalDate searchTarget = LocalDate.parse(month + "-01", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate preTarget = searchTarget.minusMonths(1);

        LocalDate postTarget = searchTarget.plusMonths(1);
        int lastDayOfPostMonth = postTarget.lengthOfMonth();
        List<Todo> todos = todoRepository.getAllTodoByUserAndMonth(userId, preTarget.getYear(), preTarget.getMonthValue(),
                postTarget.getYear(), postTarget.getMonthValue(), lastDayOfPostMonth);

        for (Todo todo : todos) {
            Have have = haveRepository.getHaveByTodoId(todo.getId());
            if (todo.getEndedAtDate() == null) {
                response.add(GetTodoByMonthResDTO.builder().id(todo.getId()).todoContent(todo.getContent())
                        .todoStartedAt(todo.getStartedAtDate().toString()).todoEndedAt(null)
                        .categoryTextColorCode(have.getTextCode())
                        .categoryBgColorCode(have.getBgCode()).build());
            } else {
                response.add(GetTodoByMonthResDTO.builder().id(todo.getId()).todoContent(todo.getContent())
                        .todoStartedAt(todo.getStartedAtDate().toString()).todoEndedAt(todo.getEndedAtDate().toString())
                        .categoryTextColorCode(have.getTextCode())
                        .categoryBgColorCode(have.getBgCode()).build());
            }
        }

        return response;
    }

    @Override
    @Transactional
    public List<GetTodoByDayResDTO> getTodoByDay(String userId, String day) {
        List<GetTodoByDayResDTO> response = new ArrayList<>();

        LocalDate date = LocalDate.parse(day, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        List<Category> categories = categoryRepository.getCategoryListByUserId(userId);

        categories.forEach(category -> {
            List<TodoInGetTodoByDayDTO> todos = new ArrayList<>();
            List<Todo> todosForCategory = todoRepository.getAllTodoByCategoryIdAndDay(userId, category.getId(),
                    date.getYear(), date.getMonthValue(), date.getDayOfMonth());
            Have have = haveRepository.existsHaveRelationshipBetweenUserAndCategory(userId, category.getId());

            if (!todosForCategory.isEmpty()) {
                todosForCategory.forEach(todo -> {
                    todos.add(TodoInGetTodoByDayDTO.builder()
                            .todoId(todo.getId())
                            .content(todo.getContent())
                            .clearYN(todo.isClearYN())
                            .startedAtTime(todo.getStartedAtTime() != null ? String.valueOf(todo.getStartedAtTime()) : null)
                            .alertType(todo.getAlertType())
                            .getExperiencePointOrNot(todo.isGetExperiencePointOrNot())
                            .build());
                });

                response.add(GetTodoByDayResDTO.builder()
                        .categoryId(category.getId())
                        .categoryName(category.getName())
                        .categoryColorCode(have != null ? have.getColorCode() : null)
                        .todoList(todos)
                        .build());
            }
        });

        return response;
    }

    @Override
    public void unclearTodo(String userId, UnclearTodoReqDTO unclearTodoReqDTO) {
        String categoryId = unclearTodoReqDTO.getCategoryId();
        String todoId = unclearTodoReqDTO.getTodoId();

        Todo todo = todoRepository.getOneTodoByTodoId(todoId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXISTS_TODO_ID));

        if (!todo.isClearYN()) {
            throw new CustomException(ErrorCode.NOT_CLEARED_TODO);
        }

        if (haveRepository.existsHaveRelationshipBetweenUserAndCategory(userId, categoryId) == null){
            throw new CustomException(ErrorCode.WRONG_CATEGORY_ID);
        }
        if (!includeRepository.existsIncludeRelationshipBetweenCategoryAndTodo(categoryId, todoId)) {
            throw new CustomException(ErrorCode.NOT_EXISTS_RELATIONSHIP_BETWEEN_CATEGORY_AND_TODO);
        }

        todoRepository.updateClearYNToUnclearByTodoId(unclearTodoReqDTO.getTodoId());
    }

    @Override
    @Transactional
    public String deleteTodo(String userId, String todoId) {
        Todo todo = todoRepository.getTodoByUserIdAndTodoId(userId, todoId).orElseThrow(()
                -> new CustomException(ErrorCode.WRONG_USER_AND_TODO));

        if (todo.getRepeatType() == RepeatType.NONE_REPEAT) {
            todoRepository.deleteOneById(todoId);
        } else {
            String repeatCode = todo.getRepeatCode();
            todoRepository.deleteAllByRepeatCode(repeatCode);
        }

        return todo.getId();
    }

    @Override
    @Transactional
    public TodoDetailResDTO getTodoDetail(String userId, String todoId) {
        Todo todo = todoRepository.getTodoByUserIdAndTodoId(userId, todoId).orElseThrow(()
                -> new CustomException(ErrorCode.WRONG_USER_AND_TODO));
        Category category = categoryRepository.getCategoryByTodoId(todoId).orElseThrow(()
                -> new CustomException(ErrorCode.NOT_EXISTS_CATEGORY));
        Have have = haveRepository.existsHaveRelationshipBetweenUserAndCategory(userId, category.getId());
        return TodoDetailResDTO.builder().todoId(todo.getId()).categoryId(category.getId())
                .content(todo.getContent())
                .categoryName(category.getName()).categoryColorCode(have.getColorCode())
                .startedAtDate(todo.getStartedAtDate()).startedAtTime(todo.getStartedAtTime())
                .markOnTheCalenderOrNot(todo.isMarkOnTheCalenderOrNot())
                .alertAt(todo.getAlertAt()).alertType(todo.getAlertType())
                .repeatType(todo.getRepeatType()).repeatData(todo.getRepeatData())
                .repeatEndDate(todo.getRepeatEndDate())
                .endedAtDate(todo.getEndedAtDate()).endedAtTime(todo.getEndedAtTime()).build();
    }

    @Override
    @Transactional
    public String updateTodo(String userId, String todoId, UpdateTodoReqDTO updateInfos) {
        Todo todo = todoRepository.getOneTodoByTodoId(todoId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXISTS_TODO_ID));

        Category curCategory = categoryRepository.getCategoryByTodoId(todoId)
                .orElseThrow(() -> new CustomException(ErrorCode.CANT_FIND_TODOS_CATEGORY_INFO));
        String updatedCategoryId = updateInfos.getTodoInfos().get(0).getCategoryId();
        if (!curCategory.getId().equals(updatedCategoryId)) {
            includeRepository.deleteIncludeBetweenCategoryAndTodo(curCategory.getId(), todoId);
            includeRepository.createIncludeRelationshipBetweenCategoryAndTodo(todoId, updatedCategoryId);
        }

        if (todo.getRepeatType() == RepeatType.NONE_REPEAT) {
            TodoInfoReqDTO todoInfo = updateInfos.getTodoInfos().get(0);
            todoRepository.updateTodoByTodoId(todoId, todoInfo.getContent(), todoInfo.isReceiveAlert(),
                    todoInfo.isMarkOnTheCalenderOrNot(), todoInfo.getAlertAt(),
                    LocalDate.parse(todoInfo.getStartedAtDate()),
                    todoInfo.getAlertType());

            if (todoInfo.getStartedAtTime() == null) {
                todoRepository.deleteStartedAtTime(todoId);
            } else {
                todoRepository.updateTodoStartedAtTime(todoId, LocalTime.parse(todoInfo.getStartedAtTime()));
            }
            if (todoInfo.getEndedAtDate() == null) {
                todoRepository.deleteEndedAtDate(todoId);
            } else {
                todoRepository.updateTodoEndedAtDate(todoId, LocalDate.parse(todoInfo.getEndedAtDate()));
            }
            if (todoInfo.getEndedAtTime() == null) {
                todoRepository.deleteEndedAtTime(todoId);
            } else {
                todoRepository.updateTodoEndedAtTime(todoId, LocalTime.parse(todoInfo.getEndedAtTime()));
            }

            return todoId;
        }

        RepeatInfoReqDTO repeatInfo = updateInfos.getRepeatInfo();
        LocalDate repeatEndedAt = updateInfos.getEditFrom();

        todoRepository.endTheRepeatTodoByRepeatCode(todo.getRepeatCode(), repeatEndedAt.getYear(),
                repeatEndedAt.getMonthValue(), repeatEndedAt.getDayOfMonth());
        todoRepository.updateTodoRepeatEndDateByRepeatCode(todo.getRepeatCode(),
                updateInfos.getRepeatInfo().getRepeatEndDate());


        for (TodoInfoReqDTO todoInfo : updateInfos.getTodoInfos()) {
            if (haveRepository.existsHaveRelationshipBetweenUserAndCategory(userId, todoInfo.getCategoryId()) == null) {
                throw new CustomException(ErrorCode.WRONG_CATEGORY_ID);
            };

            String savedTodoId = UlidCreator.getUlid().toString();

            Todo.TodoBuilder todoBuilder = Todo.builder().id(savedTodoId)
                    .content(todoInfo.getContent())
                    .startedAtDate(LocalDate.parse(todoInfo.getStartedAtDate()))
                    .receiveAlert(todoInfo.isReceiveAlert()).clearYN(false)
                    .getExperiencePointOrNot(false).markOnTheCalenderOrNot(todoInfo.isMarkOnTheCalenderOrNot())
                    .alertAt(todoInfo.getAlertAt())
                    .alertType(todoInfo.getAlertType())
                    .repeatType(repeatInfo.getRepeatType())
                    .repeatData(repeatInfo.getRepeatData())
                    .repeatCode(todo.getRepeatCode());

            if (todoInfo.getStartedAtTime() != null) {
                todoBuilder.startedAtTime(LocalTime.parse(todoInfo.getStartedAtTime()));
            }
            if (todoInfo.getEndedAtDate() != null) {
                todoBuilder.endedAtDate(LocalDate.parse(todoInfo.getEndedAtDate()));
            }
            if (todoInfo.getEndedAtTime() != null) {
                todoBuilder.endedAtTime(LocalTime.parse(todoInfo.getEndedAtTime()));
            }
            if (updateInfos.getRepeatInfo().getRepeatEndDate() != null) {
                todoBuilder.repeatEndDate(updateInfos.getRepeatInfo().getRepeatEndDate());
            }
            if (updateInfos.getRepeatInfo().getRepeatStartDate() != null) {
                todoBuilder.repeatStartDate(updateInfos.getRepeatInfo().getRepeatStartDate());
            }

            todoRepository.save(todoBuilder.build());

            includeRepository.createIncludeRelationshipBetweenCategoryAndTodo(savedTodoId, todoInfo.getCategoryId());
        }

        return todoId;
    }

    @Override
    @Transactional
    public String endTheRepeatTodo(String userId, EndTheRepeatTodoReqDTO req) {
        Todo todo = todoRepository.getTodoByUserIdAndTodoId(userId, req.getTodoId()).orElseThrow(()
                -> new CustomException(ErrorCode.WRONG_USER_AND_TODO));

        LocalDate repeatEndedAt = LocalDate.parse(req.getRepeatEndDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String repeatCode = todo.getRepeatCode();
        todoRepository.endTheRepeatTodoByRepeatCode(repeatCode, repeatEndedAt.getYear(),
                repeatEndedAt.getMonthValue(), repeatEndedAt.getDayOfMonth());
        LocalDate updatedRepeatEndedAt = repeatEndedAt.minusDays(1);
        todoRepository.updateTodoRepeatEndDateByRepeatCode(repeatCode, updatedRepeatEndedAt);
        return req.getTodoId();
    }

    @Override
    public List<GetTodoByAlertTimeResDTO> getTodoByAlertAt(LocalDateTime alertAt) {
        List<GetTodoByAlertTimeResDTO> response = new ArrayList<>();

        List<Todo> todos = todoRepository.getAllTodoByAlertAt(alertAt);
        for (Todo todo : todos) {
            response.add(GetTodoByAlertTimeResDTO.builder().todoId(todo.getId())
                    .todoContent(todo.getContent()).build());
        }
        return response;
    }

    @Override
    @Transactional
    public void deleteAllCategoryAndTodoByUserId(String userId) {
        todoRepository.deleteAllTodoByUserId(userId);
        categoryRepository.deleteAllCategoryByUserId(userId);
    }

    @Override
    public String getHolidayInfo(String solYear, String solMonth) {
        String response = "";
        try {
            StringBuilder sb = new StringBuilder("https://apis.data.go.kr/B090041/openapi/service/SpcdeInfoService/getRestDeInfo?");
            sb.append("serviceKey=Rw4YdrK6RwpxZQhjWXfaqUybySBDQUaLQqR9xLLfEkqeTwqgDbuU7gzQpapJCbtnBOJN9zE%2BbJux9Jj5QYibZg%3D%3D");
            sb.append("&solYear=").append(solYear);
            sb.append("&solMonth=").append(solMonth);
            sb.append("&_type=json");
            URL url = new URL(sb.toString());

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");

            BufferedReader br;

            if (conn.getResponseCode() == 200) {
                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            }

            StringBuilder sb2 = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb2.append(line);
            }
            br.close();
            conn.disconnect();
            response = sb2.toString();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return response;
    }
}
