package com.bagno.marino.model.restaurant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestaurantUpdateDto {

    private Long id;
    private String name;
    private String description;
    private String address;
    private String phone;
    private String openingTime;
    private String closingTime;
    private String email;
}
