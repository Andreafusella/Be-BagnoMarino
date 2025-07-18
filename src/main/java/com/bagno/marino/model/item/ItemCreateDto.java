package com.bagno.marino.model.item;

import com.bagno.marino.model.base.BaseCreateDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemCreateDto extends BaseCreateDto {

    private String title;
    private String description;
    private Double price;
    private String category;

    //eventuali allergeni piu avanti
}
