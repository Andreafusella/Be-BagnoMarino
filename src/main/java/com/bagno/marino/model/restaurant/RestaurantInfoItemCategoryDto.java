package com.bagno.marino.model.restaurant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestaurantInfoItemCategoryDto {

    private long category;
    private int itemAvailable;
    private int itemNotAvailable;
}
