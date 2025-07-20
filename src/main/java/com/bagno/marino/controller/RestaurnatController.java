package com.bagno.marino.controller;

import com.bagno.marino.model.restaurant.RestaurantCreateDto;
import com.bagno.marino.model.restaurant.RestaurantDto;
import com.bagno.marino.model.restaurant.RestaurantUpdateDto;
import com.bagno.marino.service.RestaurantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/restaurant", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class RestaurnatController {

    @Autowired
    private RestaurantService restaurantService;

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> save(@RequestBody RestaurantCreateDto data) {
        restaurantService.save(data);
        return ResponseEntity.ok().build();
    }

    @PutMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> update(@RequestBody RestaurantUpdateDto data) {
        restaurantService.update(data);
        return ResponseEntity.ok().build();
    }

    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getInfo() {
        RestaurantDto response = restaurantService.getInfo();
        return ResponseEntity.ok().body(response);
    }
}
