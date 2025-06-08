package com.telconova.tracking.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/avances")
public class AvancesController {

    @GetMapping
    public Map<String, String> getAvances() {
        return Collections.singletonMap("message", "Endpoint de avances funcionando correctamente");
    }
}
