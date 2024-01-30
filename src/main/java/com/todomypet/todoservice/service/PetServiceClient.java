package com.todomypet.todoservice.service;

import com.todomypet.todoservice.dto.openFeign.FeignClientResDTO;
import com.todomypet.todoservice.dto.openFeign.UpdateExperiencePointReqDTO;
import com.todomypet.todoservice.dto.openFeign.UpdateExperiencePointResDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name="pet-service", url="${feign.pet.url}")
public interface PetServiceClient {
    @PutMapping(value = "/update-exp", consumes = "application/json")
    FeignClientResDTO<UpdateExperiencePointResDTO> updateExperiencePoint(@RequestHeader String userId,
                                                                         @RequestBody UpdateExperiencePointReqDTO req);

    @GetMapping(value = "/get-main-pet", consumes = "application/json")
    FeignClientResDTO<String> getMainPet(@RequestHeader String userId);
}
