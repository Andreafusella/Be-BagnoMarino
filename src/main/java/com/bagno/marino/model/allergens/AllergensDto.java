package com.bagno.marino.model.allergens;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AllergensDto {

    private Long id;
    private String symbol;
    private String name;
    private String description;
}
