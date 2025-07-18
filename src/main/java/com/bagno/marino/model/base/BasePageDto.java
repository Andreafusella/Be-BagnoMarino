package com.bagno.marino.model.base;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class BasePageDto<DTO> {
    public List<DTO> content = new ArrayList<>();
    private int pageElements;
    private long totalCount;

    public int getPageElements() {
        return content.size();
    }
}
