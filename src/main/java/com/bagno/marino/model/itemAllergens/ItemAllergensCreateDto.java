package com.bagno.marino.model.itemAllergens;

import com.bagno.marino.model.base.BaseCreateDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemAllergensCreateDto extends BaseCreateDto {

    private Long itemId;
    private Long allergenId;

}
