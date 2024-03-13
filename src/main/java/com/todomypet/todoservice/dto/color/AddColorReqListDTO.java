package com.todomypet.todoservice.dto.color;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class AddColorReqListDTO {
    private List<AddColorReqDTO> colorList;
}
