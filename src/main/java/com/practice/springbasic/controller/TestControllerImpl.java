package com.practice.springbasic.controller;

import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;

@Validated
@RestController
public class TestControllerImpl implements TestController {

    @GetMapping(value = "/test")
    public String getTest(@RequestParam("hz") String hz,
                          @RequestParam("hz1") String hz1) {
        return "test";
    }
}