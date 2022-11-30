package com.heyform.userservice;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalTime;

@FeignClient(name = "ANSWER-SERVICE")
public interface AnswerServiceClient {
    @GetMapping("/test")
    LocalTime getTime();

}
