package com.todomypet.todoservice.service;

import com.todomypet.todoservice.dto.color.AddColorReqDTO;
import com.todomypet.todoservice.mapper.ColorSetMapper;
import com.todomypet.todoservice.node.ColorSet;
import com.todomypet.todoservice.repository.ColorSetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ColorSetServiceImpl implements ColorSetService {

    private final ColorSetRepository colorSetRepository;
    private final ColorSetMapper colorSetMapper;

    @Override
    public String addColor(AddColorReqDTO addColorReqDTO) {
        return colorSetRepository.save(colorSetMapper.AddColorReqDTOToColorSet(addColorReqDTO)).getColorCode();
    }
}
