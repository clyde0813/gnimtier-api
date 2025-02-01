package com.gnimtier.api.controller.error;

import com.gnimtier.api.exception.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {
    private static final String ERROR_PATH = "/error";

    @RequestMapping(value = ERROR_PATH)
    public void error() {
        throw new CustomException("Bad Request", HttpStatus.BAD_REQUEST);
    }
}
