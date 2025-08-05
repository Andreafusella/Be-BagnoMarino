package com.bagno.marino.model.category;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CategoryUpdateDto {

    private Long id;
    private String name;
    private String icon;
    private Integer orderIndex;
    private Long subCategoryId;
}
