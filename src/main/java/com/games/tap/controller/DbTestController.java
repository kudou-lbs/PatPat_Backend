package com.games.tap.controller;

import com.games.tap.util.Echo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DbTestController {

    @RequestMapping(value = "/",method = RequestMethod.GET)
    public String hello(){
        return "Hello";
    }
}
