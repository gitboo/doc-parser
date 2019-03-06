package com.wmp.docparser.error;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseExceptionHandler {
    private static final ExceptionMapping DEFAULT_ERROR = new ExceptionMapping(
            "S9999",
            "Internal server error",
            HttpStatus.INTERNAL_SERVER_ERROR);

    private final Logger log;
    private final Map<Class<?>, ExceptionMapping> exceptionMappings = new HashMap<>();

    public BaseExceptionHandler(final Logger log) {
        this.log = log;

        registerMapping(
                MissingServletRequestParameterException.class,
                "E0101",
                "Missing request parameter",
                HttpStatus.BAD_REQUEST);
        registerMapping(
                MethodArgumentTypeMismatchException.class,
                "E0102",
                "Argument type mismatch",
                HttpStatus.BAD_REQUEST);
        registerMapping(
                HttpRequestMethodNotSupportedException.class,
                "E0103",
                "HTTP method not supported",
                HttpStatus.METHOD_NOT_ALLOWED);
        registerMapping(
                ServletRequestBindingException.class,
                "E0104",
                "Missing header in request",
                HttpStatus.BAD_REQUEST);
        registerMapping(
                IllegalArgumentException.class,
                "E0105",
                "Illegal request parameter",
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Throwable.class)
    @ResponseBody
    public RestError handleThrowable(final Throwable ex, final HttpServletResponse response) {
        ExceptionMapping mapping = exceptionMappings.getOrDefault(ex.getClass(), DEFAULT_ERROR);

        response.setStatus(mapping.status.value());
        log.error("{} ({}): {} ", mapping.code, mapping.message, ex.toString());
        return new RestError(mapping.code, mapping.message, ex.getMessage());
    }

    protected void registerMapping(final Class<?> clazz, final String code, final String message, final HttpStatus status) {
        exceptionMappings.put(clazz, new ExceptionMapping(code, message, status));
    }

    @AllArgsConstructor
    private static class ExceptionMapping {
        private final String code;
        private final String message;
        private final HttpStatus status;
    }
}
