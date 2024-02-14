package com.todomypet.todoservice.dto.category;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CategoryInfoResDTO {
    private String categoryId;
    private String categoryName;
    private String colorCode;
}
