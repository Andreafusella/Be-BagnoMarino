package com.bagno.marino.model.restaurant;

import com.bagno.marino.model.base.BaseCreateDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestaurantCreateDto extends BaseCreateDto {

    private String name;
    private String description;
    private String address;
    private String phone;
    private String openingHours;

}
