package com.todomypet.todoservice.service;

import com.todomypet.todoservice.dto.color.AddColorReqDTO;
import com.todomypet.todoservice.dto.color.AddColorReqListDTO;
import com.todomypet.todoservice.dto.color.GetAllColorsResDTO;

public interface ColorSetService {
    void addColor(AddColorReqListDTO req);

    GetAllColorsResDTO getAllColors();
}
