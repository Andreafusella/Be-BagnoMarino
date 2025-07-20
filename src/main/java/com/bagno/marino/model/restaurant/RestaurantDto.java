package com.bagno.marino.model.restaurant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestaurantDto {

    private Integer id;
    private String name;
    private String description;
    private String address;
    private String phone;
    private String openingHours;
}
