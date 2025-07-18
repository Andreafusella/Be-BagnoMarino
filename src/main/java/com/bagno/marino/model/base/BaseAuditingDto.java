package com.bagno.marino.model.base;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@MappedSuperclass
@Getter
@Setter
public class BaseAuditingDto {

    protected Instant createDate;

    protected Integer createBy;

    protected Instant modifyDate;

    protected Integer modifyBy;
}
