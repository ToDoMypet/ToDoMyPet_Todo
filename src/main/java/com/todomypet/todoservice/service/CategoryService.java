package com.todomypet.todoservice.service;

import com.todomypet.todoservice.dto.category.AddCategoryReqDTO;
import com.todomypet.todoservice.dto.category.AddCategoryResDTO;
import com.todomypet.todoservice.dto.category.GetCategoryListResDTO;

public interface CategoryService {
    AddCategoryResDTO addCategory(String userId, AddCategoryReqDTO addCategoryReqDTO);

    void deleteCategory(String userId, String categoryId);

    GetCategoryListResDTO getCategoryListByUser(String userId);
}
