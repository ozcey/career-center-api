package com.careercenter.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Please log in with a valid username and password!")
public class UnauthorizedException extends RuntimeException {

    private static final long serialVersionUID = 1L;
}
