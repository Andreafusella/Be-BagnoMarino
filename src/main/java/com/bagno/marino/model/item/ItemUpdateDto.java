package com.bagno.marino.model.item;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ItemUpdateDto {

    private Long id;
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
