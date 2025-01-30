package com.gnimtier.api.controller.error;

import com.gnimtier.api.exception.CustomException;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController implements ErrorController {
    private static final String ERROR_PATH = "/error";

    @RequestMapping(value = ERROR_PATH)
    public void error() {
        throw new CustomException("404", HttpStatus.NOT_FOUND);
    }
}
