package com.heyform.surveyservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalTime;


@RestController
@ResponseBody
public class ctlr {
    private final AnswerServiceClient answerServiceClient;

    public ctlr(AnswerServiceClient answerServiceClient) {
        this.answerServiceClient = answerServiceClient;
    }

    @GetMapping("/test")
    public String test() {
        LocalTime answer_time = answerServiceClient.getTime();
        LocalTime survey_time = LocalTime.now();

        return "answer time: " + answer_time + "  survey time: " + survey_time;
    }

}
