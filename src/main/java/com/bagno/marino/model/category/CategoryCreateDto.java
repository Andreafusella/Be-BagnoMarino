package com.bagno.marino.model.category;

import com.bagno.marino.model.base.BaseCreateDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryCreateDto extends BaseCreateDto {

    private String name;
    private String icon;
    private Integer orderIndex;
    private Long subCategoryId;
}
