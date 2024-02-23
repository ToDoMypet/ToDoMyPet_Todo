package com.todomypet.todoservice.service;

import com.todomypet.todoservice.dto.category.*;

public interface CategoryService {
    AddCategoryResDTO addCategory(String userId, AddCategoryReqDTO addCategoryReqDTO);

    void deleteCategory(String userId, String categoryId);

    GetCategoryListResDTO getCategoryListByUser(String userId);

    UpdateCategoryResDTO updateCategory(String userId, String categoryId, UpdateCategoryReqDTO updateCategoryInfo);
}
