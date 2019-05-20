package com.legicycle.backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class MainController {

    @GetMapping(value = "/search", produces="application/json")
    public ResponseEntity<?> getSearchFields()
    {
        return null;
    }

    @PostMapping(value = "/search", consumes = "application/json", produces="application/json")
    public ResponseEntity<?> searchByCriteria()
    {
        return null;
    }
}
