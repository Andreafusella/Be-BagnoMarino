package com.bagno.marino.model.category;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryUpdateDto {

    private Long id;
    private String name;
    private String icon;
    private Integer orderIndex;
    private Long subCategoryId;
}
