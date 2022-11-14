package com.practice.springbasic.controller;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.Min;

public interface TestController {
    String getTest(@Min(value = 3, message = "min 3") String hz, String hz1);

}
