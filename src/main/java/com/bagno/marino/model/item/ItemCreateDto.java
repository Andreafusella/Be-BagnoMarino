package com.bagno.marino.model.item;

import com.bagno.marino.model.base.BaseCreateDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ItemCreateDto extends BaseCreateDto {

    private String name;
    private String description;
    private Double price;
    private Long category;
    private Boolean available;
    private Boolean special;
    private Boolean frozen;
    private Integer orderIndex;
    List<Long> allergensIds;
}
