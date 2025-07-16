package com.bagno.marino.exception;

import com.bagno.marino.exception.model.Error;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@Getter
@Setter
@ResponseStatus(code = HttpStatus.BAD_REQUEST, value = HttpStatus.BAD_REQUEST)
public class ValidationException extends BaseException {
    public ValidationException() {
    }

    public ValidationException(List<Error> errors) {
        super.getErrors().addAll(errors);
    }

}
