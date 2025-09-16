package com.it.graphql_gateway_skeleton.controller;

import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Andy Wang
 * @Created 2025/9/16 17:16
 */
@RestController
@RequestMapping("/api/v1")
public class UserController {

    @QueryMapping
    @GetMapping("/user")
    public String getUser(){
        return "This is getting user api";
    }
}
