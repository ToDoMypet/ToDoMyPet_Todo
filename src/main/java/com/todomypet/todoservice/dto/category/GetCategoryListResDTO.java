package com.todomypet.todoservice.dto.category;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class GetCategoryListResDTO {
    List<CategoryInfoResDTO> categoryList;
}
