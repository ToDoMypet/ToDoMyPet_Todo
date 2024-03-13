package com.todomypet.todoservice.dto.color;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddColorReqListDTO {
    private List<AddColorReqDTO> colorList;
}
