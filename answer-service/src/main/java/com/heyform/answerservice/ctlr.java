package com.heyform.answerservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalTime;

@RestController
@ResponseBody
public class ctlr {
    @GetMapping("/test")
    public LocalTime test() {
        LocalTime now = LocalTime.now();

        return now;
    }

    @GetMapping("/testmsg")
    public String testMsg(@Value("${message}") String msg) {
        return msg;
    }
}
