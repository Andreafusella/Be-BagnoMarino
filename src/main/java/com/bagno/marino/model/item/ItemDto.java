package com.bagno.marino.model.item;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemDto {

    private Long id;
    private String title;
    private String description;
    private Double price;
}
