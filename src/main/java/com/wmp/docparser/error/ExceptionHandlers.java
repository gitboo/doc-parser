package com.wmp.docparser.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
@Slf4j
public class ExceptionHandlers extends BaseExceptionHandler {
    public ExceptionHandlers() {
        super(log);
        registerMapping(InvalidUriException.class, "S0201", "Invalid Uri", HttpStatus.BAD_REQUEST);
        registerMapping(NetworkErrorException.class, "S0202", "Network Error or Invalid Uri", HttpStatus.BAD_GATEWAY);
        registerMapping(ExceededDocSizeException.class, "S0203", "Document capacity has been exceeded.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
