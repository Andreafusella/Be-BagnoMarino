package com.bagno.marino.model.item;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ItemWithCategoryDto {
    private Long categoryId;
    private String category;
    private String categoryIcon;
    private List<ItemDto> items;
}
