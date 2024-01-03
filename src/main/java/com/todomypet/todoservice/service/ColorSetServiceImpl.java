package com.todomypet.todoservice.service;

import com.todomypet.todoservice.dto.color.AddColorReqDTO;
import com.todomypet.todoservice.dto.color.GetAllColorsResDTO;
import com.todomypet.todoservice.mapper.ColorSetMapper;
import com.todomypet.todoservice.domain.node.ColorSet;
import com.todomypet.todoservice.repository.ColorSetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ColorSetServiceImpl implements ColorSetService {

    private final ColorSetRepository colorSetRepository;
    private final ColorSetMapper colorSetMapper;

    @Override
    public String addColor(AddColorReqDTO addColorReqDTO) {
        return colorSetRepository.save(colorSetMapper.AddColorReqDTOToColorSet(addColorReqDTO)).getColorCode();
    }

    @Override
    public GetAllColorsResDTO getAllColors() {
        List<ColorSet> colorSets = colorSetRepository.getAllColors();
        List<String> response = new ArrayList<>();
        for (ColorSet colorSet : colorSets) {
            response.add(colorSet.getColorCode());
        }
        return GetAllColorsResDTO.builder().colorCodes(response).build();
    }
}
