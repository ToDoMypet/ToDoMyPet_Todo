package com.todomypet.todoservice.controller;

import com.todomypet.todoservice.dto.SuccessResDTO;
import com.todomypet.todoservice.dto.color.AddColorReqDTO;
import com.todomypet.todoservice.dto.color.AddColorResDTO;
import com.todomypet.todoservice.dto.color.GetAllColorsResDTO;
import com.todomypet.todoservice.service.ColorSetService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/color")
@RequiredArgsConstructor
public class ColorSetController {

    private final ColorSetService colorSetService;

    @PostMapping("/add")
    public SuccessResDTO<AddColorResDTO> addColor(@RequestBody AddColorReqDTO addColorReqDTO) {
        AddColorResDTO response =
                AddColorResDTO.builder().mainColorId(colorSetService.addColor(addColorReqDTO)).build();
        return new SuccessResDTO<AddColorResDTO>(response);
    }

    @GetMapping("")
    public SuccessResDTO<GetAllColorsResDTO> getAllColors(@RequestHeader String userId) {
        GetAllColorsResDTO response = colorSetService.getAllColors();
        return new SuccessResDTO<GetAllColorsResDTO>(response);
    }
}
