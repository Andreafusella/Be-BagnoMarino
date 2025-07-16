package com.bagno.marino.exception.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * @author MarioArcomano
 */
@Setter
@Getter
@NoArgsConstructor
public class ErrorResponse {

    private Instant timestamp;
    private String path;
    private int status;
    private String error;
    private List<Error> errors = new ArrayList<>();
}
