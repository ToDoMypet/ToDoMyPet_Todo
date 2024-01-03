package com.todomypet.todoservice.dto.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
public class AddCategoryResDTO {
    private String categoryId;
}
