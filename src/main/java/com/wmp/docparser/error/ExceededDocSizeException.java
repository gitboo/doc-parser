package com.wmp.docparser.error;

public class ExceededDocSizeException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public ExceededDocSizeException() {
        super();
    }
    public ExceededDocSizeException(String s) {
        super(s);
    }

    public ExceededDocSizeException(String s, Throwable t) {
        super(s, t);
    }
}
