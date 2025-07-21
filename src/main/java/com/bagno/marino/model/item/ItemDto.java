package com.bagno.marino.model.item;

import com.bagno.marino.model.allergens.AllergensDto;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ItemDto {

    private Long id;
    private String title;
    private String description;
    private Double price;
    private List<AllergensDto> allergenes = new ArrayList<>();
}
