package com.todomypet.todoservice.controller;

import com.todomypet.todoservice.dto.SuccessResDTO;
import com.todomypet.todoservice.dto.color.AddColorReqDTO;
import com.todomypet.todoservice.dto.color.AddColorReqListDTO;
import com.todomypet.todoservice.dto.color.AddColorResDTO;
import com.todomypet.todoservice.dto.color.GetAllColorsResDTO;
import com.todomypet.todoservice.service.ColorSetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "ColorSet Controller")
@RestController
@RequestMapping("/color")
@RequiredArgsConstructor
public class ColorSetController {

    private final ColorSetService colorSetService;

    @Operation(summary = "컬러셋 추가", description = "컬러셋을 추가합니다. 어드민 전용 api입니다.")
    @PostMapping("/add")
    public SuccessResDTO<Void> addColor(@RequestBody AddColorReqListDTO req) {
        colorSetService.addColor(req);
        return new SuccessResDTO<Void>(null);
    }

    @Operation(summary = "컬러셋 조회", description = "모든 컬러셋을 조회합니다.")
    @GetMapping("")
    public SuccessResDTO<GetAllColorsResDTO> getAllColors(@RequestHeader String userId) {
        GetAllColorsResDTO response = colorSetService.getAllColors();
        return new SuccessResDTO<GetAllColorsResDTO>(response);
    }
}
