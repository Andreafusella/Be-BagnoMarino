package com.bagno.marino.exception;

import com.bagno.marino.exception.model.Error;
import com.bagno.marino.exception.model.InternalErrorCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@Setter
@ResponseStatus(code = HttpStatus.NOT_FOUND, value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends BaseException {

    public ResourceNotFoundException() {
    }

    public ResourceNotFoundException(Class enityClass, String id) {
        String message = String.format("%s with id %s not found", enityClass.getSimpleName(), id);
        Error error = new Error(InternalErrorCode.RESOURCE_NOT_FOUND, message);
        this.getErrors().add(error);
    }
}
