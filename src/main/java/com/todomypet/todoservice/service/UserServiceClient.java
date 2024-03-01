package com.todomypet.todoservice.service;

import com.todomypet.todoservice.dto.openFeign.AchieveReqDTO;
import com.todomypet.todoservice.dto.openFeign.CheckAchieveOrNotReqDTO;
import com.todomypet.todoservice.dto.openFeign.CheckAchievementOrNotResDTO;
import com.todomypet.todoservice.dto.openFeign.FeignClientResDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name="user-service", url="${feign.user.url}")
public interface UserServiceClient {
    @GetMapping(value = "/increase-and-get-todo-clear-count", consumes = "application/json")
    FeignClientResDTO<Integer> increaseAndGetTodoClearCount(@RequestHeader String userId);

    @PostMapping(value = "/achievement/achieve-or-not", consumes = "application/json")
    FeignClientResDTO<CheckAchievementOrNotResDTO> checkAchieveOrNot(@RequestHeader String userId,
                                                                     @RequestBody CheckAchieveOrNotReqDTO req);

    @PostMapping(value = "/achievement", consumes = "application/json")
    FeignClientResDTO<Void> achieve(@RequestHeader String userId,
                                    @RequestBody AchieveReqDTO achieveReqDTO);

}
