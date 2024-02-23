package com.todomypet.todoservice.service;

import com.github.f4b6a3.ulid.UlidCreator;
import com.todomypet.todoservice.domain.node.ColorSet;
import com.todomypet.todoservice.domain.node.Todo;
import com.todomypet.todoservice.dto.category.*;
import com.todomypet.todoservice.domain.node.Category;
import com.todomypet.todoservice.exception.CustomException;
import com.todomypet.todoservice.exception.ErrorCode;
import com.todomypet.todoservice.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final HaveRepository haveRepository;
    private final ColorSetRepository colorSetRepository;
    private final TodoRepository todoRepository;
    private final IncludeRepository includeRepository;

    @Override
    @Transactional
    public AddCategoryResDTO addCategory(String userId, AddCategoryReqDTO addCategoryReqDTO) {
        Category category = Category.builder()
                .name(addCategoryReqDTO.getName())
                .id(String.valueOf(UlidCreator.getUlid())).build();

        if (haveRepository.existsHaveRelationshipBetweenUserAndCategoryName(userId, addCategoryReqDTO.getName()) != null) {
            return AddCategoryResDTO.builder().duplicatedOrNot(true).categoryId(null).build();
        }

        String savedCategoryId = categoryRepository.save(category).getId();

        ColorSet colorSet = colorSetRepository.getColorSetByColorCode(addCategoryReqDTO.getColorCode());

        haveRepository.createHaveRelationshipBetweenUserAndCategory(userId, savedCategoryId,
                colorSet.getColorCode(), colorSet.getBgCode(), colorSet.getTextCode());

        return AddCategoryResDTO.builder().categoryId(savedCategoryId).duplicatedOrNot(false).build();
    }

    @Override
    @Transactional
    public void deleteCategory(String userId, String categoryId) {
        List<Todo> todoList = todoRepository.getAllTodoByCategoryId(userId, categoryId);
        String defaultCategoryId = categoryRepository.getDefaultCategoryIdByUserId(userId).getId();

        if (defaultCategoryId.equals(categoryId)) {
            throw new CustomException(ErrorCode.DEFAULT_CATEGORY_CANT_DELETED);
        }

        for (Todo todo : todoList) {
            includeRepository.createIncludeRelationshipBetweenCategoryAndTodo(todo.getId(), defaultCategoryId);
        }

        categoryRepository.deleteCategoryById(categoryId);
    }

    @Override
    @Transactional
    public GetCategoryListResDTO getCategoryListByUser(String userId) {
        List<Category> categories = categoryRepository.getCategoryListByUserId(userId);
        List<CategoryInfoResDTO> response = new ArrayList<>();
        for (Category category : categories) {
            response.add(CategoryInfoResDTO.builder()
                    .categoryId(category.getId())
                    .categoryName(category.getName())
                    .colorCode(haveRepository
                            .existsHaveRelationshipBetweenUserAndCategory(userId, category.getId()).getColorCode())
                    .build());
        }
        return GetCategoryListResDTO.builder().categoryList(response).build();
    }

    @Override
    @Transactional
    public UpdateCategoryResDTO updateCategory(String userId, String categoryId, UpdateCategoryReqDTO updateCategoryInfo) {
        Category category = Category.builder()
                .name(updateCategoryInfo.getName())
                .id(String.valueOf(UlidCreator.getUlid())).build();

        if (haveRepository.existsHaveRelationshipBetweenUserAndCategoryName(userId, updateCategoryInfo.getName()) != null) {
            return UpdateCategoryResDTO.builder().duplicatedOrNot(true).categoryId(null).build();
        }

        categoryRepository.updateCategoryName(categoryId, updateCategoryInfo.getName());

        ColorSet colorSet = colorSetRepository.getColorSetByColorCode(updateCategoryInfo.getColorCode());

        haveRepository.updateHaveRelationshipColor(userId, categoryId, colorSet.getColorCode(), colorSet.getBgCode(),
                colorSet.getTextCode());

        return UpdateCategoryResDTO.builder().categoryId(categoryId).duplicatedOrNot(false).build();
    }
}
