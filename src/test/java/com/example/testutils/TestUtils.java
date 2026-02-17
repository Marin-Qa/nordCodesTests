package com.example.testutils;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TestUtils {

    public String genToken(){
        return UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }
}
