package com.todomypet.todoservice.service;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name="user-service", url="${feign.pet.url}")
public interface UserServiceClient {
}
