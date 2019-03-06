package com.wmp.docparser.error;

public class InvalidUriException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public InvalidUriException() {
        super();
    }
    public InvalidUriException(String s) {
        super(s);
    }

    public InvalidUriException(String s, Throwable t) {
        super(s, t);
    }
}
