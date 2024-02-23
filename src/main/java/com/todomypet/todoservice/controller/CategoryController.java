package com.todomypet.todoservice.controller;

import com.todomypet.todoservice.dto.SuccessResDTO;
import com.todomypet.todoservice.dto.category.*;
import com.todomypet.todoservice.service.CategoryService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Category Controller")
@SecurityRequirement(name = "bearer-key")
@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "카테고리 목록 조회", description = "유저가 가진 카테고리를 모두 조회합니다.")
    @GetMapping("/category")
    public SuccessResDTO<GetCategoryListResDTO> getCategoryList(@RequestHeader String userId) {
        GetCategoryListResDTO response = categoryService.getCategoryListByUser(userId);
        return new SuccessResDTO<GetCategoryListResDTO>(response);
    }

    @Operation(summary = "카테고리 추가", description = "카테고리를 추가합니다. 카테고리명은 중복 불가능합니다.")
    @PostMapping("/category")
    public SuccessResDTO<AddCategoryResDTO> addCategory(@RequestHeader String userId,
                                                     @RequestBody AddCategoryReqDTO addCategoryReqDTO) {
        AddCategoryResDTO response = categoryService.addCategory(userId, addCategoryReqDTO);
        return new SuccessResDTO<AddCategoryResDTO>(response);
    }

    @PutMapping("/category/{categoryId}")
    public SuccessResDTO<UpdateCategoryResDTO> updateCategory(@RequestHeader String userId, @PathVariable String categoryId,
                                                              @RequestBody UpdateCategoryReqDTO updateCategoryInfo) {
        UpdateCategoryResDTO response = categoryService.updateCategory(userId, categoryId, updateCategoryInfo);
        return new SuccessResDTO<UpdateCategoryResDTO>(response);
    }

    @Operation(summary = "카테고리 삭제", description = "카테고리를 삭제합니다. 포함된 todo는 모두 미분류로 이동됩니다.")
    @DeleteMapping("/category/{categoryId}")
    public SuccessResDTO<Void> deleteCategory(@RequestHeader String userId, @PathVariable String categoryId) {
        categoryService.deleteCategory(userId, categoryId);
        return new SuccessResDTO<Void>(null);
    }
}
