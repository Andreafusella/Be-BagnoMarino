package com.bagno.marino.model.base;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public class BaseGetDto extends BaseAuditing{
    protected Long id;
}
