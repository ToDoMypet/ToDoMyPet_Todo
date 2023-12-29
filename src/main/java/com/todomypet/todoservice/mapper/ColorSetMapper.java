package com.todomypet.todoservice.mapper;

import com.todomypet.todoservice.dto.color.AddColorReqDTO;
import com.todomypet.todoservice.node.ColorSet;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ColorSetMapper {
    ColorSet AddColorReqDTOToColorSet(AddColorReqDTO addColorReqDTO);
}
