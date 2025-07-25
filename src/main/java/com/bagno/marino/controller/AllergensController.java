package com.bagno.marino.controller;

import com.bagno.marino.service.AllergensService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/allergens", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class AllergensController {

    @Autowired
    private AllergensService allergensService;

    @GetMapping()
    public ResponseEntity<?> getAllAllergens() {
        return ResponseEntity.ok(allergensService.getAllAllergens());
    }
}
