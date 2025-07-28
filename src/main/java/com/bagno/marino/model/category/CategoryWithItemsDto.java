package com.bagno.marino.model.category;

import com.bagno.marino.model.item.ItemDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CategoryWithItemsDto {
    private Long id;
    private String name;
    private String icon;
    private List<ItemDto> items;
    private List<CategoryWithItemsDto> subcategories;
}
