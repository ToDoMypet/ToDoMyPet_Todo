package com.todomypet.todoservice.service;

import com.todomypet.todoservice.dto.color.AddColorReqDTO;
import com.todomypet.todoservice.dto.color.GetAllColorsResDTO;

public interface ColorSetService {
    String addColor(AddColorReqDTO addColorReqDTO);

    GetAllColorsResDTO getAllColors();
}
