package com.bagno.marino.model.restaurant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestaurantDto {

    private Long id;
    private String name;
    private String description;
    private String address;
    private String phone;
    private String openingTime;
    private String closingTime;
    private String email;
}
